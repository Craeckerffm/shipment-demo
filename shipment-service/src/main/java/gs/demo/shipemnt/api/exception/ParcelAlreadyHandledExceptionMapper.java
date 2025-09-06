package gs.demo.shipemnt.api.exception;

import gs.demo.shipemnt.api.dto.ErrorResponseDto;
import gs.demo.shipemnt.application.exception.ParcelAlreadyHandledException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.Instant;

@Provider
public class ParcelAlreadyHandledExceptionMapper implements ExceptionMapper<ParcelAlreadyHandledException> {

    @Override
    public Response toResponse(ParcelAlreadyHandledException e) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                Response.Status.CONFLICT.getStatusCode(),
                "TRACKING_NUMBER_GENERATION_FAILED",
                e.getMessage(),
                Instant.now().toString()
        );

        return Response.status(errorResponse.status())
                .entity(errorResponse)
                .build();
    }


}
