package elp.max.e.taxistation.service.dispatcherService;

import elp.max.e.taxistation.dto.*;
import elp.max.e.taxistation.repository.DispatcherRepository;
import elp.max.e.taxistation.service.ServiceInterface;
import elp.max.e.taxistation.service.carService.CarServiceImpl;
import elp.max.e.taxistation.service.driverService.DriverServiceImpl;
import elp.max.e.taxistation.service.orderNumberService.OrderNumberServiceImpl;
import elp.max.e.taxistation.utils.DateUtil;
import elp.max.e.taxistation.utils.Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DispatcherServiceImpl implements ServiceInterface<DispatcherDto> {

    private final CarServiceImpl carService;
    private final DriverServiceImpl driverService;
    private final OrderNumberServiceImpl orderNumberService;
    private final DispatcherRepository dispatcherRepository;

    public DispatcherServiceImpl(CarServiceImpl carService, DriverServiceImpl driverService, OrderNumberServiceImpl orderNumberService, DispatcherRepository dispatcherRepository) {
        this.carService = carService;
        this.driverService = driverService;
        this.orderNumberService = orderNumberService;
        this.dispatcherRepository = dispatcherRepository;
    }

    @Override
    @Transactional
    public List<DispatcherDto> findAll() {
        return dispatcherRepository.findAll()
                .stream()
                .map(DispatcherConverter::fromDispatcherEntityToDispatcherDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DispatcherDto save(DispatcherDto dispatcherDto) throws ValidationException {
        return null;
    }

    @Override
    @Transactional
    public void delete(Long dispatcherId) {

    }

    public DispatcherDto getWorkerDispatcher() {
        List<DispatcherDto> dispatcherDtos = findAll();
        Date startLunch;
        Date endLunch;
        Date currentDate;
        String dayOfWeek;
        DispatcherDto workerDispatcher = null;
        for (DispatcherDto dispatcherDto : dispatcherDtos) {
            startLunch = DateUtil.convertFromStringToDateViaInstant(dispatcherDto.getStartLunch().split(":"));
            endLunch = DateUtil.convertFromStringToDateViaInstant(dispatcherDto.getEndLunch().split(":"));
            currentDate = new Date();
            dayOfWeek = LocalDate.now().getDayOfWeek().toString();
            boolean workStatus;
            if (endLunch.getTime() > currentDate.getTime() &&
                    currentDate.getTime() > startLunch.getTime() ||
                    dayOfWeek.equalsIgnoreCase(dispatcherDto.getDayoff()))
            {
                workStatus = false;
                dispatcherDto.setWorkStatus(workStatus);
                dispatcherRepository.save(DispatcherConverter.fromDispatcherDtoToDispatcherEntity(dispatcherDto));
            }
            else {
                workStatus = true;
                dispatcherDto.setWorkStatus(workStatus);
                dispatcherRepository.save(DispatcherConverter.fromDispatcherDtoToDispatcherEntity(dispatcherDto));
                workerDispatcher = dispatcherDto;
                break;
            }
        }
        return workerDispatcher;
    }

    private CarDto findWorkerCar() throws ValidationException {
        return carService.getWorkerCar();
    }

    private DriverDto findWorkerDriver() throws ValidationException {
        return driverService.getWorkerDriver();
    }

    public OrderNumberDto assignCarToDriverAndCallClient(ClientDto clientDto, DispatcherDto dispatcherDto) throws Exception {
        CarDto carDto = findWorkerCar();
        System.out.println(carDto);
        if (carDto == null) {
            throw new Exception();
        }
        DriverDto driverDto = findWorkerDriver();
        System.out.println(driverDto);
        if (driverDto == null) {
            throw new Exception();
        }

        //заняты клиентом
        driverDto.setBusy(true);
        carDto.setBusy(true);

        driverDto.setCar(carDto.getNumberCar());
        System.out.println(driverDto.getName());
        System.out.println(driverDto.getId());
        driverDto = driverService.update(driverDto.getId(), driverDto);
        carDto = carService.update(carDto.getId(), carDto);

        OrderNumberDto orderNumberDto = new OrderNumberDto();
        orderNumberDto.setNumber(Utils.randomizer());
        orderNumberDto.setClient(clientDto.getName());
        orderNumberDto.setDispatcher(dispatcherDto.getName());
        orderNumberDto.setDriver(driverDto.getName());
        orderNumberDto.setCar(carDto.getNumberCar());

        return orderNumberService.save(orderNumberDto);
    }
}
