package elp.max.e.taxistation.service.dispatcher;

import elp.max.e.taxistation.BaseTest;
import elp.max.e.taxistation.dto.*;
import elp.max.e.taxistation.exception.ValidationDtoException;
import elp.max.e.taxistation.exception.WorkingDtoNotFoundException;
import elp.max.e.taxistation.repository.DispatcherRepository;
import elp.max.e.taxistation.service.car.CarServiceImpl;
import elp.max.e.taxistation.service.client.ClientServiceImpl;
import elp.max.e.taxistation.service.driver.DriverServiceImpl;
import elp.max.e.taxistation.service.mechanic.MechanicServiceImpl;
import elp.max.e.taxistation.service.orderNumber.OrderNumberServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
    void getWorkingDispatcher() {
        DispatcherDto workingDispatcher = dispatcherService.getWorkingDispatcher();
        DispatcherDto expected = dispatcherService.findById(workingDispatcher.getId());
        assertTrue(isMakeEqual(expected, workingDispatcher));
        assertTrue(workingDispatcher.isWorkStatus(), "Диспетчер " + workingDispatcher + " должен работать, но НЕ работает");
    }

    @Test
    @DisplayName("Проверить смену статусов водителя и автомобиля и состояние наряд-заказа у клиента после заказа")
    void releaseDriverAndCarAfterOrdering() throws ValidationDtoException {
        ClientDto clientDto = clientService.findById(1L);
        clientDto.setOrderNumber("Random order");
        clientService.update(clientDto.getId(), clientDto);
        DriverDto driverDto = driverService.findByName("driver-busy");
        CarDto carDto = carService.findByNumberCar("busy_car");

        long currentTime = new Date().getTime();
        long orderTime = 20000L;
        long endTimer = currentTime + orderTime + 1000L;
        dispatcherService.releaseClientAndDriverAndCarAfterOrdering(driverDto, carDto, clientDto, clientService, orderTime);
        // проверка до завершения заказа
        assertTrue(driverDto.isBusy());
        assertTrue(carDto.isBusy());
        assertEquals(carDto.getNumberCar(), driverDto.getCar());
        assertEquals("Random order", clientDto.getOrderNumber());
        // проверка после завершения заказа
        while (endTimer >= System.currentTimeMillis()){
            if (endTimer == System.currentTimeMillis()) {
                assertFalse(driverDto.isBusy());
                assertFalse(carDto.isBusy());
                assertEquals("free", driverDto.getCar());
                assertEquals("No order", clientDto.getOrderNumber());
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
        DispatcherDto dispatcherDto = dispatcherService.getWorkingDispatcher();

        OrderNumberDto orderNumberDto = dispatcherService.assignCarToDriverAndCallClient(clientDto, dispatcherDto, clientService);
        String orderNumber = orderNumberDto.getNumber();
        String client = orderNumberDto.getClient();
        String dispatcher = orderNumberDto.getDispatcher();
        String car = orderNumberDto.getCar();
        String driver = orderNumberDto.getDriver();

        assertEquals("Tom", client, "Назначен неверный клиент: " + client);
        assertEquals("Vladimir-dispatcher-working", dispatcher, "Назначен неверный диспетчер: " + dispatcher);
        assertEquals(orderNumber, orderNumber, "Назначен неверный номер наряд-заказа: " + orderNumber);
        assertEquals("car-not-busy", car, "Назначен неверный автомобиль: " + car);
        assertEquals("Aurora-driver-not-busy", driver, "Назначен неверный водитель: " + driver);
    }

    @Test
    @Sql(value = {"/data/import_positive_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/data/delete_positive_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Проверить отправку автомобиля на ремонт после заказа, если ресурс равен 0, и его починку")
    void sendCarForRepairIfResourceIsZeroAfterOrder() throws Exception {
        CarDto carDto = carService.getWorkingCar();
        carDto.setResource(1);
        carService.save(carDto);

        ClientDto clientDto = clientService.findById(1L);
        DispatcherDto dispatcherDto = dispatcherService.getWorkingDispatcher();
        MechanicDto mechanicDto = mechanicService.findById(1L);

        OrderNumberDto orderNumberDto = dispatcherService.assignCarToDriverAndCallClient(clientDto, dispatcherDto, clientService);
        String numberCar = orderNumberDto.getCar();
        carDto = carService.findByNumberCar(numberCar);

        if (carDto.getResource() == 0) {
            assertEquals(numberCar, carDto.getNumberCar(), "Назначен неверный автомобиль: " + numberCar);
            int recoveredResource = mechanicDto.getResource();
            long repairTime = mechanicDto.getRepairTime();
            long orderTime = 20000L;
            long timeAfterRepair = System.currentTimeMillis() + repairTime + orderTime + 1000L;
            while (timeAfterRepair >= System.currentTimeMillis()) {
                if (timeAfterRepair == System.currentTimeMillis()) {
                    carDto = carService.findById(carDto.getId());
                    Assertions.assertEquals(recoveredResource, carDto.getResource(), "Механик плохо постаралася: ресурс равен - " + carDto.getResource() + ", а должен быть - " + recoveredResource);
                    Assertions.assertFalse(carDto.isBusy(), "Автомобиль занят на ремонте: " + carDto.isBusy());
                }
            }
        }
    }

    @Test
    @DisplayName("Проверить, что не будет создан наряд-заказ из-за нерабочего водителя")
    void negativeTestAssignCarToDriverAndCallClient() throws Exception {
        ClientDto clientDto = new ClientDto(1L, "Tom", "No order");
        DispatcherDto dispatcherDto = dispatcherService.findById(1L);
        dispatcherDto.setDayoff("random");
        OrderNumberDto orderNumberDto = null;
        try {
            orderNumberDto = dispatcherService.assignCarToDriverAndCallClient(clientDto, dispatcherDto, clientService);
        }
        catch (WorkingDtoNotFoundException e) {
            assertEquals("Водители не работают!", e.getMessage(), "Водитель работает" );
        }
        // сломать тест
        if (orderNumberDto != null) {
            fail("Водитель работает: " + orderNumberDto.getDriver());
        }
    }

    private Boolean isMakeEqual(DispatcherDto actual, DispatcherDto expected) {
        return ((actual.getName().equals(expected.getName())) &&
                (actual.getId().equals(expected.getId())) &&
                (actual.getEndLunch().equals(expected.getEndLunch())));
    }
}
