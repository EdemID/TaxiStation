package elp.max.e.taxistation.service.carService;

import elp.max.e.taxistation.dto.CarDto;
import elp.max.e.taxistation.model.CarEntity;

public class CarConverter {

    public static CarEntity fromCarDtoToCarEntity(CarDto carDto) {
        CarEntity carEntity = new CarEntity();
        carEntity.setId(carDto.getId());
        carEntity.setNumberCar(carDto.getNumberCar());
        carEntity.setResource(carDto.getResource());
        carEntity.setBusy(carDto.isBusy());
        return carEntity;
    }

    public static CarDto fromCarEntityToCarDto(CarEntity carEntity) {
        CarDto carDto = new CarDto();
        carDto.setId(carEntity.getId());
        carDto.setNumberCar(carEntity.getNumberCar());
        carDto.setResource(carEntity.getResource());
        carDto.setBusy(carEntity.isBusy());
        return carDto;
    }
}
