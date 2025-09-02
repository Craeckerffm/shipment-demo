package gs.demo.shipment.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import gs.demo.shipment.api.dto.ShipmentEventDto;
import gs.demo.shipment.api.mapper.OutboxEventMapper;
import gs.demo.shipment.domain.entity.OutboxEvent;
import gs.demo.shipment.domain.enums.ShipmentStatus;
import gs.demo.shipment.domain.repository.OutboxEventRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Instant;

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
        event.aggregateType = "Shipment";
        event.aggregateId = aggregateId;
        event.eventType = eventType;
        event.occurredOn = Instant.now();

        ShipmentEventDto eventData = outboxEventMapper.toDto(event);

        try {
            event.payload = objectMapper.writeValueAsString(eventData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize event data to JSON", e);
        }

        repository.save(event);

    }
}
