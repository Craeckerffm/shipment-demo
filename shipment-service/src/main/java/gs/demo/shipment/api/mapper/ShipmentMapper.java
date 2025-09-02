package gs.demo.shipment.api.mapper;

import gs.demo.shipment.api.dto.CreateShipmentDto;
import gs.demo.shipment.domain.entity.Shipment;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ShipmentMapper {

    CreateShipmentDto toDto(Shipment entity);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "trackingNumber", ignore = true)
    Shipment fromCreate(CreateShipmentDto dto);


}