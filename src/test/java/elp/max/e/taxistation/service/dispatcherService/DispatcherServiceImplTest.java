package elp.max.e.taxistation.service.dispatcherService;

import elp.max.e.taxistation.BaseTest;
import elp.max.e.taxistation.dto.*;
import elp.max.e.taxistation.repository.DispatcherRepository;
import elp.max.e.taxistation.service.carService.CarServiceImpl;
import elp.max.e.taxistation.service.clientService.ClientServiceImpl;
import elp.max.e.taxistation.service.driverService.DriverServiceImpl;
import elp.max.e.taxistation.service.mechanicService.MechanicServiceImpl;
import elp.max.e.taxistation.service.orderNumberService.OrderNumberServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class DispatcherServiceImplTest extends BaseTest {

    @Autowired
    public DispatcherServiceImplTest(CarServiceImpl carService, DriverServiceImpl driverService, OrderNumberServiceImpl orderNumberService, DispatcherRepository dispatcherRepository, MechanicServiceImpl mechanicService, ClientServiceImpl clientService) {
        super(carService, driverService, orderNumberService, dispatcherRepository, mechanicService, clientService);
    }

    // как в тестах обновить на нужные значения сущностей
    @Test
    @Sql(value = {"/data/import_positive_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/data/delete_positive_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Проверить рабочего диспетчера")
    void getWorkerDispatcher() {
        DispatcherDto workerDispatcher = dispatcherService.getWorkerDispatcher();
        DispatcherDto expected = dispatcherService.findById(workerDispatcher.getId());
        assertTrue(isMakeEqual(expected, workerDispatcher));
        assertTrue(workerDispatcher.isWorkStatus(), "Диспетчер " + workerDispatcher + " должен работать, но НЕ работает");
    }

    @Test
    @DisplayName("Проверить смену статусов водителя и автомобиля после заказа")
    void releaseDriverAndCarAfterOrdering() {
        DriverDto driverDto = driverService.findByName("driver-busy");
        CarDto carDto = carService.findByNumberCar("busy_car");

        long currentTime = new Date().getTime();
        long orderTime = 20000L;
        long endTimer = currentTime + orderTime + 1000L;
        dispatcherService.releaseDriverAndCarAfterOrdering(driverDto, carDto, orderTime);
        // проверка до завершения заказа
        assertTrue(driverDto.isBusy());
        assertTrue(carDto.isBusy());
        assertEquals(carDto.getNumberCar(), driverDto.getCar());
        // проверка после завершения заказа
        while (endTimer >= System.currentTimeMillis()){
            if (endTimer == System.currentTimeMillis()) {
                assertFalse(driverDto.isBusy());
                assertFalse(carDto.isBusy());
                assertEquals("free", driverDto.getCar());
                break;
            }
        }
    }

    @Test
    @Sql(value = {"/data/import_positive_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/data/delete_positive_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Проверить создание и содержание наряд-заказа")
    void assignCarToDriverAndCallClient() throws Exception {
        ClientDto clientDto = new ClientDto(1L, "Tom", "No order");
        DispatcherDto dispatcherDto = dispatcherService.getWorkerDispatcher();

        OrderNumberDto orderNumberDto = dispatcherService.assignCarToDriverAndCallClient(clientDto, dispatcherDto);
        String orderNumber = orderNumberDto.getNumber();
        String client = orderNumberDto.getClient();
        String dispatcher = orderNumberDto.getDispatcher();
        String car = orderNumberDto.getCar();
        String driver = orderNumberDto.getDriver();

        assertEquals("Tom", client, "Назначен неверный клиент: " + client);
        assertEquals("Vladimir-dispatcher-worker", dispatcher, "Назначен неверный диспетчер: " + dispatcher);
        assertEquals(orderNumber, orderNumber, "Назначен неверный номер наряд-заказа: " + orderNumber);
        assertEquals("car-not-busy", car, "Назначен неверный автомобиль: " + car);
        assertEquals("Aurora-driver-not-busy", driver, "Назначен неверный водитель: " + driver);
    }

    @Test
    @Sql(value = {"/data/import_positive_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/data/delete_positive_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Проверить отправку автомобиля на ремонт после заказа, если ресурс равен 0, и его починку")
    void sendCarForRepairIfResourceIsZeroAfterOrder() throws Exception {
        CarDto carDto = carService.getWorkerCar();
        carDto.setResource(1);
        carService.save(carDto);

        ClientDto clientDto = clientService.findById(1L);
        DispatcherDto dispatcherDto = dispatcherService.getWorkerDispatcher();
        MechanicDto mechanicDto = mechanicService.findById(1L);

        OrderNumberDto orderNumberDto = dispatcherService.assignCarToDriverAndCallClient(clientDto, dispatcherDto);
        String numberCar = orderNumberDto.getCar();
        carDto = carService.findByNumberCar(numberCar);
        System.out.println(carDto);

        if (carDto.getResource() == 0) {
            assertEquals(numberCar, carDto.getNumberCar(), "Назначен неверный автомобиль: " + numberCar);
            int recoveredResource = mechanicDto.getResource();
            long repairTime = mechanicDto.getRepairTime();
            long timeAfterRepair = System.currentTimeMillis() + repairTime + 2000L;
            while (timeAfterRepair >= System.currentTimeMillis()) {
                if (timeAfterRepair == System.currentTimeMillis()) {
                    carDto = carService.findById(carDto.getId());
                    System.out.println(carDto.getId());
                    System.out.println(carDto.getResource());
                    Assertions.assertEquals(recoveredResource, carDto.getResource(), "Механик плохо постаралася: ресурс равен - " + carDto.getResource() + ", а должен быть - " + recoveredResource);
                    Assertions.assertFalse(carDto.isBusy(), "Автомобиль занят на ремонте: " + carDto.isBusy());
                }
            }
        }
    }

    private Boolean isMakeEqual(DispatcherDto actual, DispatcherDto expected) {
        return ((actual.getName().equals(expected.getName())) &&
                (actual.getId().equals(expected.getId())) &&
                (actual.getEndLunch().equals(expected.getEndLunch())));
    }
}
