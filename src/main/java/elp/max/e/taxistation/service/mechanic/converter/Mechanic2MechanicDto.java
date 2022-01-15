package elp.max.e.taxistation.service.mechanic.converter;

import elp.max.e.taxistation.dto.MechanicDto;
import elp.max.e.taxistation.model.MechanicEntity;

public class Mechanic2MechanicDto {

    public static MechanicDto convert(MechanicEntity mechanicEntity) {
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
