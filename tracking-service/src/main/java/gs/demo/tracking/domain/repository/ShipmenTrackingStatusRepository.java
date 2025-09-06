package gs.demo.tracking.domain.repository;

import gs.demo.tracking.domain.entity.ShipmentTrackingStatus;

import java.util.List;

public interface ShipmenTrackingStatusRepository {

    Boolean alreadyHandled(String eventId);

    List<ShipmentTrackingStatus> findTrackingNumber(String trackingNumber);
}
