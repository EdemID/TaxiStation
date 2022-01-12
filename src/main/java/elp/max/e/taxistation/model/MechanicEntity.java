package elp.max.e.taxistation.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "service")
public class MechanicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // в бд repair_time, так и остался varchar - как изменить тип столбца с элементами?
    @Column(name = "repair_time")
    private Long repairTime;
    @Column(name = "resource", columnDefinition = "integer DEFAULT 5")
    private Integer resource;
    @Column(name = "busy", columnDefinition = "boolean DEFAULT false")
    private boolean busy;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mechanicEntity")
    private List<CarEntity> brokenCars;

    public MechanicEntity() {
    }

    public MechanicEntity(Long repairTime, Integer resource, boolean busy) {
        this.repairTime = repairTime;
        this.resource = resource;
        this.busy = busy;
    }

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

    public List<CarEntity> getBrokenCars() {
        return brokenCars;
    }

    public void setBrokenCars(List<CarEntity> brokenCars) {
        this.brokenCars = brokenCars;
    }
}
