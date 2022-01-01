package elp.max.e.taxistation.model;

import javax.persistence.*;

@Entity
@Table(name = "cars")
public class CarEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "number_car")
    private String numberCar;
    private Integer resource;
    private boolean busy;
    @ManyToOne
    @JoinColumn(name = "mechanic_id")
    private MechanicEntity mechanicEntity;

    public CarEntity() {
    }

    public CarEntity(String numberCar, Integer resource, boolean busy) {
        this.numberCar = numberCar;
        this.resource = resource;
        this.busy = busy;
    }

    @Override
    public String toString() {
        return String.format(
                "Car[id=%d, numberCar='%s', resource='%s', busy='%s']",
                id, numberCar, resource, busy);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getNumberCar() {
        return numberCar;
    }

    public void setNumberCar(String numberCar) {
        this.numberCar = numberCar;
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

    public MechanicEntity getMechanicEntity() {
        return mechanicEntity;
    }

    public void setMechanicEntity(MechanicEntity mechanicEntity) {
        this.mechanicEntity = mechanicEntity;
    }
}
