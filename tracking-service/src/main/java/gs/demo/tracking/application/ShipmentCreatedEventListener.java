package gs.demo.tracking.application;

import gs.demo.tracking.domain.entity.ShipmentTrackingStatus;
import gs.demo.tracking.api.dto.ShipmentEventDto;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.eclipse.microprofile.reactive.messaging.Incoming;

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
    public void onShipmentCreated(ConsumerRecord<String, String> record) {

        String messageKey = record.key();
        String jsonPayload = record.value();

        try {
            ShipmentEventDto eventDto = validateMessage.validate(messageKey, jsonPayload);
            ShipmentTrackingStatus shipmentTrackingStatus = new ShipmentTrackingStatus();
            shipmentTrackingStatus.setEventId(eventDto.eventId());
            shipmentTrackingStatus.setTrackingNumber(eventDto.aggregateId());
            shipmentTrackingStatus.setOccurredOn(eventDto.occurredOn());
            shipmentTrackingStatus.setShipmentStatus(eventDto.eventType());

            shipmentTrackingStatus.persistAndFlush();
            LOG.infof("Successfully processed event - EventId: %s", messageKey);

        } catch (org.hibernate.exception.ConstraintViolationException e) {
            LOG.infof("Message with key %s already handled", messageKey);

        } catch (Exception e) {
            LOG.errorf(e, "Error processing event - EventId: %s", messageKey);
            throw new RuntimeException("Failed to process event", e);
        }
    }
}