package gs.demo.shipemnt.infrastructure.repository;

import gs.demo.shipemnt.domain.entity.Shipment;
import gs.demo.shipemnt.domain.repository.ShipmentRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ShipmentRepositoryImp implements ShipmentRepository, PanacheRepositoryBase<Shipment, Long> {


    @Override
    public Boolean isUnique(String parcelId) {

        return count("parcelId", parcelId) == 0;
    }

    @Override
    public Shipment save(Shipment shipment) {
        persist(shipment);
        flush();
        return shipment;
    }
}
