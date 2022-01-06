package elp.max.e.taxistation.service.dispatcherService;

import elp.max.e.taxistation.dto.*;
import elp.max.e.taxistation.repository.DispatcherRepository;
import elp.max.e.taxistation.service.carService.CarServiceImpl;
import elp.max.e.taxistation.service.driverService.DriverServiceImpl;
import elp.max.e.taxistation.service.mechanicService.MechanicServiceImpl;
import elp.max.e.taxistation.service.orderNumberService.OrderNumberServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("DispatcherService test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class DispatcherServiceImplTest {

    private final DispatcherServiceImpl dispatcherService;
    private final MechanicServiceImpl mechanicService;
    private final DriverServiceImpl driverService;
    private final CarServiceImpl carService;

    @Autowired
    public DispatcherServiceImplTest(CarServiceImpl carService, DriverServiceImpl driverService, OrderNumberServiceImpl orderNumberService, DispatcherRepository dispatcherRepository, MechanicServiceImpl mechanicService) {
        this.dispatcherService = new DispatcherServiceImpl(carService, driverService, orderNumberService, dispatcherRepository);
        this.mechanicService = mechanicService;
        this.driverService = driverService;
        this.carService = carService;
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    // как в тестах обновить на нужные значения сущностей
    @Order(1)
    @Test
    @DisplayName("Проверить рабочего диспетчера")
    void getWorkerDispatcher() {
        DispatcherDto workerDispatcher = dispatcherService.getWorkerDispatcher();
        DispatcherDto expected = dispatcherService.findById(workerDispatcher.getId());
        assertTrue(isMakeEqual(expected, workerDispatcher));
        assertTrue(workerDispatcher.isWorkStatus(), "Диспетчер " + workerDispatcher + " должен работать, но НЕ работает");
    }

    @Order(3)
    @Test
    @DisplayName("Проверить смену статусов водителя и автомобиля после заказа")
    void releaseDriverAndCarAfterOrdering() {
        DriverDto driverDto = driverService.findById(1L);
        CarDto carDto = carService.findById(1L);

        long currentTime = new Date().getTime();
        long orderTime = 20000L;
        long endTimer = currentTime + orderTime + 1000L;
        dispatcherService.releaseDriverAndCarAfterOrdering(driverDto, carDto, orderTime);

        while (endTimer >= System.currentTimeMillis()){
            if (endTimer == System.currentTimeMillis()) {
                assertFalse(driverDto.isBusy());
                assertFalse(carDto.isBusy());
                assertEquals("free", driverDto.getCar());
                break;
            }
        }
    }

    @Order(4)
    @Test
    @DisplayName("Проверить создание и содержание наряд-заказа")
    void assignCarToDriverAndCallClient() throws Exception {
        // данные для теста лучше создавать или брать из бд? если из бд, то нужно инжектить Сервисы других (отличных от тестируемой) сущностей в тесте?
        // в автотестах создавали данные руками, но в специальном классе TestRunData
        // здесь создаю клиента и диспетчера, так как в проверяемом методе нет их взаимодействия с бд
        ClientDto clientDto = new ClientDto(1L, "Tom", "No order");
        DispatcherDto dispatcherDto =
                new DispatcherDto(1L, "Vladimir", "dayOff", "startLunch", "endLunch", true);

        OrderNumberDto orderNumberDto = dispatcherService.assignCarToDriverAndCallClient(clientDto, dispatcherDto);
        String orderNumber = orderNumberDto.getNumber();
        String client = orderNumberDto.getClient();
        String dispatcher = orderNumberDto.getDispatcher();
        String car = orderNumberDto.getCar();
        String driver = orderNumberDto.getDriver();

        assertEquals("Tom", client, "Назначен неверный клиент: " + client);
        assertEquals("Vladimir", dispatcher, "Назначен неверный диспетчер: " + dispatcher);
        assertEquals(orderNumber, orderNumber, "Назначен неверный номер наряд-заказа: " + orderNumber);
        assertEquals("good luck-car", car, "Назначен неверный автомобиль: " + car);
        assertEquals("Aurora", driver, "Назначен неверный водитель: " + driver);
    }

    @Order(5)
    @Test
    @DisplayName("Проверить отправку автомобиля на ремонт после заказа, если ресурс равен 0, и его починку")
    void sendCarForRepairIfResourceIsZeroAfterOrder() throws Exception {
        ClientDto clientDto = new ClientDto(1L, "Tom", "No order");
        DispatcherDto dispatcherDto =
                new DispatcherDto(1L, "Vladimir", "dayOff", "startLunch", "endLunch", true);
        MechanicDto mechanicDto = mechanicService.findById(1L);

        OrderNumberDto orderNumberDto = dispatcherService.assignCarToDriverAndCallClient(clientDto, dispatcherDto);
        String numberCar = orderNumberDto.getCar();
        CarDto carDto = carService.findByNumberCar(numberCar);
        System.out.println(carDto);

        if (carDto.getResource() == 0) {
            assertEquals(numberCar, carDto.getNumberCar(), "Назначен неверный автомобиль: " + numberCar);
            int recoveredResource = mechanicDto.getResource();
            long repairTime = mechanicDto.getRepairTime();
            long timeAfterRepair = System.currentTimeMillis() + repairTime + 1000L;
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
