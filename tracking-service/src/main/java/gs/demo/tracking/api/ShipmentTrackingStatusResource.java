package gs.demo.tracking.api;

import gs.demo.tracking.api.mapper.ShipmentTrackingStatusMapper;
import gs.demo.tracking.domain.entity.ShipmentTrackingStatus;
import gs.demo.tracking.domain.repository.ShipmenTrackingStatusRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;


@Path("/tracking")
public class ShipmentTrackingStatusResource {
    @Inject
    ShipmenTrackingStatusRepository shipmenTrackingStatusRepository;

    @Inject
    ShipmentTrackingStatusMapper mapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{trackingNumber}")
    public Response getForTrackingNumber(@PathParam("trackingNumber") String trackingNumber) {

        List<ShipmentTrackingStatus> trackingStatuses = shipmenTrackingStatusRepository.findTrackingNumber(trackingNumber);

        if (trackingStatuses.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(mapper.toDtoList(trackingStatuses)).build();

    }
}
