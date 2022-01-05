package elp.max.e.taxistation.service.clientService;

import elp.max.e.taxistation.dto.*;
import elp.max.e.taxistation.service.carService.CarServiceImpl;
import elp.max.e.taxistation.service.dispatcherService.DispatcherServiceImpl;
import elp.max.e.taxistation.service.driverService.DriverServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("ClientService test")
@TestPropertySource(locations = "classpath:application-test.properties")
class ClientServiceImplTest {

    private final DispatcherServiceImpl dispatcherService;
    private final ClientServiceImpl clientService;
    private final DriverServiceImpl driverService;
    private final CarServiceImpl carService;

    @Autowired
    ClientServiceImplTest(DispatcherServiceImpl dispatcherService, ClientServiceImpl clientService, DriverServiceImpl driverService, CarServiceImpl carService) {
        this.dispatcherService = dispatcherService;
        this.clientService = clientService;
        this.driverService = driverService;
        this.carService = carService;
    }

    @Test
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