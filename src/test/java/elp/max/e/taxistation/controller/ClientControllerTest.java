package elp.max.e.taxistation.controller;

import elp.max.e.taxistation.BaseTest;
import elp.max.e.taxistation.dto.OrderNumberDto;
import elp.max.e.taxistation.repository.DispatcherRepository;
import elp.max.e.taxistation.service.carService.CarServiceImpl;
import elp.max.e.taxistation.service.clientService.ClientServiceImpl;
import elp.max.e.taxistation.service.driverService.DriverServiceImpl;
import elp.max.e.taxistation.service.mechanicService.MechanicServiceImpl;
import elp.max.e.taxistation.service.orderNumberService.OrderNumberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientControllerTest extends BaseTest {

    private final ClientController clientController;

    @Autowired
    public ClientControllerTest(ClientController clientController, CarServiceImpl carService, DriverServiceImpl driverService, OrderNumberServiceImpl orderNumberService, DispatcherRepository dispatcherRepository, MechanicServiceImpl mechanicService, ClientServiceImpl clientService) {
        super(carService, driverService, orderNumberService, dispatcherRepository, mechanicService, clientService);
        this.clientController = clientController;
    }

    @Test
    @Sql({"/data/import_positive_data.sql", "/data/import_additional_car_and_driver.sql"})
    @DisplayName("Проверить создание и содержание наряд-заказа у двух клиентов")
    void call() throws Exception {
        OrderNumberDto actualOrderNumber = clientController.call(1L);
        String expectedClientName = clientService.findById(1L).getName();
        assertEquals(expectedClientName, actualOrderNumber.getClient());

        OrderNumberDto actualOrderNumberSecond = clientController.call(2L);
        String expectedClientNameSecond = clientService.findById(2L).getName();
        assertEquals(expectedClientNameSecond, actualOrderNumberSecond.getClient());
    }
}