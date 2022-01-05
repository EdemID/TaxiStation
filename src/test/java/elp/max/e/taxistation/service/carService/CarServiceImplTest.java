package elp.max.e.taxistation.service.carService;

import elp.max.e.taxistation.dto.CarDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("CarService test")
@TestPropertySource(locations = "classpath:application-test.properties")
class CarServiceImplTest {

    private final CarServiceImpl carService;

    @Autowired
    CarServiceImplTest(CarServiceImpl carService) {
        this.carService = carService;
    }

    @Test
    @DisplayName("Проверить рабочий автомобиль")
    void getWorkerCar() {
        CarDto workerCar = carService.getWorkerCar();
        assertFalse(workerCar.isBusy());
    }

    @Test
    @DisplayName("Проверить отправку автомобиля на ремонт")
    void sendCarForRepair() {
        List<CarDto> carDtos = carService.findAll();
        for (CarDto carDto : carDtos) {
            if (carDto.getResource() == 0) {
                carService.sendCarForRepair(CarConverter.fromCarDtoToCarEntity(carDto));
                carDto = carService.findById(carDto.getId());
                assertTrue(carDto.isBusy(), "Автомобиль не на ремонте при " + carDto.getResource() + " ресурсе");
            }
        }
    }
}