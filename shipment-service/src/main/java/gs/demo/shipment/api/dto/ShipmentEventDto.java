package gs.demo.shipment.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gs.demo.shipment.domain.enums.ShipmentStatus;

import java.time.Instant;

public record ShipmentEventDto(
        @JsonProperty("status") ShipmentStatus eventType,
        @JsonProperty("trackingNumber") String aggregateId,
        @JsonProperty("occurred_on") Instant occurredOn
) {}
