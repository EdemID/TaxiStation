package elp.max.e.taxistation.service.car.converter;

import elp.max.e.taxistation.dto.CarDto;
import elp.max.e.taxistation.model.CarEntity;

public class Car2CarDto {

    public static CarDto convert(CarEntity carEntity) {
        CarDto carDto = new CarDto();
        carDto.setId(carEntity.getId());
        carDto.setNumberCar(carEntity.getNumberCar());
        carDto.setResource(carEntity.getResource());
        carDto.setBusy(carEntity.isBusy());
        return carDto;
    }
}
