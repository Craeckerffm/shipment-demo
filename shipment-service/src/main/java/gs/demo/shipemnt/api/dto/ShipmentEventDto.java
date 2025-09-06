package gs.demo.shipemnt.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gs.demo.shipemnt.domain.enums.ShipmentStatus;

import java.time.Instant;

public record ShipmentEventDto(
        @JsonProperty("event_id") String eventId,
        @JsonProperty("status") ShipmentStatus eventType,
        @JsonProperty("trackingNumber") String aggregateId,
        @JsonProperty("occurred_on") Instant occurredOn
) {}
