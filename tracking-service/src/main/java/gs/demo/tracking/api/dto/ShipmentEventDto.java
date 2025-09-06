package gs.demo.tracking.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gs.demo.tracking.domain.enums.ShipmentStatus;
import io.smallrye.common.constraint.NotNull;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public record ShipmentEventDto(
        @JsonProperty("event_id")
        @NotNull
        String eventId,

        @JsonProperty("status")
        @NotNull
        ShipmentStatus eventType,

        @JsonProperty("trackingNumber")
        @NotBlank
        String aggregateId,

        @JsonProperty("occurred_on")
        @NotNull
        Instant occurredOn
) {}
