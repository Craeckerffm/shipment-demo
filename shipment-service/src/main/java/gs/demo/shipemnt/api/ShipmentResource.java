package gs.demo.shipemnt.api;

import gs.demo.shipemnt.application.ShipmentResourceHandler;
import gs.demo.shipemnt.api.dto.CreateShipmentDto;
import gs.demo.shipemnt.api.dto.ShipmentResponseDto;
import gs.demo.shipemnt.application.exception.ParcelAlreadyHandledException;
import gs.demo.shipemnt.application.exception.TrackingNumberGenerationException;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.validation.Valid;

import java.net.URI;

@Path("/create")
public class ShipmentResource {

    @Inject
    ShipmentResourceHandler service;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Valid CreateShipmentDto dto, @Context UriInfo uriInfo) throws TrackingNumberGenerationException, ParcelAlreadyHandledException {

        ShipmentResponseDto created = service.create(dto);

        URI location = uriInfo.getAbsolutePathBuilder()
                .path(created.trackingNumber())
                .build();
        return Response.created(location).entity(created).build();
    }
}
