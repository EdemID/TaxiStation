package elp.max.e.taxistation.service.driverService;

import elp.max.e.taxistation.dto.ClientDto;
import elp.max.e.taxistation.dto.DriverDto;
import elp.max.e.taxistation.model.ClientEntity;
import elp.max.e.taxistation.model.DriverEntity;
import elp.max.e.taxistation.repository.DriverRepository;
import elp.max.e.taxistation.service.ServiceInterface;
import elp.max.e.taxistation.service.clientService.ClientConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
public class DriverServiceImpl implements ServiceInterface<DriverDto> {

    private final DriverRepository driverRepository;

    public DriverServiceImpl(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public List<DriverDto> findAll() {
        return driverRepository.findAll().stream()
                .map(DriverConverter::fromDriverEntityToDriverDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public DriverDto findById(Long id) {
        DriverEntity driverEntity = driverRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Водитель " + id + " не найден"));
        return DriverConverter.fromDriverEntityToDriverDto(driverEntity);
    }

    @Transactional
    public DriverDto findByName(String name) {
        DriverEntity driverEntity = driverRepository.findByName(name);
        if (driverEntity == null) {
            throw new EntityNotFoundException("Водитель " + name + " не найден");
        }
        return DriverConverter.fromDriverEntityToDriverDto(driverEntity);
    }

    @Override
    @Transactional
    public DriverDto save(DriverDto dto) throws ValidationException {
        validateDriverDto(dto);
        DriverEntity driverEntity = driverRepository.save(DriverConverter.fromDriverDtoToDriverEntity(dto));
        return DriverConverter.fromDriverEntityToDriverDto(driverEntity);
    }

    @Transactional
    public DriverDto update(Long id, DriverDto dto) throws ValidationException {
        DriverEntity driverEntity = driverRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Водитель " + dto + " не найден"));
        driverEntity.setName(dto.getName());
        driverEntity.setWorkStatus(dto.isWorkStatus());
        driverEntity.setCar(dto.getCar());
        driverEntity.setDayoff(dto.getDayoff());
        driverEntity.setBusy(dto.isBusy());
        driverEntity = driverRepository.save(driverEntity);
        return DriverConverter.fromDriverEntityToDriverDto(driverEntity);
    }

    @Override
    public void delete(Long id) {

    }

    public DriverDto getWorkerDriver() {
        List<DriverDto> driverDtos = findAll();
        String car;
        String dayOfWeek;
        DriverDto workerDriverDto = null;
        for(DriverDto driverDto : driverDtos) {
            car = driverDto.getCar();
            dayOfWeek = LocalDate.now().getDayOfWeek().toString();
            boolean workStatus;

            System.out.println("водитель " + driverDto.getName());

            if (dayOfWeek.equalsIgnoreCase(driverDto.getDayoff())) {
                System.out.println("водитель не работает ВЫХОДНОЙ " + driverDto.getName());
                workStatus = false;
            } else {
                System.out.println("водитель работает " + driverDto.getName());
                workStatus = true;
            }
            driverDto.setWorkStatus(workStatus);
            driverRepository.save(DriverConverter.fromDriverDtoToDriverEntity(driverDto));

            if ("free".equalsIgnoreCase(car) && workStatus) {
                System.out.println("водитель свободен и работает " + driverDto.getName());
                car = "Назначается машина";
                driverDto.setCar(car);
                driverRepository.save(DriverConverter.fromDriverDtoToDriverEntity(driverDto));
                workerDriverDto = driverDto;

                break;
            }
        }
        return workerDriverDto;
    }

    private void validateDriverDto(DriverDto dto) throws ValidationException {
        if (isNull(dto)) {
            throw new ValidationException("Object driver is null");
        }
        if (isNull(dto.getName()) || dto.getName().isEmpty()) {
            throw new ValidationException("Name is empty");
        }
    }
}
