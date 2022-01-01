package elp.max.e.taxistation.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "service")
public class MechanicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String carBeingRepaired;
    @Column(name = "repair_time")
    private String repairTime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mechanicEntity")
    private List<CarEntity> brokenCars;

    public MechanicEntity() {
    }

    public MechanicEntity(String carBeingRepaired, String repairTime) {
        this.carBeingRepaired = carBeingRepaired;
        this.repairTime = repairTime;
    }

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

    public String getRepairTime() {
        return repairTime;
    }

    public void setRepairTime(String repairTime) {
        this.repairTime = repairTime;
    }

    public List<CarEntity> getBrokenCars() {
        return brokenCars;
    }

    public void setBrokenCars(List<CarEntity> brokenCars) {
        this.brokenCars = brokenCars;
    }
}
