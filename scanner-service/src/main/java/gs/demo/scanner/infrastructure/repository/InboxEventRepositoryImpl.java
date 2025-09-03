package gs.demo.scanner.infrastructure.repository;

import gs.demo.scanner.domain.entity.InboxEvent;
import gs.demo.scanner.domain.repository.InboxEventRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InboxEventRepositoryImpl implements InboxEventRepository, PanacheRepositoryBase<InboxEvent, Long> {
    @Override
    public Boolean alreadyHandled(String eventId) {
        return count("eventId", eventId) == 1;
    }
}
