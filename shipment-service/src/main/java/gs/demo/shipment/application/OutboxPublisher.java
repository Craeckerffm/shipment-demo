package gs.demo.shipment.application;

import gs.demo.shipment.domain.entity.OutboxEvent;
import gs.demo.shipment.domain.enums.EventStatus;
import gs.demo.shipment.domain.repository.OutboxEventRepository;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.List;

import static io.quarkus.arc.ComponentsProvider.LOG;


@ApplicationScoped
public class OutboxPublisher {

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
                shipmentEmitter.send(event.payload);
                event.processed = true;
                event.status = EventStatus.SUCCESS;
                repository.save(event);

                LOG.infof("Successfully published event %d for aggregate %s",
                        event.id, event.aggregateId);

            } catch (Exception e) {
                LOG.errorf(e, "Failed to publish event %d for aggregate %s",
                        event.id, event.aggregateId);

                event.status = EventStatus.FAILED;
                repository.save(event);
            }
        }
    }

}
