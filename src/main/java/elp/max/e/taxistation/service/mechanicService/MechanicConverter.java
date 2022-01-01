package elp.max.e.taxistation.service.mechanicService;

import elp.max.e.taxistation.dto.MechanicDto;
import elp.max.e.taxistation.model.MechanicEntity;
import elp.max.e.taxistation.service.carService.CarConverter;

import java.util.stream.Collectors;

public class MechanicConverter {

    public static MechanicEntity fromMechanicDtoToMechanicEntity(MechanicDto mechanicDto) {
        MechanicEntity mechanicEntity = new MechanicEntity();
        mechanicEntity.setId(mechanicDto.getId());
        mechanicEntity.setCarBeingRepaired(mechanicDto.getCarBeingRepaired());
        mechanicEntity.setRepairTime(mechanicDto.getRepairTime());
        mechanicEntity.setBrokenCars(mechanicDto.getBrokenCars().stream()
                .map(CarConverter::fromCarDtoToCarEntity)
                .collect(Collectors.toList()));
        return mechanicEntity;
    }

    public static MechanicDto fromMechanicEntityToMechanicDto(MechanicEntity mechanicEntity) {
        MechanicDto mechanicDto = new MechanicDto();
        mechanicDto.setId(mechanicEntity.getId());
        mechanicDto.setCarBeingRepaired(mechanicEntity.getCarBeingRepaired());
        mechanicDto.setRepairTime(mechanicEntity.getRepairTime());
/*
        List<CarEntity> carEntityList = mechanicEntity.getBrokenCars();
        System.out.println("размер carEntityList " + carEntityList.size());
        List<CarDto> carDtos = new ArrayList<>();
        for (CarEntity carEntity : carEntityList) {
            System.out.println("1");
            carDtos.add(CarConverter.fromCarEntityToCarDto(carEntity));
        }
        mechanicDto.setBrokenCars(carDtos);
*/
        mechanicDto.setBrokenCars(mechanicEntity.getBrokenCars().stream()
                .map(CarConverter::fromCarEntityToCarDto)
                .collect(Collectors.toList()));
        return mechanicDto;
    }
}
