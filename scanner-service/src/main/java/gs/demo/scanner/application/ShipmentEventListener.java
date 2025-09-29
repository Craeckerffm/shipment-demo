package gs.demo.scanner.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import gs.demo.scanner.api.dto.ShipmentEventDto;
import gs.demo.scanner.domain.entity.InboxEvent;
import gs.demo.scanner.domain.repository.InboxEventRepository;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.reactive.messaging.kafka.api.IncomingKafkaRecordMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static io.quarkus.arc.ComponentsProvider.LOG;

@ApplicationScoped
@Blocking
public class ShipmentEventListener {

    @Inject
    InboxEventRepository inboxEventRepository;

    @Inject
    Validator validator;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    DemoEventsPublisher demoEventsEmitter;

    @Incoming("shipment-created")
    @Blocking
    @Transactional
    public CompletionStage<Void> onShipmentCreated(ConsumerRecord<String, String> record) {

        String messageKey = record.key();
        String jsonPayload = record.value();

        ShipmentEventDto eventDto;
        try {
            eventDto = objectMapper.readValue(jsonPayload, ShipmentEventDto.class);
        } catch (Exception e) {
            LOG.errorf("Failed to deserialize message payload: %s", e.getMessage());
            return CompletableFuture.failedFuture(new IllegalArgumentException("Invalid JSON payload"));
        }

        var violations = validator.validate(eventDto);
        if (!violations.isEmpty()) {
            LOG.errorf("Validation failed for shipment event: %s", violations);
            return CompletableFuture.failedFuture(new IllegalArgumentException("Invalid event data"));
        }

        LOG.infof("Received shipment event - MessageKey: %s, TrackingNumber: %s, Status: %s",
                messageKey, eventDto.aggregateId(), eventDto.eventType());

        if (messageKey == null || messageKey.isEmpty()) {
            LOG.warnf("Rejected Message with empty key - TrackingNumber: %s", eventDto.aggregateId());
            return CompletableFuture.failedFuture(new IllegalArgumentException("Message key is required"));
        }

        if (inboxEventRepository.alreadyHandled(eventDto.eventId())) {
            LOG.infof("Message with key %s already handled", messageKey);
            return CompletableFuture.completedFuture(null);
        }

        InboxEvent inboxEvent = new InboxEvent();
        inboxEvent.setEventId(eventDto.eventId());
        inboxEvent.setAggregateId(eventDto.aggregateId());
        inboxEvent.setReceivedAt(Instant.now());
        inboxEvent.setAggregateType("Shipment");
        inboxEvent.setEventType(eventDto.eventType().toString());

        try {
            inboxEvent.persistAndFlush();
            LOG.infof("Successfully processed event - EventId: %s", messageKey);
            demoEventsEmitter.emmitFor(inboxEvent);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            LOG.errorf(e, "Error processing event - EventId: %s", messageKey);
            return CompletableFuture.failedFuture(e);
        }
    }
}