package gs.demo.shipemnt.domain.repository;

import gs.demo.shipemnt.domain.entity.OutboxEvent;

import java.util.List;


public interface OutboxEventRepository {
    List<OutboxEvent> findAllPending();

    OutboxEvent save(OutboxEvent event);
}
