package elp.max.e.taxistation.service.driver.converter;

import elp.max.e.taxistation.dto.DriverDto;
import elp.max.e.taxistation.model.DriverEntity;

public class Driver2DriverDto {

    public static DriverDto convert(DriverEntity driverEntity) {
        DriverDto driverDto = new DriverDto();
        driverDto.setId(driverEntity.getId());
        driverDto.setName(driverEntity.getName());
        driverDto.setDayoff(driverEntity.getDayoff());
        driverDto.setCar(driverEntity.getCar());
        driverDto.setWorkStatus(driverEntity.isWorkStatus());
        driverDto.setBusy(driverEntity.isBusy());
        return driverDto;
    }
}
