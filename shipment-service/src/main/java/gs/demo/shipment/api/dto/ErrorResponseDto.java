package gs.demo.shipment.api.dto;

public record ErrorResponseDto(
        int status,
        String error,
        String message,
        String timestamp
) {}