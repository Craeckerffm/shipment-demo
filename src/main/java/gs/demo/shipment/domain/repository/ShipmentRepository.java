package gs.demo.shipment.domain.repository;

import gs.demo.shipment.domain.entity.Shipment;

public interface ShipmentRepository {

    Boolean isUnique(String parcelId);

    public Shipment save(Shipment event);
}
