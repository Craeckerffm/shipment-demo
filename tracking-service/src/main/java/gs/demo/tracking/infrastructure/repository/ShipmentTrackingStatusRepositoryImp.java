package gs.demo.tracking.infrastructure.repository;

import gs.demo.tracking.domain.entity.ShipmentTrackingStatus;
import gs.demo.tracking.domain.repository.ShipmenTrackingStatusRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ShipmentTrackingStatusRepositoryImp implements ShipmenTrackingStatusRepository, PanacheRepositoryBase<ShipmentTrackingStatus, Long> {


    @Override
    public Boolean alreadyHandled(String eventId) {
        return count("eventId", eventId) == 1;
    }

    @Override
    public List<ShipmentTrackingStatus> findTrackingNumber(String trackingNumber) {
        return find("trackingNumber", Sort.by("createdAt").descending(), trackingNumber).list();

    }
}
