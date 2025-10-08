package gs.demo.tracking.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import gs.demo.tracking.api.dto.ShipmentEventDto;
import gs.demo.tracking.domain.repository.ShipmenTrackingStatusRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Validator;

import static io.quarkus.arc.ComponentsProvider.LOG;


@ApplicationScoped
public class ValidateMessage {
    @Inject
    Validator validator;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    ShipmenTrackingStatusRepository shipmenTrackingStatusRepository;

    public ShipmentEventDto validate(String messageKey, String jsonPayload) {


        if (messageKey == null || messageKey.isEmpty()) {
            throw new IllegalArgumentException("Message key is required");
        }

        ShipmentEventDto eventDto;
        try {
            eventDto = objectMapper.readValue(jsonPayload, ShipmentEventDto.class);
        } catch (Exception e) {
            LOG.errorf("Failed to deserialize message payload: %s", e.getMessage());
            throw new IllegalArgumentException("Invalid JSON payload", e);
        }

        var violations = validator.validate(eventDto);
        if (!violations.isEmpty()) {
            LOG.errorf("Validation failed for shipment event: %s", violations);
            throw new IllegalArgumentException("Invalid event data: " + violations);
        }

        LOG.infof("Received shipment event - MessageKey: %s, TrackingNumber: %s, Status: %s",
                messageKey, eventDto.aggregateId(), eventDto.eventType());

        if (shipmenTrackingStatusRepository.alreadyHandled(messageKey)) {
            LOG.infof("Message with key %s already handled", messageKey);
            throw new IllegalArgumentException("Message already processed");
        }

        return eventDto;
    }
}