package elp.max.e.taxistation.service.carService;

import elp.max.e.taxistation.dto.CarDto;
import elp.max.e.taxistation.model.CarEntity;
import elp.max.e.taxistation.model.MechanicEntity;
import elp.max.e.taxistation.repository.CarRepository;
import elp.max.e.taxistation.service.ServiceInterface;
import elp.max.e.taxistation.service.mechanicService.MechanicConverter;
import elp.max.e.taxistation.service.mechanicService.MechanicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
public class CarServiceImpl implements ServiceInterface<CarDto> {

    private final MechanicServiceImpl mechanicService;
    private final CarRepository carRepository;

    @Autowired
    public CarServiceImpl(MechanicServiceImpl mechanicService, CarRepository carRepository) {
        this.mechanicService = mechanicService;
        this.carRepository = carRepository;
    }

    @Override
    @Transactional
    public List<CarDto> findAll() {
        return carRepository.findAll()
                .stream()
                .map(CarConverter::fromCarEntityToCarDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CarDto save(CarDto dto) throws ValidationException {
        validateCarDto(dto);
        CarEntity savedCar = carRepository.save(CarConverter.fromCarDtoToCarEntity(dto));
        return CarConverter.fromCarEntityToCarDto(savedCar);
    }

    @Transactional
    public CarDto update(Long id, CarDto dto) throws ValidationException {
        validateCarDto(dto);
        CarEntity carEntity = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Автомобиль " + dto + " не найден"));
        carEntity.setNumberCar(dto.getNumberCar());
        carEntity.setResource(dto.getResource());
        carEntity.setBusy(dto.isBusy());
        carEntity = carRepository.save(carEntity);
        return CarConverter.fromCarEntityToCarDto(carEntity);
    }

    @Transactional
    public void delete(Long id) {
        carRepository.deleteById(id);
    }

    private void validateCarDto(CarDto dto) throws ValidationException {
        if (isNull(dto)) {
            throw new ValidationException("Object car is null");
        }
        if (isNull(dto.getNumberCar()) || dto.getNumberCar().isEmpty()) {
            throw new ValidationException("Number car is empty");
        }
    }

    public CarDto getWorkerCar() throws ValidationException {
        List<CarDto> carDtos = findAll();

        int workerResource;
        boolean busy;
        CarDto workerCar = null;
        for (CarDto carDto : carDtos) {
            System.out.println("id машинки " + carDto.getId());
            workerResource = carDto.getResource();
            if (workerResource != 0) {
                System.out.println("свободная " + carDto.getId());
                --workerResource;
                carDto.setResource(workerResource);
                carRepository.save(CarConverter.fromCarDtoToCarEntity(carDto));
                workerCar = carDto;
                break;
            } else {
                System.out.println("уехала на ремонт: " + carDto.getId());
                busy = true;
                carDto.setBusy(busy);
                // хардкор, так как пока у нас 1 механик и не придумал как убрать хардкор
                sendCarForRepair(CarConverter.fromCarDtoToCarEntity(carDto), 1L);
            }
            System.out.println(carDto.getResource());
        }
        return workerCar;
    }

    public void sendCarForRepair(CarEntity carEntity, Long id) throws ValidationException {
        MechanicEntity mechanicEntity = MechanicConverter.fromMechanicDtoToMechanicEntity(mechanicService.findById(id));
        mechanicEntity.setCarBeingRepaired("Автомобиль " + carEntity.getNumberCar() + " в ремонте");
        mechanicEntity.setCarBeingRepaired(carEntity.getNumberCar());
        mechanicService.update(id, MechanicConverter.fromMechanicEntityToMechanicDto(mechanicEntity));

        carEntity.setMechanicEntity(mechanicEntity);
        carEntity.setBusy(true);

        carRepository.save(carEntity);

        /**
         * если создать бин карСервиса, то будет цикл: карСервис <-> механикСервис
         */
        mechanicService.repairCar(id, carEntity, this);
    }
}
