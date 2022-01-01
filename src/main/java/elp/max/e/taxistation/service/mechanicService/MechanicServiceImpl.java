package elp.max.e.taxistation.service.mechanicService;

import elp.max.e.taxistation.dto.MechanicDto;
import elp.max.e.taxistation.model.CarEntity;
import elp.max.e.taxistation.model.MechanicEntity;
import elp.max.e.taxistation.repository.MechanicRepository;
import elp.max.e.taxistation.service.ServiceInterface;
import elp.max.e.taxistation.service.carService.CarConverter;
import elp.max.e.taxistation.service.carService.CarServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.xml.bind.ValidationException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static java.util.Objects.isNull;

@Service
public class MechanicServiceImpl implements ServiceInterface<MechanicDto> {

    private final MechanicRepository mechanicRepository;

    public MechanicServiceImpl(MechanicRepository mechanicRepository) {
        this.mechanicRepository = mechanicRepository;
    }

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

    @Transactional
    public MechanicDto update(Long id, MechanicDto dto) throws ValidationException {
        validationMechanicDto(dto);
        MechanicEntity mechanicEntity = mechanicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Механик " + dto + " не найден"));

        mechanicEntity.setCarBeingRepaired(dto.getCarBeingRepaired());
        mechanicEntity.setRepairTime(dto.getRepairTime());
        mechanicEntity = mechanicRepository.save(mechanicEntity);

        return MechanicConverter.fromMechanicEntityToMechanicDto(mechanicEntity);

    }

    @Transactional
    @Override
    public void delete(Long id) {

    }

    private void validationMechanicDto(MechanicDto dto) throws ValidationException {
        if (isNull(dto)) {
            throw new ValidationException("Object mechanic is null");
        }
        if (isNull(dto.getId())) {
            throw new ValidationException("Id ");
        }
    }

    public void repairCar(Long id, CarEntity carEntity, CarServiceImpl carService) {
        MechanicEntity mechanicEntity = MechanicConverter.fromMechanicDtoToMechanicEntity(findById(id));

        System.out.println("Метод repairCar запущен: " + new Date());

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Задача таск запущена: " + new Date());
                try {
                    mechanicEntity.setCarBeingRepaired("Автомобиль " + carEntity.getNumberCar() + " не на ремонте");

                    update(id, MechanicConverter.fromMechanicEntityToMechanicDto(mechanicEntity));

                    // как избавиться от механика?
                    carEntity.setMechanicEntity(null);
                    carEntity.setBusy(false);
                    carEntity.setResource(5);
                    carService.save(CarConverter.fromCarEntityToCarDto(carEntity));
                } catch (ValidationException e) {
                    e.printStackTrace();
                }
            }
        };
        Timer timer = new Timer("Время ремонта");
        long delay = 10000L;
        timer.schedule(task, delay);
    }
}
