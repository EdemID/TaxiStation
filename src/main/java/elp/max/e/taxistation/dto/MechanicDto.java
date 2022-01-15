package elp.max.e.taxistation.dto;

import java.util.List;

public class MechanicDto {

    private Long id;
    private Long repairTime;
    private Integer resource;
    private boolean busy;
    private List<CarDto> brokenCars;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRepairTime() {
        return repairTime;
    }

    public void setRepairTime(Long repairTime) {
        this.repairTime = repairTime;
    }

    public Integer getResource() {
        return resource;
    }

    public void setResource(Integer resource) {
        this.resource = resource;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public List<CarDto> getBrokenCars() {
        return brokenCars;
    }

    public void setBrokenCars(List<CarDto> brokenCars) {
        this.brokenCars = brokenCars;
    }

    @Override
    public String toString() {
        return "MechanicDto{" +
                "id=" + id +
                ", repairTime=" + repairTime +
                ", resource=" + resource +
                ", busy=" + busy +
                ", brokenCars=" + brokenCars +
                '}';
    }
}
