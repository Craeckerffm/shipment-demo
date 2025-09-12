package gs.demo.shipemnt.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import gs.demo.shipemnt.api.dto.ShipmentEventDto;
import gs.demo.shipemnt.api.mapper.OutboxEventMapper;
import gs.demo.shipemnt.domain.entity.OutboxEvent;
import gs.demo.shipemnt.domain.enums.ShipmentStatus;
import gs.demo.shipemnt.domain.repository.OutboxEventRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Instant;
import java.util.UUID;

@ApplicationScoped
public class OutboxEventService {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    OutboxEventRepository repository;

    @Inject
    OutboxEventMapper outboxEventMapper;

    public void createShipmentEvent(String aggregateId, ShipmentStatus eventType) {
        OutboxEvent event = new OutboxEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setAggregateType("Shipment");
        event.setAggregateId(aggregateId);
        event.setEventType(eventType);
        event.setOccurredOn(Instant.now());

        ShipmentEventDto eventData = outboxEventMapper.toDto(event);

        try {
            event.setPayload(objectMapper.writeValueAsString(eventData));
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize event data to JSON", e);
        }

        repository.save(event);

    }
}
