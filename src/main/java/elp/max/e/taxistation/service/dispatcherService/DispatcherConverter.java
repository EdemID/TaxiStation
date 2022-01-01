package elp.max.e.taxistation.service.dispatcherService;

import elp.max.e.taxistation.dto.DispatcherDto;
import elp.max.e.taxistation.model.DispatcherEntity;
import org.springframework.stereotype.Component;

@Component
public class DispatcherConverter {

    public static DispatcherEntity fromDispatcherDtoToDispatcherEntity(DispatcherDto dispatcherDto) {
        DispatcherEntity dispatcherEntity = new DispatcherEntity();
        dispatcherEntity.setId(dispatcherDto.getId());
        dispatcherEntity.setName(dispatcherDto.getName());
        dispatcherEntity.setDayoff(dispatcherDto.getDayoff());
        dispatcherEntity.setStartLunch(dispatcherDto.getStartLunch());
        dispatcherEntity.setEndLunch(dispatcherDto.getEndLunch());
        dispatcherEntity.setWorkStatus(dispatcherDto.isWorkStatus());

        return dispatcherEntity;
    }

    public static DispatcherDto fromDispatcherEntityToDispatcherDto(DispatcherEntity dispatcherEntity) {
        DispatcherDto dispatcherDto = new DispatcherDto();
        dispatcherDto.setName(dispatcherEntity.getName());
        dispatcherDto.setId(dispatcherEntity.getId());
        dispatcherDto.setDayoff(dispatcherEntity.getDayoff());
        dispatcherDto.setStartLunch(dispatcherEntity.getStartLunch());
        dispatcherDto.setEndLunch(dispatcherEntity.getEndLunch());
        dispatcherDto.setWorkStatus(dispatcherEntity.isWorkStatus());
        return dispatcherDto;
    }
}
