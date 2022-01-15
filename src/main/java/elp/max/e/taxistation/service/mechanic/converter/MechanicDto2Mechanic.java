package elp.max.e.taxistation.service.mechanic.converter;

import elp.max.e.taxistation.dto.MechanicDto;
import elp.max.e.taxistation.model.MechanicEntity;

public class MechanicDto2Mechanic {

    public static MechanicEntity convert(MechanicDto mechanicDto) {
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
}
