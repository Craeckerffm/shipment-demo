package gs.demo.shipment.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record ShipmentEventDto(
        @JsonProperty("aggregate_type") String aggregateType,
        @JsonProperty("aggregate_id") String aggregateId,
        @JsonProperty("occurred_on") Instant occurredOn
) {}
