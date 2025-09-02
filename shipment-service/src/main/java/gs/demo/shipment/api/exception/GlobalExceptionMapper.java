package gs.demo.shipment.api.exception;

import gs.demo.shipment.api.dto.ErrorResponseDto;
import io.quarkus.arc.profile.IfBuildProfile;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.Instant;

@Provider
@IfBuildProfile("dev")
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                500,
                "INTERNAL_SERVER_ERROR",
                exception.getMessage(),
                Instant.now().toString()
        );

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .build();
    }
}