package gs.demo.shipemnt.infrastructure.repository;

import gs.demo.shipemnt.domain.entity.OutboxEvent;
import gs.demo.shipemnt.domain.enums.EventStatus;
import gs.demo.shipemnt.domain.repository.OutboxEventRepository;
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
