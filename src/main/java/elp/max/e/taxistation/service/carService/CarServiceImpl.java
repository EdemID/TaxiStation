package elp.max.e.taxistation.service.carService;

import elp.max.e.taxistation.dto.CarDto;
import elp.max.e.taxistation.dto.MechanicDto;
import elp.max.e.taxistation.exception.EntityNotFoundException;
import elp.max.e.taxistation.exception.ValidationDtoException;
import elp.max.e.taxistation.model.CarEntity;
import elp.max.e.taxistation.repository.CarRepository;
import elp.max.e.taxistation.service.ServiceInterface;
import elp.max.e.taxistation.service.mechanicService.MechanicConverter;
import elp.max.e.taxistation.service.mechanicService.MechanicServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
public class CarServiceImpl implements ServiceInterface<CarDto> {

    private static final Logger logger = LoggerFactory.getLogger(CarServiceImpl.class);

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
    public CarDto findById(Long id) {
        CarDto carDto = null;
        if (carRepository.findById(id).isPresent()) {
            carDto = CarConverter.fromCarEntityToCarDto(carRepository.findById(id).get());
        } else {
            throw new EntityNotFoundException("Автомобиль " + id + " не найден!");
        }
        return carDto;
    }

    @Transactional
    public CarDto findByNumberCar(String number) {
        CarDto carDto;
        if (carRepository.findByNumberCar(number) != null) {
            carDto = CarConverter.fromCarEntityToCarDto(carRepository.findByNumberCar(number));
        } else {
            throw new EntityNotFoundException("Автомобиль " + number + " не найден!");
        }
        return carDto;
    }

    @Override
    @Transactional
    public CarDto save(CarDto dto) throws ValidationDtoException {
        validateDto(dto);
        CarEntity savedCar = carRepository.save(CarConverter.fromCarDtoToCarEntity(dto));
        return CarConverter.fromCarEntityToCarDto(savedCar);
    }

    @Override
    @Transactional
    public CarDto update(Long id, CarDto dto) throws ValidationDtoException {
        validateDto(dto);
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

    public CarDto getWorkingCar() throws ValidationDtoException {
        List<CarDto> carDtos = findAll();

        int workingResource;
        boolean busy;
        CarDto workingCar = null;
        for (CarDto carDto : carDtos) {
            logger.info("Car with id: {}", carDto.getId());
            busy = carDto.isBusy();
            workingResource = carDto.getResource();
            if (workingResource == 0) {
                logger.info("Car with id={} went to be repaired", carDto.getId());
                sendCarForRepair(CarConverter.fromCarDtoToCarEntity(carDto));
            } else if (!busy) {
                logger.info("Car with id={} not busy", carDto.getId());
                --workingResource;
                carDto.setResource(workingResource);
                carRepository.save(CarConverter.fromCarDtoToCarEntity(carDto));
                workingCar = carDto;
                logger.info("Working car with id={} not busy", carDto.getId());
                break;
            }
            logger.info("Car with id={} resource: {}", carDto.getId(), carDto.getResource());
        }
        return workingCar;
    }

    public void sendCarForRepair(CarEntity carEntity) throws ValidationDtoException {
        logger.info("Car with id={} arrived for repair", carEntity.getId());
        boolean busy = true;
        carEntity.setBusy(busy);
        // хардкор, так как пока у нас 1 механик, в будущем можно также реализовать двух механиков с режимом работы
        MechanicDto mechanicDto = mechanicService.findById(1L);
        carEntity.setMechanicEntity(MechanicConverter.fromMechanicDtoToMechanicEntity(mechanicDto));

        carRepository.save(carEntity);

        /**
         * если создать бин карСервиса, то будет цикл: карСервис <-> механикСервис
         */
        mechanicService.repairCar(mechanicDto, carEntity, this);
    }

    @Override
    public void validateDto(CarDto dto) throws ValidationDtoException {
        if (isNull(dto)) {
            throw new ValidationDtoException("Car is null");
        }
        if (isNull(dto.getNumberCar()) || dto.getNumberCar().isEmpty()) {
            throw new ValidationDtoException("Number car is empty");
        }
    }
}
