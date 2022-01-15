package elp.max.e.taxistation.service.driver.converter;

import elp.max.e.taxistation.dto.DriverDto;
import elp.max.e.taxistation.model.DriverEntity;

public class DriverDto2Driver {

    public static DriverEntity convert(DriverDto driverDto) {
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
