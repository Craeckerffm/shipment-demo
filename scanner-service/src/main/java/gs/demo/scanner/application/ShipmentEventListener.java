package gs.demo.scanner.application;

import gs.demo.scanner.domain.entity.InboxEvent;
import gs.demo.scanner.domain.repository.InboxEventRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.time.Instant;

import static io.quarkus.arc.ComponentsProvider.LOG;

@ApplicationScoped
public class ShipmentEventListener {

    @Inject
    InboxEventRepository inboxEventRepository;

    @Incoming("shipment-created")
    @Transactional
    public void onShipmentCreated(ConsumerRecord<String, Double> record) {

        if (record.key() == null || record.key().isEmpty()) {
            LOG.warnf("Rejected Message with empty key - Topic: %s, Partition: %d, Offset: %d",
                    record.topic(), record.partition(), record.offset());
            return;
        }

        String eventId = extractHeaderAsString(record, "eventId");

        if (eventId == null || eventId.isEmpty()) {
            LOG.warnf("Rejected Message with missing eventId - Key: %s",
                    record.key());
            return;
        }

        if (inboxEventRepository.alreadyHandled(eventId)) {
            LOG.infof("Message with key  %s- already handled", record.key());
            return;
        }

        String eventType = extractHeaderAsString(record, "eventType");
        String aggregateType = extractHeaderAsString(record, "aggregateType");

        InboxEvent inboxEvent = new InboxEvent();
        inboxEvent.eventId = eventId;
        inboxEvent.aggregateId = record.key();
        inboxEvent.receivedAt = Instant.now();
        inboxEvent.aggregateType = aggregateType;
        inboxEvent.eventType = eventType;

        try {
            inboxEvent.persistAndFlush();
            LOG.infof("Successfully processed event - EventId: %s", eventId);

        } catch (Exception e) {
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                LOG.infof("Message with key  %s- already handled", record.key());
            } else {
                LOG.errorf(e, "Error processing event - EventId: %s", eventId);
            }
        }
    }

    private String extractHeaderAsString(ConsumerRecord<String, Double> record, String headerName) {
        if (record.headers() != null) {
            var header = record.headers().lastHeader(headerName);
            if (header != null && header.value() != null) {
                return new String(header.value());
            }
        }
        return null;
    }

}
