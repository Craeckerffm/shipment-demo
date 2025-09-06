package gs.demo.shipemnt.api.dto;

public record ErrorResponseDto(
        int status,
        String error,
        String message,
        String timestamp
) {}