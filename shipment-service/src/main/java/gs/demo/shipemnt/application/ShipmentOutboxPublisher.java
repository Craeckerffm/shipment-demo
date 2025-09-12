package gs.demo.shipemnt.application;

import gs.demo.shipemnt.domain.entity.OutboxEvent;
import gs.demo.shipemnt.domain.enums.EventStatus;
import gs.demo.shipemnt.domain.repository.OutboxEventRepository;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.List;

import static io.quarkus.arc.ComponentsProvider.LOG;


@ApplicationScoped
public class ShipmentOutboxPublisher {

    @Inject
    OutboxEventRepository repository;

    @Inject
    @Channel("shipment-created")
    Emitter<String> shipmentEmitter;

    @Scheduled(every = "5s")
    @Transactional
    void checkForShipments() {
        List<OutboxEvent> pendingEvents = repository.findAllPending();

        LOG.infof("Found %d pending outbox events", pendingEvents.size());

        for (OutboxEvent event : pendingEvents) {
            try {

                KafkaRecord<String, String> record = KafkaRecord.of(event.getAggregateId(), event.getPayload())
                        .withHeader("eventType", event.getEventType().name())
                        .withHeader("aggregateType", event.getAggregateId())
                        .withHeader("producerService", "shipment-service")
                        .withHeader("correlationId", event.getAggregateId())
                        .withHeader("timestamp", event.getOccurredOn().toString());

                shipmentEmitter.send(record);
                event.setProcessed(true);
                event.setStatus(EventStatus.SUCCESS);
                repository.save(event);

                LOG.infof("Successfully published event %d for aggregate %s",
                        event.getId(), event.getAggregateId());

            } catch (Exception e) {
                LOG.errorf(e, "Failed to publish event %d for aggregate %s",
                        event.getId(), event.getAggregateId());

                event.setStatus(EventStatus.FAILED);
                repository.save(event);
            }
        }
    }
}
