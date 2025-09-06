package gs.demo.tracking.api.mapper;

import gs.demo.tracking.api.dto.ShipmentTrackingStatusResponseDto;
import gs.demo.tracking.domain.entity.ShipmentTrackingStatus;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "cdi",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ShipmentTrackingStatusMapper {

    @Mapping(target = "shipmentStatus", source = "shipmentStatus")
    @Mapping(target = "occurredOn", source = "occurredOn")
    ShipmentTrackingStatusResponseDto toDto(ShipmentTrackingStatus entity);

    List<ShipmentTrackingStatusResponseDto> toDtoList(List<ShipmentTrackingStatus> entities);
}

