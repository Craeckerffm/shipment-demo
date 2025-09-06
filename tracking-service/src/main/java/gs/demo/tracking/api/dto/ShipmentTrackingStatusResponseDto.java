package gs.demo.tracking.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gs.demo.tracking.domain.enums.ShipmentStatus;

import java.time.Instant;

public record ShipmentTrackingStatusResponseDto(

        @JsonProperty("shipment_status")
        ShipmentStatus shipmentStatus,

        @JsonProperty("occurred_on")
        Instant occurredOn
) {
}
