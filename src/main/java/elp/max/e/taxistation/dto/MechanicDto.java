package elp.max.e.taxistation.dto;

import java.util.List;

public class MechanicDto {

    private Long id;
    private String carBeingRepaired;
    private Long repairTime;
    private List<CarDto> brokenCars;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCarBeingRepaired() {
        return carBeingRepaired;
    }

    public void setCarBeingRepaired(String carBeingRepaired) {
        this.carBeingRepaired = carBeingRepaired;
    }

    public Long getRepairTime() {
        return repairTime;
    }

    public void setRepairTime(Long repairTime) {
        this.repairTime = repairTime;
    }

    public List<CarDto> getBrokenCars() {
        return brokenCars;
    }

    public void setBrokenCars(List<CarDto> brokenCars) {
        this.brokenCars = brokenCars;
    }
}
