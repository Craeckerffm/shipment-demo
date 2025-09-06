package gs.demo.scanner.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import gs.demo.scanner.api.dto.ShipmentEventDto;
import gs.demo.scanner.domain.entity.InboxEvent;
import gs.demo.scanner.domain.enums.ShipmentStatus;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import static io.quarkus.arc.ComponentsProvider.LOG;

@ApplicationScoped
public class DemoEventsPublisher {

    @Inject
    @Channel("shipment-updated")
    Emitter<String> shipmentEmitter;

    @Inject
    ObjectMapper objectMapper;

    void emmitFor(InboxEvent event) {

        List<ShipmentEventDto> events = new ArrayList<>();
        events.add(new ShipmentEventDto(
                UUID.randomUUID().toString(),
                ShipmentStatus.PICKED_UP,
                event.aggregateId,
                Instant.now().plus(Duration.ofDays(1))));
        events.add(new ShipmentEventDto(
                UUID.randomUUID().toString(),
                ShipmentStatus.IN_TRANSIT,
                event.aggregateId,
                Instant.now().plus(Duration.ofDays(2))));
        events.add(new ShipmentEventDto(
                UUID.randomUUID().toString(),
                ShipmentStatus.OUT_FOR_DELIVERY,
                event.aggregateId,
                Instant.now().plus(Duration.ofDays(3))));
        events.add(new ShipmentEventDto(
                UUID.randomUUID().toString(),
                ShipmentStatus.DELIVERED,
                event.aggregateId,
                Instant.now().plus(Duration.ofDays(4))));

        events.forEach(dto -> {
            try {
                String payload = objectMapper.writeValueAsString(dto);

                KafkaRecord<String, String> record = KafkaRecord.of(event.aggregateId, payload)
                        .withHeader("eventType", "SHIPMENT")
                        .withHeader("aggregateType", "UPDATE")
                        .withHeader("producerService", "scanner-service")
                        .withHeader("correlationId", event.aggregateId)
                        .withHeader("timestamp", dto.occurredOn().toString());

                shipmentEmitter.send(record);

                LOG.infof("Successfully published event for aggregate %s with status %s",
                        event.aggregateId, dto.eventType());
                Thread.sleep(5000);


            } catch (Exception e) {
                LOG.errorf(e, "Failed to publish event for aggregate %s with status %s",
                        event.aggregateId, dto.eventType());
            }
        });

    }
}
