package gs.demo.tracking.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import gs.demo.tracking.api.dto.ShipmentEventDto;
import gs.demo.tracking.domain.repository.ShipmenTrackingStatusRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Validator;

import java.util.concurrent.CompletableFuture;

import static io.quarkus.arc.ComponentsProvider.LOG;


@ApplicationScoped
public class ValidateMessage {
    @Inject
    Validator validator;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    ShipmenTrackingStatusRepository shipmenTrackingStatusRepository;

    public CompletableFuture<ShipmentEventDto> validate(String messageKey, String jsonPayload) {
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

        if (shipmenTrackingStatusRepository.alreadyHandled(messageKey)) {
            LOG.infof("Message with key %s already handled", messageKey);
            return CompletableFuture.failedFuture(new IllegalArgumentException("Message already processed"));
        }

        return CompletableFuture.completedFuture(eventDto);
    }
}
