package elp.max.e.taxistation.service.clientService;

import elp.max.e.taxistation.BaseTest;
import elp.max.e.taxistation.dto.*;
import elp.max.e.taxistation.repository.DispatcherRepository;
import elp.max.e.taxistation.service.carService.CarServiceImpl;
import elp.max.e.taxistation.service.driverService.DriverServiceImpl;
import elp.max.e.taxistation.service.mechanicService.MechanicServiceImpl;
import elp.max.e.taxistation.service.orderNumberService.OrderNumberServiceImpl;
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
    @Sql({"/data/import_positive_data.sql"})
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
}