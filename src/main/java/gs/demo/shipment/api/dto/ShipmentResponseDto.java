package gs.demo.shipment.api.dto;

import gs.demo.shipment.domain.enums.ShipmentStatus;

public record ShipmentResponseDto (String trackingNumber, ShipmentStatus status)
{ }
