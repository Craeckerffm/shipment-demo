package gs.demo.tracking.application;

import gs.demo.tracking.domain.entity.ShipmentTrackingStatus;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static io.quarkus.arc.ComponentsProvider.LOG;

@ApplicationScoped
@Blocking
public class ShipmentCreatedEventListener {

    @Inject
    ValidateMessage validateMessage;

    @Incoming("shipment-created")
    @Incoming("shipment-updated")
    @Blocking
    @Transactional
    public CompletionStage<Void> onShipmentCreated(ConsumerRecord<String, String> record) {


        return validateMessage.validate(record.key(), record.value()).thenCompose(eventDto -> {

            ShipmentTrackingStatus shipmentTrackingStatus = new ShipmentTrackingStatus();
            shipmentTrackingStatus.eventId = eventDto.eventId();
            shipmentTrackingStatus.trackingNumber = eventDto.aggregateId();
            shipmentTrackingStatus.occurredOn = eventDto.occurredOn();
            shipmentTrackingStatus.shipmentStatus = eventDto.eventType();

            try {
                shipmentTrackingStatus.persistAndFlush();
                LOG.infof("Successfully processed event - EventId: %s", record.key());
                return CompletableFuture.completedFuture(null);

            } catch (Exception e) {
                if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                    LOG.infof("Message with key %s already handled", record.key());
                    return CompletableFuture.completedFuture(null);
                } else {
                    LOG.errorf(e, "Error processing event - EventId: %s", record.key());
                    return CompletableFuture.failedFuture(e);
                }
            }
        });
    }
}
