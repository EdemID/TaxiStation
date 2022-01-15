package elp.max.e.taxistation.service.dispatcher;

import elp.max.e.taxistation.dto.*;
import elp.max.e.taxistation.exception.EntityNotFoundException;
import elp.max.e.taxistation.exception.ValidationDtoException;
import elp.max.e.taxistation.exception.WorkingDtoNotFoundException;
import elp.max.e.taxistation.model.DispatcherEntity;
import elp.max.e.taxistation.repository.DispatcherRepository;
import elp.max.e.taxistation.service.ServiceInterface;
import elp.max.e.taxistation.service.car.CarServiceImpl;
import elp.max.e.taxistation.service.car.converter.CarDto2Car;
import elp.max.e.taxistation.service.client.ClientServiceImpl;
import elp.max.e.taxistation.service.dispatcher.converter.DispatcherDto2Dispatcher;
import elp.max.e.taxistation.service.driver.DriverServiceImpl;
import elp.max.e.taxistation.service.orderNumber.OrderNumberServiceImpl;
import elp.max.e.taxistation.utils.DateUtil;
import elp.max.e.taxistation.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
public class DispatcherServiceImpl implements ServiceInterface<DispatcherDto> {

    private static Logger logger = LoggerFactory.getLogger(DispatcherServiceImpl.class);

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
                .map(Dispatcher2DispatcherDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DispatcherDto findById(Long id) {
        return Dispatcher2DispatcherDto.convert(dispatcherRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Диспетчер " + id + " не найден")));
    }

    @Transactional
    public DispatcherDto findByName(String name) {
        DispatcherEntity dispatcherEntity = dispatcherRepository.findByName(name);
        if (dispatcherEntity == null) {
            throw new EntityNotFoundException("Диспетчер " + name + " не найден");
        }
        return Dispatcher2DispatcherDto.convert(dispatcherEntity);
    }

    @Override
    @Transactional
    public DispatcherDto save(DispatcherDto dto) throws ValidationDtoException {
        validateDto(dto);
        DispatcherEntity dispatcherEntity = dispatcherRepository.save(DispatcherDto2Dispatcher.convert(dto));
        return Dispatcher2DispatcherDto.convert(dispatcherEntity);
    }

    @Override
    @Transactional
    public DispatcherDto update(Long id, DispatcherDto dto) throws ValidationDtoException {
        return null;
    }

    @Override
    @Transactional
    public void delete(Long dispatcherId) {

    }

    public DispatcherDto getWorkingDispatcher() {
        List<DispatcherDto> dispatcherDtos = findAll();
        Date startLunch;
        Date endLunch;
        Date currentDate;
        String dayOfWeek;
        DispatcherDto workingDispatcher = null;
        for (DispatcherDto dispatcherDto : dispatcherDtos) {
            startLunch = DateUtil.convertFromStringToLocalDateTimeViaInstant(dispatcherDto.getStartLunch().split(":"));
            endLunch = DateUtil.convertFromStringToLocalDateTimeViaInstant(dispatcherDto.getEndLunch().split(":"));
            currentDate = new Date();
            dayOfWeek = LocalDate.now().getDayOfWeek().toString();
            boolean workStatus;
            logger.info("Dispatcher with id={}: {}", dispatcherDto.getId(), dispatcherDto);
            if ((endLunch.getTime() > currentDate.getTime() &&
                    currentDate.getTime() > startLunch.getTime()) ||
                    dayOfWeek.equalsIgnoreCase(dispatcherDto.getDayoff()))
            {
                logger.info("Dispatcher with id={} not working: {}", dispatcherDto.getId(), dispatcherDto.toString());
                workStatus = false;
                dispatcherDto.setWorkStatus(workStatus);
                dispatcherRepository.save(DispatcherDto2Dispatcher.convert(dispatcherDto));
            }
            else {
                logger.info("Dispatcher with id={} working: {}", dispatcherDto.getId(), dispatcherDto.toString());
                workStatus = true;
                dispatcherDto.setWorkStatus(workStatus);
                dispatcherRepository.save(DispatcherDto2Dispatcher.convert(dispatcherDto));
                workingDispatcher = dispatcherDto;

                break;
            }
        }
        if (workingDispatcher == null) {
            throw new WorkingDtoNotFoundException("Диспетчеры");
        }
        return workingDispatcher;
    }

    private CarDto findWorkingCar() throws ValidationDtoException {
        return carService.getWorkingCar();
    }

    private DriverDto findWorkingDriver() {
        return driverService.getWorkingDriver();
    }

    public OrderNumberDto assignCarToDriverAndCallClient(ClientDto clientDto, DispatcherDto dispatcherDto, ClientServiceImpl clientService) throws Exception {
        DriverDto driverDto = findWorkingDriver();
        if (driverDto == null) {
            logger.info("Not found a working driver");
            throw new WorkingDtoNotFoundException("Водители");
        }
        logger.info("Found a working driver with name={}", driverDto.getName());

        CarDto carDto = findWorkingCar();
        if (carDto == null) {
            logger.info("Not found a working car");
            throw new WorkingDtoNotFoundException("Автомобили");
        }
        logger.info("Found a working car with number={}", carDto.getNumberCar());

        //заняты клиентом
        driverDto.setBusy(true);
        logger.info("Working driver with name={} is busy with client who has an name={}", driverDto.getName(), clientDto.getName());
        carDto.setBusy(true);
        logger.info("Working car with number={} is busy with client who has an name={}", carDto.getNumberCar(), clientDto.getName());

        driverDto.setCar(carDto.getNumberCar());
        logger.info("Assign car={} to driver={}", carDto.getNumberCar(), driverDto.getName());

        driverDto = driverService.update(driverDto.getId(), driverDto);
        carDto = carService.update(carDto.getId(), carDto);

        OrderNumberDto orderNumberDto = new OrderNumberDto();
        orderNumberDto.setNumber(Utils.randomizer());
        orderNumberDto.setClient(clientDto.getName());
        orderNumberDto.setDispatcher(dispatcherDto.getName());
        orderNumberDto.setDriver(driverDto.getName());
        orderNumberDto.setCar(carDto.getNumberCar());

        // исходя из каких-то данных, диспетчер будет знать время заказа
        releaseClientAndDriverAndCarAfterOrdering(driverDto, carDto, clientDto, clientService, 20000L);

        return orderNumberService.save(orderNumberDto);
    }

    public void releaseClientAndDriverAndCarAfterOrdering(DriverDto driverDto, CarDto carDto, ClientDto clientDto, ClientServiceImpl clientService, long orderTime) {
        logger.info("Метод releaseDriverAndCarAfterOrdering запущен: {}", new Date());
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                logger.info("Задача таск запущена: {}", new Date());
                try {
                    driverDto.setBusy(false);
                    driverDto.setCar("free");

                    carDto.setBusy(false);

                    clientDto.setOrderNumber("No order");

                    driverService.update(driverDto.getId(), driverDto);
                    carService.update(carDto.getId(), carDto);
                    clientService.update(clientDto.getId(), clientDto);

                    // проверяем ресурс и отправляем после заказа машину, если равен 0
                    if (carDto.getResource() == 0) {
                        carService.sendCarForRepair(CarDto2Car.convert(carDto));
                    }
                } catch (ValidationDtoException e) {
                    e.printStackTrace();
                }
            }
        };
        Timer timer = new Timer("Врема заказа");
        logger.info("Start timer \"Врема заказа\" in: {} mc", orderTime);
        timer.schedule(task, orderTime);
    }

    @Override
    public void validateDto(DispatcherDto dto) throws ValidationDtoException {
        if (isNull(dto)) {
            throw new ValidationDtoException("Dispatcher is null");
        }
        if (isNull(dto.getName()) || dto.getName().isEmpty()) {
            throw new ValidationDtoException("Dispatcher name is empty");
        }
    }
}
