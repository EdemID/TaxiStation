package elp.max.e.taxistation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MechanicDto {

    private Long id;
    private Long repairTime;
    private Integer resource;
    private boolean busy;
    private List<CarDto> brokenCars;
}
