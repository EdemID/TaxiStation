package elp.max.e.taxistation.service.car;

import elp.max.e.taxistation.BaseTest;
import elp.max.e.taxistation.dto.CarDto;
import elp.max.e.taxistation.exception.ValidationDtoException;
import elp.max.e.taxistation.repository.DispatcherRepository;
import elp.max.e.taxistation.service.car.converter.CarDto2Car;
import elp.max.e.taxistation.service.client.ClientServiceImpl;
import elp.max.e.taxistation.service.driver.DriverServiceImpl;
import elp.max.e.taxistation.service.mechanic.MechanicServiceImpl;
import elp.max.e.taxistation.service.orderNumber.OrderNumberServiceImpl;
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
    @Sql(value = {"/data/import_positive_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/data/delete_positive_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Проверить рабочий автомобиль")
    void getWorkingCar() throws ValidationDtoException {
        CarDto workingCar = carService.getWorkingCar();
        assertFalse(workingCar.isBusy());
    }

    @Test
    @Sql(value = {"/data/import_positive_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/data/delete_positive_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Проверить отправку автомобиля на ремонт")
    void sendCarForRepair() throws ValidationDtoException {
        List<CarDto> carDtos = carService.findAll();
        for (CarDto carDto : carDtos) {
            if (carDto.getResource() == 0) {
                carService.sendCarForRepair(CarDto2Car.convert(carDto));
                carDto = carService.findById(carDto.getId());
                assertTrue(carDto.isBusy(), "Автомобиль не на ремонте при " + carDto.getResource() + " ресурсе");
            }
        }
    }
}