package gs.demo.shipemnt.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import gs.demo.shipemnt.api.mapper.ShipmentMapper;
import gs.demo.shipemnt.api.dto.CreateShipmentDto;
import gs.demo.shipemnt.api.dto.ShipmentResponseDto;
import gs.demo.shipemnt.application.exception.ParcelAlreadyHandledException;
import gs.demo.shipemnt.application.exception.TrackingNumberGenerationException;
import gs.demo.shipemnt.domain.entity.Shipment;
import gs.demo.shipemnt.domain.enums.ShipmentStatus;
import gs.demo.shipemnt.domain.repository.ShipmentRepository;
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
        s.setTrackingNumber(trackingNumberService.generateTrackingNumber());
        shipmentRepository.save(s);

        outboxEventService.createShipmentEvent(s.getTrackingNumber(), ShipmentStatus.CREATED);

        return new ShipmentResponseDto(s.getTrackingNumber());
    }
}
