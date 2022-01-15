package elp.max.e.taxistation.service.driver;

import elp.max.e.taxistation.dto.DriverDto;
import elp.max.e.taxistation.exception.EntityNotFoundException;
import elp.max.e.taxistation.exception.ValidationDtoException;
import elp.max.e.taxistation.model.DriverEntity;
import elp.max.e.taxistation.repository.DriverRepository;
import elp.max.e.taxistation.service.ServiceInterface;
import elp.max.e.taxistation.service.driver.converter.Driver2DriverDto;
import elp.max.e.taxistation.service.driver.converter.DriverDto2Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
public class DriverServiceImpl implements ServiceInterface<DriverDto> {

    private static final Logger logger = LoggerFactory.getLogger(DriverServiceImpl.class);

    private final DriverRepository driverRepository;

    public DriverServiceImpl(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public List<DriverDto> findAll() {
        return driverRepository.findAll().stream()
                .map(Driver2DriverDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DriverDto findById(Long id) {
        DriverEntity driverEntity = driverRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Водитель " + id + " не найден"));
        return Driver2DriverDto.convert(driverEntity);
    }

    @Transactional
    public DriverDto findByName(String name) {
        DriverEntity driverEntity = driverRepository.findByName(name);
        if (driverEntity == null) {
            throw new EntityNotFoundException("Водитель " + name + " не найден");
        }
        return Driver2DriverDto.convert(driverEntity);
    }

    @Override
    @Transactional
    public DriverDto save(DriverDto dto) throws ValidationDtoException {
        validateDto(dto);
        DriverEntity driverEntity = driverRepository.save(DriverDto2Driver.convert(dto));
        return Driver2DriverDto.convert(driverEntity);
    }

    @Override
    @Transactional
    public DriverDto update(Long id, DriverDto dto) throws ValidationDtoException {
        validateDto(dto);
        DriverEntity driverEntity = driverRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Водитель " + dto + " не найден"));
        driverEntity.setName(dto.getName());
        driverEntity.setWorkStatus(dto.isWorkStatus());
        driverEntity.setCar(dto.getCar());
        driverEntity.setDayoff(dto.getDayoff());
        driverEntity.setBusy(dto.isBusy());
        driverEntity = driverRepository.save(driverEntity);
        return Driver2DriverDto.convert(driverEntity);
    }

    @Override
    public void delete(Long id) {

    }

    public DriverDto getWorkingDriver() {
        List<DriverDto> driverDtos = findAll();
        String car;
        String dayOfWeek;
        DriverDto workingDriverDto = null;
        for(DriverDto driverDto : driverDtos) {
            car = driverDto.getCar();
            dayOfWeek = LocalDate.now().getDayOfWeek().toString();
            boolean workStatus;

            logger.info("Driver with name={}", driverDto.getName());

            if (dayOfWeek.equalsIgnoreCase(driverDto.getDayoff())) {
                logger.info("Driver with id={} not working: {}", driverDto.getId(), driverDto);
                workStatus = false;
            } else {
                logger.info("Driver with id={} working: {}", driverDto.getId(), driverDto);
                workStatus = true;
            }
            driverDto.setWorkStatus(workStatus);
            driverRepository.save(DriverDto2Driver.convert(driverDto));

            if ("free".equalsIgnoreCase(car) && workStatus) {
                logger.info("Working driver with name={} not busy", driverDto.getName());
                car = "Назначается машина";
                driverDto.setCar(car);
                driverRepository.save(DriverDto2Driver.convert(driverDto));
                workingDriverDto = driverDto;

                break;
            }
        }
        return workingDriverDto;
    }

    @Override
    public void validateDto(DriverDto dto) throws ValidationDtoException {
        if (isNull(dto)) {
            throw new ValidationDtoException("Driver is null");
        }
        if (isNull(dto.getName()) || dto.getName().isEmpty()) {
            throw new ValidationDtoException("Driver name is empty");
        }
    }
}
