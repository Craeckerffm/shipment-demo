package gs.demo.shipment.api.exception;

import gs.demo.shipment.api.dto.ErrorResponseDto;
import gs.demo.shipment.application.exception.TrackingNumberGenerationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.Instant;

@Provider
public class TrackingNumberGenerationExceptionMapper implements ExceptionMapper<TrackingNumberGenerationException> {

    @Override
    public Response toResponse(TrackingNumberGenerationException exception) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                500,
                "TRACKING_NUMBER_GENERATION_FAILED",
                exception.getMessage(),
                Instant.now().toString()
        );

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .build();
    }

}
