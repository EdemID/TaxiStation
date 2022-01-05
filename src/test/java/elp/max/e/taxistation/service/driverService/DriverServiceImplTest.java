package elp.max.e.taxistation.service.driverService;

import elp.max.e.taxistation.dto.DriverDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("DriverService test")
@TestPropertySource(locations = "classpath:application-test.properties")
class DriverServiceImplTest {

    private final DriverServiceImpl driverService;

    @Autowired
    DriverServiceImplTest(DriverServiceImpl driverService) {
        this.driverService = driverService;
    }

    @Test
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