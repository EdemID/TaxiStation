package elp.max.e.taxistation.service.driverService;

import elp.max.e.taxistation.dto.DriverDto;
import elp.max.e.taxistation.model.DriverEntity;

public class DriverConverter {

    public static DriverDto fromDriverEntityToDriverDto(DriverEntity driverEntity) {
        DriverDto driverDto = new DriverDto();
        driverDto.setId(driverEntity.getId());
        driverDto.setName(driverEntity.getName());
        driverDto.setDayoff(driverEntity.getDayoff());
        driverDto.setCar(driverEntity.getCar());
        driverDto.setWorkStatus(driverEntity.isWorkStatus());
        driverDto.setBusy(driverEntity.isBusy());
        return driverDto;
    }

    public static DriverEntity fromDriverDtoToDriverEntity(DriverDto driverDto) {
        DriverEntity driverEntity = new DriverEntity();
        driverEntity.setId(driverDto.getId());
        driverEntity.setName(driverDto.getName());
        driverEntity.setDayoff(driverDto.getDayoff());
        driverEntity.setCar(driverDto.getCar());
        driverEntity.setWorkStatus(driverDto.isWorkStatus());
        driverEntity.setBusy(driverDto.isBusy());
        return driverEntity;
    }
}
