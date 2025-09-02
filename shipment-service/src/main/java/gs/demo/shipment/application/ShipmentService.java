package gs.demo.shipment.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import gs.demo.shipment.api.dto.ShipmentEventDto;
import gs.demo.shipment.api.mapper.ShipmentMapper;
import gs.demo.shipment.api.dto.CreateShipmentDto;
import gs.demo.shipment.api.dto.ShipmentResponseDto;
import gs.demo.shipment.application.exception.ParcelAlreadyHandledException;
import gs.demo.shipment.application.exception.TrackingNumberGenerationException;
import gs.demo.shipment.domain.entity.OutboxEvent;
import gs.demo.shipment.domain.entity.Shipment;
import gs.demo.shipment.domain.enums.ShipmentStatus;
import gs.demo.shipment.domain.repository.ShipmentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.Instant;

@ApplicationScoped
public class ShipmentService {

    @Inject
    ShipmentMapper mapper;

    @Inject
    TrackingNumberService trackingNumberService;

    @Inject
    ShipmentRepository shipmentRepository;

    @Inject
    ObjectMapper objectMapper;

    @Transactional
    public ShipmentResponseDto create(CreateShipmentDto dto) throws TrackingNumberGenerationException, ParcelAlreadyHandledException {
        if (shipmentRepository.isUnique(dto.parcelId()) == false) {
            throw new ParcelAlreadyHandledException("ParcelId already exists");
        }

        Shipment s = mapper.fromCreate(dto);
        s.trackingNumber = trackingNumberService.generateTrackingNumber();
        shipmentRepository.save(s);

        OutboxEvent o = new OutboxEvent();
        o.aggregateType = "Shipment";
        o.aggregateId = s.trackingNumber;
        o.eventType = ShipmentStatus.CREATED;
        o.occurredOn = Instant.now();

        ShipmentEventDto eventData = new ShipmentEventDto(
                o.aggregateType,
                o.aggregateId,
                o.occurredOn
        );

        try {
            o.payload = objectMapper.writeValueAsString(eventData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize event data to JSON", e);
        }

        o.persistAndFlush();

        return new ShipmentResponseDto(s.trackingNumber);
    }
}
