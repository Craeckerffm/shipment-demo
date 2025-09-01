package gs.demo.shipment.infrastructure.repository;

import gs.demo.shipment.domain.entity.OutboxEvent;
import gs.demo.shipment.domain.enums.EventStatus;
import gs.demo.shipment.domain.repository.OutboxEventRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
@ApplicationScoped
public class OutboxEventRepositoryImp implements OutboxEventRepository, PanacheRepositoryBase<OutboxEvent, Long> {
    @Override
    public List<OutboxEvent> findAllPending() {
        return find("status", EventStatus.PENDING).list();
    }

    @Override
    public OutboxEvent save(OutboxEvent event) {
       persist(event);
       flush();
       return event;
    }
}
