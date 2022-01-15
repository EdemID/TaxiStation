package elp.max.e.taxistation.service.dispatcher;

import elp.max.e.taxistation.dto.DispatcherDto;
import elp.max.e.taxistation.model.DispatcherEntity;
import org.springframework.stereotype.Component;

@Component
public class Dispatcher2DispatcherDto {

    public static DispatcherDto convert(DispatcherEntity dispatcherEntity) {
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
