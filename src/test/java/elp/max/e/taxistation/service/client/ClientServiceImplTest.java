package elp.max.e.taxistation.service.client;

import elp.max.e.taxistation.BaseTest;
import elp.max.e.taxistation.dto.*;
import elp.max.e.taxistation.exception.CallBeforeCompletionOfOrderException;
import elp.max.e.taxistation.repository.DispatcherRepository;
import elp.max.e.taxistation.service.car.CarServiceImpl;
import elp.max.e.taxistation.service.driver.DriverServiceImpl;
import elp.max.e.taxistation.service.mechanic.MechanicServiceImpl;
import elp.max.e.taxistation.service.orderNumber.OrderNumberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

class ClientServiceImplTest extends BaseTest {

    @Autowired
    public ClientServiceImplTest(CarServiceImpl carService, DriverServiceImpl driverService, OrderNumberServiceImpl orderNumberService, DispatcherRepository dispatcherRepository, MechanicServiceImpl mechanicService, ClientServiceImpl clientService) {
        super(carService, driverService, orderNumberService, dispatcherRepository, mechanicService, clientService);
    }

    @Test
    @Sql(value = {"/data/import_positive_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/data/delete_positive_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Проверить создание, содержание наряд-заказа и смену статусов после заказа")
    void call() throws Exception {
        ClientDto clientDto = clientService.findById(1L);

        OrderNumberDto orderNumberDto = clientService.call(clientDto.getId());
        clientDto = clientService.findById(1L);
        String numberOrder = orderNumberDto.getNumber();
        DispatcherDto dispatcherDtoOnOrder = dispatcherService.findByName(orderNumberDto.getDispatcher());
        DriverDto driverDtoOnOrder = driverService.findByName(orderNumberDto.getDriver());
        CarDto carDtoOnOrder = carService.findByNumberCar(orderNumberDto.getCar());
        // проверка до завершения заказа
        assertEquals(clientDto.getOrderNumber(), numberOrder);
        assertTrue(dispatcherDtoOnOrder.isWorkStatus());
        assertTrue(driverDtoOnOrder.isWorkStatus());
        assertTrue(driverDtoOnOrder.isBusy());
        assertTrue(carDtoOnOrder.isBusy());

        // проверка после завершения заказа
        long startOrderTime = System.currentTimeMillis();
        long endOrderTime = startOrderTime + 20000L + 1000L;
        while (endOrderTime >= System.currentTimeMillis()) {
            if (endOrderTime == System.currentTimeMillis()) {
                driverDtoOnOrder = driverService.findByName(orderNumberDto.getDriver());
                carDtoOnOrder = carService.findByNumberCar(orderNumberDto.getCar());
                assertFalse(driverDtoOnOrder.isBusy());
                assertFalse(carDtoOnOrder.isBusy());
            }
        }
    }

    @Test
    @Sql(value = {"/data/import_positive_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/data/delete_positive_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Проверить вывод номера заказа при вызове такси до завершения заказа")
    void negativeTestCallBeforeCompletionOfOrder() throws Exception {
        ClientDto clientDto = clientService.findById(1L);

        OrderNumberDto orderNumberDto = clientService.call(clientDto.getId());
        clientDto = clientService.findById(1L);
        String numberOrder = orderNumberDto.getNumber();
        // проверка номера первого заказа
        assertEquals(clientDto.getOrderNumber(), numberOrder);

        Thread.sleep(5000);
        // вызов такси до завршения первого заказа
        try {
            clientService.call(clientDto.getId());
        } catch (CallBeforeCompletionOfOrderException e) {
            assertTrue(e.getMessage().contains(numberOrder));
        }
    }
}