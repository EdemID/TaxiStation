package elp.max.e.taxistation.service.mechanicService;

import elp.max.e.taxistation.dto.MechanicDto;
import elp.max.e.taxistation.model.CarEntity;
import elp.max.e.taxistation.service.carService.CarConverter;
import elp.max.e.taxistation.service.carService.CarServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("MechanicService test")
@TestPropertySource(locations = "classpath:application-test.properties")
class MechanicServiceImplTest {
    private final MechanicServiceImpl mechanicService;
    private final CarServiceImpl carService;

    @Autowired
    public MechanicServiceImplTest(MechanicServiceImpl mechanicService, CarServiceImpl carService) {
        this.mechanicService = mechanicService;
        this.carService = carService;
    }

    @Test
    void repairCar() {
        CarEntity carEntity = CarConverter.fromCarDtoToCarEntity(carService.findById(3L));
        MechanicDto mechanicDto = mechanicService.findById(1L);

        int recoveredResource = mechanicDto.getResource();
        long repairTime = mechanicDto.getRepairTime();
        mechanicService.repairCar(mechanicDto, carEntity, carService);
        long timeAfterRepair = System.currentTimeMillis() + repairTime + 1000L;

        while (timeAfterRepair >= System.currentTimeMillis()) {
            if (timeAfterRepair == System.currentTimeMillis()) {
                carEntity = CarConverter.fromCarDtoToCarEntity(carService.findById(3L));

                Assertions.assertEquals(recoveredResource, carEntity.getResource(), "Механик плохо постаралася: ресурс равен - " + carEntity.getResource() + ", а должен быть - " + recoveredResource);
                Assertions.assertFalse(carEntity.isBusy(), "Автомобиль занят на ремонте: " + carEntity.isBusy());
            }
        }
    }
}