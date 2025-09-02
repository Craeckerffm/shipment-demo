package gs.demo.shipment.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import gs.demo.shipment.api.mapper.ShipmentMapper;
import gs.demo.shipment.api.dto.CreateShipmentDto;
import gs.demo.shipment.api.dto.ShipmentResponseDto;
import gs.demo.shipment.application.exception.ParcelAlreadyHandledException;
import gs.demo.shipment.application.exception.TrackingNumberGenerationException;
import gs.demo.shipment.domain.entity.Shipment;
import gs.demo.shipment.domain.enums.ShipmentStatus;
import gs.demo.shipment.domain.repository.ShipmentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ShipmentResourceHandler {

    @Inject
    ShipmentMapper mapper;

    @Inject
    TrackingNumberService trackingNumberService;

    @Inject
    ShipmentRepository shipmentRepository;

    @Inject
    OutboxEventService outboxEventService;

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

        outboxEventService.createShipmentEvent(s.trackingNumber, ShipmentStatus.CREATED);

        return new ShipmentResponseDto(s.trackingNumber);
    }
}
