package elp.max.e.taxistation.service.mechanicService;

import elp.max.e.taxistation.dto.MechanicDto;
import elp.max.e.taxistation.model.CarEntity;
import elp.max.e.taxistation.model.MechanicEntity;
import elp.max.e.taxistation.repository.MechanicRepository;
import elp.max.e.taxistation.service.ServiceInterface;
import elp.max.e.taxistation.service.carService.CarServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.xml.bind.ValidationException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

import static java.util.Objects.isNull;

@Service
public class MechanicServiceImpl implements ServiceInterface<MechanicDto> {

    private final MechanicRepository mechanicRepository;

    public MechanicServiceImpl(MechanicRepository mechanicRepository) {
        this.mechanicRepository = mechanicRepository;
    }

    @Override
    @Transactional
    public MechanicDto findById(Long id) {
        MechanicEntity mechanicEntity = null;
        if (mechanicRepository.findById(id).isPresent()) {
            mechanicEntity = mechanicRepository.findById(id).get();
        }
        return MechanicConverter.fromMechanicEntityToMechanicDto(mechanicEntity);
    }

    @Transactional
    @Override
    public List<MechanicDto> findAll() {
        return null;
    }

    @Transactional
    @Override
    public MechanicDto save(MechanicDto dto) throws ValidationException {
        return null;
    }

    @Override
    @Transactional
    public MechanicDto update(Long id, MechanicDto dto) throws ValidationException {
        validateDto(dto);
        MechanicEntity mechanicEntity = mechanicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Механик " + dto + " не найден"));

        mechanicEntity.setRepairTime(dto.getRepairTime());
        mechanicEntity.setResource(dto.getResource());
        mechanicEntity.setBusy(dto.isBusy());
        mechanicEntity = mechanicRepository.save(mechanicEntity);

        return MechanicConverter.fromMechanicEntityToMechanicDto(mechanicEntity);

    }

    @Transactional
    @Override
    public void delete(Long id) {

    }

    @Override
    public void validateDto(MechanicDto dto) throws ValidationException {
        if (isNull(dto)) {
            throw new ValidationException("Object mechanic is null");
        }
        if (isNull(dto.getId())) {
            throw new ValidationException("Id ");
        }
    }

    public long repairCar(MechanicDto mechanicDto, CarEntity carEntity, CarServiceImpl carService) throws ValidationException {
        MechanicEntity mechanicEntity = MechanicConverter.fromMechanicDtoToMechanicEntity(mechanicDto);

        System.out.println("Метод repairCar запущен: " + new Date());
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Callable<TimerRepairCar> callable = new CallableClass(carEntity, carService, mechanicEntity, mechanicDto, this);
        Future<TimerRepairCar> future = executor.submit(callable);

        // Выводим в консоль полученное значение
        TimerRepairCar s;
        try {
            s = future.get();
            System.out.println("Результат выполнения нити Callable: " + s.scheduledExecutionTime());

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // Останавливаем пул потоков
        executor.shutdown();

        System.out.println("ремонт закончен: " + carEntity.getNumberCar());
        return System.currentTimeMillis();
    }
}
