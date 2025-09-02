package gs.demo.shipment.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateShipmentDto(
        @NotBlank String parcelId,
        @NotBlank String senderId,
        @NotBlank String recipientId
) {
}

