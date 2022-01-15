package elp.max.e.taxistation.service.mechanicService;

import elp.max.e.taxistation.dto.MechanicDto;
import elp.max.e.taxistation.model.CarEntity;
import elp.max.e.taxistation.model.MechanicEntity;
import elp.max.e.taxistation.service.carService.CarServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.concurrent.Callable;

public class CallableClass implements Callable<TimerRepairCar> {

    private static Logger logger = LoggerFactory.getLogger(CallableClass.class);

    private CarEntity carEntity;
    private CarServiceImpl carService;
    private MechanicEntity mechanicEntity;
    private MechanicDto mechanicDto;
    private MechanicServiceImpl mechanicService;

    public CallableClass(CarEntity carEntity, CarServiceImpl carService, MechanicEntity mechanicEntity, MechanicDto mechanicDto, MechanicServiceImpl mechanicService) {
        this.carEntity = carEntity;
        this.carService = carService;
        this.mechanicEntity = mechanicEntity;
        this.mechanicDto = mechanicDto;
        this.mechanicService = mechanicService;
    }

    @Override
    public TimerRepairCar call() {

        long repairTime = mechanicEntity.getRepairTime();

        long repairCompletionTime = System.currentTimeMillis() + repairTime;
        while (repairCompletionTime > System.currentTimeMillis()) {
            if (!mechanicService.findById(mechanicDto.getId()).isBusy()) {
                logger.info("Mechanic free");
                break;
            }
        }

        mechanicDto.setBusy(true);
        mechanicService.update(mechanicDto.getId(), mechanicDto);
        logger.info("Mechanic busy");

        TimerRepairCar task = new TimerRepairCar(carEntity, carService, mechanicEntity, mechanicDto, mechanicService);
        Timer timer = new Timer("Время ремонта");
        timer.schedule(task, repairTime);
        logger.info("Start timer \"Время ремонта\" in: {} mc", repairTime);
        return task;
    }
}
