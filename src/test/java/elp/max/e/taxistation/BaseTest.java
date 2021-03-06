package elp.max.e.taxistation;

import elp.max.e.taxistation.dto.DispatcherDto;
import elp.max.e.taxistation.dto.DriverDto;
import elp.max.e.taxistation.exception.ValidationDtoException;
import elp.max.e.taxistation.repository.DispatcherRepository;
import elp.max.e.taxistation.service.car.CarServiceImpl;
import elp.max.e.taxistation.service.client.ClientServiceImpl;
import elp.max.e.taxistation.service.dispatcher.DispatcherServiceImpl;
import elp.max.e.taxistation.service.driver.DriverServiceImpl;
import elp.max.e.taxistation.service.mechanic.MechanicServiceImpl;
import elp.max.e.taxistation.service.orderNumber.OrderNumberServiceImpl;
import elp.max.e.taxistation.utils.DateUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
public class BaseTest {

    protected final DispatcherServiceImpl dispatcherService;
    protected final MechanicServiceImpl mechanicService;
    protected final DriverServiceImpl driverService;
    protected final ClientServiceImpl clientService;
    protected final CarServiceImpl carService;

    @Autowired
    public BaseTest(CarServiceImpl carService, DriverServiceImpl driverService, OrderNumberServiceImpl orderNumberService, DispatcherRepository dispatcherRepository, MechanicServiceImpl mechanicService, ClientServiceImpl clientService) {
        this.dispatcherService = new DispatcherServiceImpl(carService, driverService, orderNumberService, dispatcherRepository);
        this.mechanicService = mechanicService;
        this.driverService = driverService;
        this.clientService = clientService;
        this.carService = carService;
    }

    @BeforeEach
    void setUp() throws ValidationDtoException {
        String dayOfWeek = LocalDate.now().getDayOfWeek().toString();
        // ???????? ?????????????????? - 22:30, ?? ?????????????? - 00:30, ???? dispatcherLunch ???????????????? ??????????????????
        String startLunch = DateUtil.convertFromLocalDateTimeToString(LocalDateTime.now().minusHours(1));
        String endLunch = DateUtil.convertFromLocalDateTimeToString(LocalDateTime.now().plusMinutes(7));

        DispatcherDto dispatcherDayoff = dispatcherService.findById(1L);
        dispatcherDayoff.setDayoff(dayOfWeek);
        dispatcherDayoff.setWorkStatus(false);
        dispatcherService.save(dispatcherDayoff);

        DispatcherDto dispatcherLunch = dispatcherService.findById(2L);
        dispatcherLunch.setStartLunch(startLunch);
        dispatcherLunch.setEndLunch(endLunch);
        dispatcherLunch.setWorkStatus(true);
        dispatcherService.save(dispatcherLunch);

        DriverDto driverDayoff = driverService.findById(1L);
        driverDayoff.setDayoff(dayOfWeek);
        driverDayoff.setWorkStatus(false);
        driverService.save(driverDayoff);
    }

    @AfterEach
    void tearDown() {
    }
}
