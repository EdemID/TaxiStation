package elp.max.e.taxistation.service.car.converter;

import elp.max.e.taxistation.dto.CarDto;
import elp.max.e.taxistation.model.CarEntity;

public class CarDto2Car {

    public static CarEntity convert(CarDto carDto) {
        CarEntity carEntity = new CarEntity();
        carEntity.setId(carDto.getId());
        carEntity.setNumberCar(carDto.getNumberCar());
        carEntity.setResource(carDto.getResource());
        carEntity.setBusy(carDto.isBusy());
        return carEntity;
    }
}
