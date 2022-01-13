package elp.max.e.taxistation.service.dispatcherService;

import elp.max.e.taxistation.dto.*;
import elp.max.e.taxistation.exception.EntityNotFoundException;
import elp.max.e.taxistation.exception.ValidationDtoException;
import elp.max.e.taxistation.exception.WorkerDtoNotFoundException;
import elp.max.e.taxistation.model.DispatcherEntity;
import elp.max.e.taxistation.repository.DispatcherRepository;
import elp.max.e.taxistation.service.ServiceInterface;
import elp.max.e.taxistation.service.carService.CarConverter;
import elp.max.e.taxistation.service.carService.CarServiceImpl;
import elp.max.e.taxistation.service.clientService.ClientServiceImpl;
import elp.max.e.taxistation.service.driverService.DriverServiceImpl;
import elp.max.e.taxistation.service.orderNumberService.OrderNumberServiceImpl;
import elp.max.e.taxistation.utils.DateUtil;
import elp.max.e.taxistation.utils.Utils;
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
    public DispatcherDto findById(Long id) {
        return DispatcherConverter.fromDispatcherEntityToDispatcherDto(dispatcherRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Диспетчер " + id + " не найден")));
    }

    @Transactional
    public DispatcherDto findByName(String name) {
        DispatcherEntity dispatcherEntity = dispatcherRepository.findByName(name);
        if (dispatcherEntity == null) {
            throw new EntityNotFoundException("Диспетчер " + name + " не найден");
        }
        return DispatcherConverter.fromDispatcherEntityToDispatcherDto(dispatcherEntity);
    }

    @Override
    @Transactional
    public DispatcherDto save(DispatcherDto dto) throws ValidationDtoException {
        validateDto(dto);
        DispatcherEntity dispatcherEntity = dispatcherRepository.save(DispatcherConverter.fromDispatcherDtoToDispatcherEntity(dto));
        return DispatcherConverter.fromDispatcherEntityToDispatcherDto(dispatcherEntity);
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

    public DispatcherDto getWorkerDispatcher() {
        List<DispatcherDto> dispatcherDtos = findAll();
        Date startLunch;
        Date endLunch;
        Date currentDate;
        String dayOfWeek;
        DispatcherDto workerDispatcher = null;
        for (DispatcherDto dispatcherDto : dispatcherDtos) {
            startLunch = DateUtil.convertFromStringToLocalDateTimeViaInstant(dispatcherDto.getStartLunch().split(":"));
            endLunch = DateUtil.convertFromStringToLocalDateTimeViaInstant(dispatcherDto.getEndLunch().split(":"));
            currentDate = new Date();
            dayOfWeek = LocalDate.now().getDayOfWeek().toString();
            boolean workStatus;
            System.out.println(346 + dispatcherDto.getName());
            System.out.println(346 + endLunch.toString());
            System.out.println(346 +  startLunch.toString());
            if ((endLunch.getTime() > currentDate.getTime() &&
                    currentDate.getTime() > startLunch.getTime()) ||
                    dayOfWeek.equalsIgnoreCase(dispatcherDto.getDayoff()))
            {
                System.out.println(234 + dispatcherDto.toString());
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
        if (workerDispatcher == null) {
            throw new WorkerDtoNotFoundException("Диспетчеры");
        }
        return workerDispatcher;
    }

    private CarDto findWorkerCar() throws ValidationDtoException {
        return carService.getWorkerCar();
    }

    private DriverDto findWorkerDriver() {
        return driverService.getWorkerDriver();
    }

    public OrderNumberDto assignCarToDriverAndCallClient(ClientDto clientDto, DispatcherDto dispatcherDto, ClientServiceImpl clientService) throws Exception {
        DriverDto driverDto = findWorkerDriver();
        System.out.println(driverDto);
        if (driverDto == null) {
            throw new WorkerDtoNotFoundException("Водители");
        }

        CarDto carDto = findWorkerCar();
        System.out.println(carDto);
        if (carDto == null) {
            throw new WorkerDtoNotFoundException("Автомобили");
        }

        //заняты клиентом
        driverDto.setBusy(true);
        System.out.println("Driver " + driverDto.getName() + " занят");
        carDto.setBusy(true);
        System.out.println("Car " + carDto.getNumberCar() + " занят");

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

        // исходя из каких-то данных, диспетчер будет знать время заказа
        releaseDriverAndCarAfterOrdering(driverDto, carDto, clientDto, clientService, 20000L);

        return orderNumberService.save(orderNumberDto);
    }

    public void releaseDriverAndCarAfterOrdering(DriverDto driverDto, CarDto carDto, ClientDto clientDto, ClientServiceImpl clientService, long orderTime) {
        System.out.println("Метод releaseDriverAndCarAfterOrdering запущен: " + new Date());

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Задача таск запущена: " + new Date());
                try {
                    driverDto.setBusy(false);
                    driverDto.setCar("free");

                    carDto.setBusy(false);

                    clientDto.setOrderNumber("No order");

                    System.out.println("=================");
                    System.out.println(driverDto.getId());
                    System.out.println(driverDto.getName());
                    System.out.println(clientDto.getOrderNumber());
                    System.out.println("=================");

                    driverService.update(driverDto.getId(), driverDto);
                    carService.update(carDto.getId(), carDto);
                    clientService.update(clientDto.getId(), clientDto);

                    // проверяем ресурс и отправляем после заказа машину, если равен 0
                    if (carDto.getResource() == 0) {
                        carService.sendCarForRepair(CarConverter.fromCarDtoToCarEntity(carDto));
                    }
                } catch (ValidationDtoException e) {
                    e.printStackTrace();
                }
            }
        };
        Timer timer = new Timer("Врема заказа");
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
