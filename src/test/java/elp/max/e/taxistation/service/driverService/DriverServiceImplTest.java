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
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DriverServiceImplTest extends BaseTest {

    @Autowired
    public DriverServiceImplTest(CarServiceImpl carService, DriverServiceImpl driverService, OrderNumberServiceImpl orderNumberService, DispatcherRepository dispatcherRepository, MechanicServiceImpl mechanicService, ClientServiceImpl clientService) {
        super(carService, driverService, orderNumberService, dispatcherRepository, mechanicService, clientService);
    }

    @Test
    @Sql(value = {"/data/import_positive_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/data/delete_positive_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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