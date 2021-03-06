package elp.max.e.taxistation.service.mechanic;

import elp.max.e.taxistation.dto.MechanicDto;
import elp.max.e.taxistation.exception.EntityNotFoundException;
import elp.max.e.taxistation.exception.ValidationDtoException;
import elp.max.e.taxistation.model.CarEntity;
import elp.max.e.taxistation.model.MechanicEntity;
import elp.max.e.taxistation.repository.MechanicRepository;
import elp.max.e.taxistation.service.ServiceInterface;
import elp.max.e.taxistation.service.car.CarServiceImpl;
import elp.max.e.taxistation.service.car.converter.Car2CarDto;
import elp.max.e.taxistation.service.mechanic.converter.Mechanic2MechanicDto;
import elp.max.e.taxistation.service.mechanic.converter.MechanicDto2Mechanic;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class MechanicServiceImpl implements ServiceInterface<MechanicDto> {

    private static final Logger logger = LoggerFactory.getLogger(MechanicServiceImpl.class);

    private final MechanicRepository mechanicRepository;

    @Override
    @Transactional
    public MechanicDto findById(Long id) {
        MechanicEntity mechanicEntity = null;
        if (mechanicRepository.findById(id).isPresent()) {
            mechanicEntity = mechanicRepository.findById(id).get();
        }
        return Mechanic2MechanicDto.convert(mechanicEntity);
    }

    @Transactional
    @Override
    public List<MechanicDto> findAll() {
        return null;
    }

    @Transactional
    @Override
    public MechanicDto save(MechanicDto dto) throws ValidationDtoException {
        return null;
    }

    @Override
    @Transactional
    public MechanicDto update(Long id, MechanicDto dto) throws ValidationDtoException {
        validateDto(dto);
        MechanicEntity mechanicEntity = mechanicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("?????????????? " + dto + " ???? ????????????"));

        mechanicEntity.setRepairTime(dto.getRepairTime());
        mechanicEntity.setResource(dto.getResource());
        mechanicEntity.setBusy(dto.isBusy());
        mechanicEntity = mechanicRepository.save(mechanicEntity);

        return Mechanic2MechanicDto.convert(mechanicEntity);

    }

    @Transactional
    @Override
    public void delete(Long id) {

    }

    @Override
    public void validateDto(MechanicDto dto) throws ValidationDtoException {
        if (isNull(dto)) {
            throw new ValidationDtoException("Mechanic is null");
        }
        if (isNull(dto.getId())) {
            throw new ValidationDtoException("Mechanic id is null");
        }
    }

    public long repairCar(MechanicDto mechanicDto, CarEntity carEntity, CarServiceImpl carService) throws ValidationDtoException {
        MechanicEntity mechanicEntity = MechanicDto2Mechanic.convert(mechanicDto);

        logger.info("?????????? repairCar ??????????????: {}", new Date());
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Callable<TimerRepairCar> callable = new CallableClass(carEntity, carService, mechanicEntity, mechanicDto, this);
        Future<TimerRepairCar> future = executor.submit(callable);

        // ?????????????? ?? ?????????????? ???????????????????? ????????????????
        TimerRepairCar s;
        try {
            s = future.get();
            logger.info("?????????????????? ???????????????????? ???????? Callable: {}", s.scheduledExecutionTime());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // ?????????????????????????? ?????? ??????????????
        executor.shutdown();

        logger.info("Car with number car={} under repair", carEntity.getNumberCar());
        return System.currentTimeMillis();
    }

    @AllArgsConstructor
    private static class CallableClass implements Callable<TimerRepairCar> {

        private static Logger logger = LoggerFactory.getLogger(CallableClass.class);

        private CarEntity carEntity;
        private CarServiceImpl carService;
        private MechanicEntity mechanicEntity;
        private MechanicDto mechanicDto;
        private MechanicServiceImpl mechanicService;

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
            Timer timer = new Timer("?????????? ??????????????");
            timer.schedule(task, repairTime);
            logger.info("Start timer \"?????????? ??????????????\" in: {} mc", repairTime);
            logger.info("???????????? ??????????????: {}", new Date());
            return task;
        }
    }

    @AllArgsConstructor
    private static class TimerRepairCar extends TimerTask {

        private static Logger logger = LoggerFactory.getLogger(TimerRepairCar.class);

        private CarEntity carEntity;
        private CarServiceImpl carService;
        private MechanicEntity mechanicEntity;
        private MechanicDto mechanicDto;
        private MechanicServiceImpl mechanicService;

        @Override
        public void run() {
            logger.info("???????????? \"?????????? ??????????????\" ????????????????: {}", new Date());
            try {
                // ?????? ???????????????????? ???? ?????????????????
                carEntity.setMechanicEntity(null);
                carEntity.setBusy(false);
                logger.info("Car resource before repair: {}", carEntity.getResource());
                carEntity.setResource(mechanicEntity.getResource());
                logger.info("Car resource after repair: {}", carEntity.getResource());
                carService.update(carEntity.getId(), Car2CarDto.convert(carEntity));

                mechanicDto.setBusy(false);
                mechanicService.update(mechanicDto.getId(), mechanicDto);
                logger.info("Mechanic freed");
                logger.info("?????????? ??????????????: {}", new Date());
            } catch (ValidationDtoException e) {
                e.printStackTrace();
            }
        }
    }
}
