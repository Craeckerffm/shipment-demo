package gs.demo.shipemnt.infrastructure.repository;

import gs.demo.shipemnt.domain.entity.OutboxEvent;
import gs.demo.shipemnt.domain.enums.EventStatus;
import gs.demo.shipemnt.domain.repository.OutboxEventRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class OutboxEventRepositoryImp implements OutboxEventRepository, PanacheRepositoryBase<OutboxEvent, Long> {


    @Override
    @Transactional
    public List<OutboxEvent> findAllPending() {

        List<OutboxEvent> events = this.getEntityManager().createQuery(
                        "SELECT e FROM OutboxEvent e WHERE e.status = :status", OutboxEvent.class)
                .setParameter("status", EventStatus.PENDING)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .getResultList();

        if (!events.isEmpty()) {
            List<Long> eventIds = events.stream().map(OutboxEvent::getId).toList();
            this.getEntityManager().createQuery(
                            "UPDATE OutboxEvent e SET e.status = :status WHERE e.id IN :eventIds")
                    .setParameter("status", EventStatus.IN_PROCESS)
                    .setParameter("eventIds", eventIds)
                    .executeUpdate();

            events.forEach(event -> event.setStatus(EventStatus.IN_PROCESS));
        }

        return events;
    }

    @Override
    @Transactional
    public OutboxEvent save(OutboxEvent event) {
        if (event.getId() != null) {
            OutboxEvent merged = getEntityManager().merge(event);
            flush();
            return merged;
        } else {
            persist(event);
            flush();
            return event;
        }
    }

}
