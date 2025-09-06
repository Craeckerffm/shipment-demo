package gs.demo.shipemnt.api.mapper;

import gs.demo.shipemnt.api.dto.ShipmentEventDto;
import gs.demo.shipemnt.domain.entity.OutboxEvent;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;


@Mapper(componentModel = "cdi",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface OutboxEventMapper {

    ShipmentEventDto toDto(OutboxEvent event);

}