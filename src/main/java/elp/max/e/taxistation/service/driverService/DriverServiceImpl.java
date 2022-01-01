package elp.max.e.taxistation.service.driverService;

import elp.max.e.taxistation.dto.DriverDto;
import elp.max.e.taxistation.model.DriverEntity;
import elp.max.e.taxistation.repository.DriverRepository;
import elp.max.e.taxistation.service.ServiceInterface;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public DriverDto save(DriverDto dto) throws ValidationException {
        return null;
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
                System.out.println("водитель не работает " + driverDto.getName());

                workStatus = false;
                driverDto.setWorkStatus(workStatus);
                driverRepository.save(DriverConverter.fromDriverDtoToDriverEntity(driverDto));
            }

            System.out.println("водитель работает " + driverDto.getName());

            if ("free".equalsIgnoreCase(car)) {
                System.out.println("водитель свободен " + driverDto.getName());

                workStatus = true;
                car = "Назначается машина";
                driverDto.setCar(car);
                driverDto.setWorkStatus(workStatus);
                System.out.println(driverDto.getId());
                driverRepository.save(DriverConverter.fromDriverDtoToDriverEntity(driverDto));
                workerDriverDto = driverDto;
                break;
            }
        }
        return workerDriverDto;
    }
}
