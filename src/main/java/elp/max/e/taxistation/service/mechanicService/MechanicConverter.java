package elp.max.e.taxistation.service.mechanicService;

import elp.max.e.taxistation.dto.MechanicDto;
import elp.max.e.taxistation.model.MechanicEntity;

public class MechanicConverter {

    public static MechanicEntity fromMechanicDtoToMechanicEntity(MechanicDto mechanicDto) {
        MechanicEntity mechanicEntity = new MechanicEntity();
        mechanicEntity.setId(mechanicDto.getId());
        mechanicEntity.setRepairTime(mechanicDto.getRepairTime());
        mechanicEntity.setResource(mechanicDto.getResource());
        mechanicEntity.setBusy(mechanicDto.isBusy());
//        mechanicEntity.setBrokenCars(mechanicDto.getBrokenCars().stream()
//                .map(CarConverter::fromCarDtoToCarEntity)
//                .collect(Collectors.toList()));
        return mechanicEntity;
    }

    public static MechanicDto fromMechanicEntityToMechanicDto(MechanicEntity mechanicEntity) {
        MechanicDto mechanicDto = new MechanicDto();
        mechanicDto.setId(mechanicEntity.getId());
        mechanicDto.setRepairTime(mechanicEntity.getRepairTime());
        mechanicDto.setResource(mechanicEntity.getResource());
        mechanicDto.setBusy(mechanicEntity.isBusy());
//        mechanicDto.setBrokenCars(mechanicEntity.getBrokenCars().stream()
//                .map(CarConverter::fromCarEntityToCarDto)
//                .collect(Collectors.toList()));
        return mechanicDto;
    }
}
