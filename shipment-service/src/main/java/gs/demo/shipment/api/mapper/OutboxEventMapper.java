package gs.demo.shipment.api.mapper;

import gs.demo.shipment.api.dto.ShipmentEventDto;
import gs.demo.shipment.domain.entity.OutboxEvent;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;


@Mapper(componentModel = "cdi",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface OutboxEventMapper {

    ShipmentEventDto toDto(OutboxEvent event);

}