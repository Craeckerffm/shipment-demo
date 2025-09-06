package gs.demo.shipemnt.domain.repository;

import gs.demo.shipemnt.domain.entity.Shipment;

public interface ShipmentRepository {

    Boolean isUnique(String parcelId);

    Shipment save(Shipment event);
}
