package elp.max.e.taxistation.service.mechanicService;

import elp.max.e.taxistation.BaseTest;
import elp.max.e.taxistation.dto.CarDto;
import elp.max.e.taxistation.dto.MechanicDto;
import elp.max.e.taxistation.model.CarEntity;
import elp.max.e.taxistation.repository.DispatcherRepository;
import elp.max.e.taxistation.service.carService.CarConverter;
import elp.max.e.taxistation.service.carService.CarServiceImpl;
import elp.max.e.taxistation.service.clientService.ClientServiceImpl;
import elp.max.e.taxistation.service.driverService.DriverServiceImpl;
import elp.max.e.taxistation.service.orderNumberService.OrderNumberServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import javax.xml.bind.ValidationException;

class MechanicServiceImplTest extends BaseTest {

    @Autowired
    public MechanicServiceImplTest(CarServiceImpl carService, DriverServiceImpl driverService, OrderNumberServiceImpl orderNumberService, DispatcherRepository dispatcherRepository, MechanicServiceImpl mechanicService, ClientServiceImpl clientService) {
        super(carService, driverService, orderNumberService, dispatcherRepository, mechanicService, clientService);
    }

    @Test
    @Sql(value = {"/data/import_positive_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/data/delete_positive_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Проверить починку автомобиля")
    void repairCar() throws ValidationException {
        CarDto carDto = carService.getWorkerCar();
        carDto.setResource(0);
        carService.save(carDto);
        CarEntity carEntity = CarConverter.fromCarDtoToCarEntity(carDto);
        MechanicDto mechanicDto = mechanicService.findById(1L);

        int recoveredResource = mechanicDto.getResource();
        long repairTime = mechanicDto.getRepairTime();
        mechanicService.repairCar(mechanicDto, carEntity, carService);
        long timeAfterRepair = System.currentTimeMillis() + repairTime + 1000L;

        while (timeAfterRepair >= System.currentTimeMillis()) {
            if (timeAfterRepair == System.currentTimeMillis()) {
                carEntity = CarConverter.fromCarDtoToCarEntity(carService.findByNumberCar(carEntity.getNumberCar()));

                Assertions.assertEquals(recoveredResource, carEntity.getResource(), "Механик плохо постаралася: ресурс равен - " + carEntity.getResource() + ", а должен быть - " + recoveredResource);
                Assertions.assertFalse(carEntity.isBusy(), "Автомобиль занят на ремонте: " + carEntity.isBusy());
            }
        }
    }

    @Test
    @Sql(value = {"/data/import_positive_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/data/delete_positive_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Проверить починку двух автомобилей сразу")
    void repairTwoCars() throws ValidationException {
        CarEntity carEntityWithZeroResource = new CarEntity(10L, "add", 0, false);

        CarDto carDto = carService.getWorkerCar();
        carDto.setResource(0);
        carService.save(carDto);
        CarEntity carEntity = CarConverter.fromCarDtoToCarEntity(carDto);
        MechanicDto mechanicDto = mechanicService.findById(1L);

        int recoveredResource = mechanicDto.getResource();
        long repairTime = mechanicDto.getRepairTime();
        mechanicService.repairCar(mechanicDto, carEntity, carService);
        mechanicService.repairCar(mechanicDto, carEntityWithZeroResource, carService);

        long timeAfterRepair = System.currentTimeMillis() + repairTime + 1000L;
        while (timeAfterRepair >= System.currentTimeMillis()) {
            if (timeAfterRepair == System.currentTimeMillis()) {
                carEntity = CarConverter.fromCarDtoToCarEntity(carService.findByNumberCar(carEntity.getNumberCar()));

                Assertions.assertEquals(recoveredResource, carEntity.getResource(), "Механик плохо постаралася: ресурс равен - " + carEntity.getResource() + ", а должен быть - " + recoveredResource);
                Assertions.assertFalse(carEntity.isBusy(), "Автомобиль занят на ремонте: " + carEntity.isBusy());
            }
        }
    }
}