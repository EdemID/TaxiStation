package elp.max.e.taxistation.service.dispatcher.converter;

import elp.max.e.taxistation.dto.DispatcherDto;
import elp.max.e.taxistation.model.DispatcherEntity;

public class DispatcherDto2Dispatcher {

    public static DispatcherEntity convert(DispatcherDto dispatcherDto) {
        DispatcherEntity dispatcherEntity = new DispatcherEntity();
        dispatcherEntity.setId(dispatcherDto.getId());
        dispatcherEntity.setName(dispatcherDto.getName());
        dispatcherEntity.setDayoff(dispatcherDto.getDayoff());
        dispatcherEntity.setStartLunch(dispatcherDto.getStartLunch());
        dispatcherEntity.setEndLunch(dispatcherDto.getEndLunch());
        dispatcherEntity.setWorkStatus(dispatcherDto.isWorkStatus());

        return dispatcherEntity;
    }
}
