package elp.max.e.taxistation.service.mechanicService;

import elp.max.e.taxistation.dto.MechanicDto;
import elp.max.e.taxistation.exception.ValidationDtoException;
import elp.max.e.taxistation.model.CarEntity;
import elp.max.e.taxistation.model.MechanicEntity;
import elp.max.e.taxistation.service.carService.CarConverter;
import elp.max.e.taxistation.service.carService.CarServiceImpl;

import java.util.Date;
import java.util.TimerTask;

public class TimerRepairCar extends TimerTask {

    private CarEntity carEntity;
    private CarServiceImpl carService;
    private MechanicEntity mechanicEntity;
    private MechanicDto mechanicDto;
    private MechanicServiceImpl mechanicService;

    public TimerRepairCar(CarEntity carEntity, CarServiceImpl carService, MechanicEntity mechanicEntity, MechanicDto mechanicDto, MechanicServiceImpl mechanicService) {
        this.carEntity = carEntity;
        this.carService = carService;
        this.mechanicEntity = mechanicEntity;
        this.mechanicDto = mechanicDto;
        this.mechanicService = mechanicService;
    }

    @Override
    public void run() {

        System.out.println("Задача таск запущена: " + new Date());
        try {
            // как избавиться от механика?
            carEntity.setMechanicEntity(null);
            carEntity.setBusy(false);
            System.out.println("Автомобиль отремантирован, был: " + carEntity.getResource());
            carEntity.setResource(mechanicEntity.getResource());
            System.out.println("Автомобиль отремантирован, стал: " + carEntity.getResource());
            carService.update(carEntity.getId(), CarConverter.fromCarEntityToCarDto(carEntity));

            mechanicDto.setBusy(false);
            mechanicService.update(mechanicDto.getId(), mechanicDto);
            System.out.println("Механик освободился");
        } catch (ValidationDtoException e) {
            e.printStackTrace();
        }
    }
}
