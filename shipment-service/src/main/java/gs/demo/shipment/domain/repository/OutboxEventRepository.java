package gs.demo.shipment.domain.repository;

import gs.demo.shipment.domain.entity.OutboxEvent;

import java.util.List;


public interface OutboxEventRepository {
    List<OutboxEvent> findAllPending();

    OutboxEvent save(OutboxEvent event);
}
