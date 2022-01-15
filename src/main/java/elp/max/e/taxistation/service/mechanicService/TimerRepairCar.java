package elp.max.e.taxistation.service.mechanicService;

import elp.max.e.taxistation.dto.MechanicDto;
import elp.max.e.taxistation.exception.ValidationDtoException;
import elp.max.e.taxistation.model.CarEntity;
import elp.max.e.taxistation.model.MechanicEntity;
import elp.max.e.taxistation.service.carService.CarConverter;
import elp.max.e.taxistation.service.carService.CarServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.TimerTask;

public class TimerRepairCar extends TimerTask {

    private static Logger logger = LoggerFactory.getLogger(TimerRepairCar.class);

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
        logger.info("Задача таск запущена: {}", new Date());
        try {
            // как избавиться от механика?
            carEntity.setMechanicEntity(null);
            carEntity.setBusy(false);
            logger.info("Car resource before repair: {}", carEntity.getResource());
            carEntity.setResource(mechanicEntity.getResource());
            logger.info("Car resource after repair: {}", carEntity.getResource());
            carService.update(carEntity.getId(), CarConverter.fromCarEntityToCarDto(carEntity));

            mechanicDto.setBusy(false);
            mechanicService.update(mechanicDto.getId(), mechanicDto);
            logger.info("Mechanic freed");
        } catch (ValidationDtoException e) {
            e.printStackTrace();
        }
    }
}
