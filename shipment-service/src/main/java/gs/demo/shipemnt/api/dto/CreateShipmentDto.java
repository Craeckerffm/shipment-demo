package gs.demo.shipemnt.api.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CreateShipmentDto(
        @NotBlank @Length(min = 1, max = 50) String parcelId,
        @NotBlank @Length(min = 1, max = 50) String senderId,
        @NotBlank @Length(min = 1, max = 50) String recipientId
) {
}

