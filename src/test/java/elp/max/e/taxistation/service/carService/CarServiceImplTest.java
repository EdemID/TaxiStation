package elp.max.e.taxistation.service.carService;

import elp.max.e.taxistation.BaseTest;
import elp.max.e.taxistation.dto.CarDto;
import elp.max.e.taxistation.repository.DispatcherRepository;
import elp.max.e.taxistation.service.clientService.ClientServiceImpl;
import elp.max.e.taxistation.service.driverService.DriverServiceImpl;
import elp.max.e.taxistation.service.mechanicService.MechanicServiceImpl;
import elp.max.e.taxistation.service.orderNumberService.OrderNumberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CarServiceImplTest extends BaseTest {

    @Autowired
    public CarServiceImplTest(CarServiceImpl carService, DriverServiceImpl driverService, OrderNumberServiceImpl orderNumberService, DispatcherRepository dispatcherRepository, MechanicServiceImpl mechanicService, ClientServiceImpl clientService) {
        super(carService, driverService, orderNumberService, dispatcherRepository, mechanicService, clientService);
    }

    @Test
    @Sql({"/data/import_positive_data.sql"})
    @DisplayName("Проверить рабочий автомобиль")
    void getWorkerCar() {
        CarDto workerCar = carService.getWorkerCar();
        assertFalse(workerCar.isBusy());
    }

    @Test
    @Sql({"/data/import_positive_data.sql"})
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