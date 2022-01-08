package elp.max.e.taxistation.service.driverService;

import elp.max.e.taxistation.BaseTest;
import elp.max.e.taxistation.dto.DriverDto;
import elp.max.e.taxistation.repository.DispatcherRepository;
import elp.max.e.taxistation.service.carService.CarServiceImpl;
import elp.max.e.taxistation.service.clientService.ClientServiceImpl;
import elp.max.e.taxistation.service.mechanicService.MechanicServiceImpl;
import elp.max.e.taxistation.service.orderNumberService.OrderNumberServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DriverServiceImplTest extends BaseTest {

    @Autowired
    public DriverServiceImplTest(CarServiceImpl carService, DriverServiceImpl driverService, OrderNumberServiceImpl orderNumberService, DispatcherRepository dispatcherRepository, MechanicServiceImpl mechanicService, ClientServiceImpl clientService) {
        super(carService, driverService, orderNumberService, dispatcherRepository, mechanicService, clientService);
    }

    @Test
    @Sql({"/data/import_positive_data.sql"})
    void getWorkerDriver() {
        DriverDto workerDriver = driverService.getWorkerDriver();
        DriverDto expected = driverService.findById(workerDriver.getId());
        assertTrue(isMakeEqual(workerDriver, expected));
        assertTrue(workerDriver.isWorkStatus());
    }

    private Boolean isMakeEqual(DriverDto actual, DriverDto expected) {
        return ((actual.getName().equals(expected.getName())) &&
                (actual.getId().equals(expected.getId())) &&
                (actual.getDayoff().equals(expected.getDayoff())) &&
                (actual.isBusy() == expected.isBusy()) &&
                (actual.isWorkStatus() == expected.isWorkStatus()));
    }
}