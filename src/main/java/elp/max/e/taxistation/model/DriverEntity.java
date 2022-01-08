package elp.max.e.taxistation.model;

import javax.persistence.*;

@Entity
@Table(name = "driver")
public class DriverEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String dayoff;
    private String car;
    @Column(name = "workstatus", columnDefinition = "boolean DEFAULT true")
    private boolean workStatus;
    @Column(columnDefinition = "boolean DEFAULT false")
    private boolean busy;

    public DriverEntity() {
    }

    public DriverEntity(String name, String dayoff, String car, boolean workStatus, boolean busy) {
        this.name = name;
        this.dayoff = dayoff;
        this.car = car;
        this.workStatus = workStatus;
        this.busy = busy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDayoff() {
        return dayoff;
    }

    public void setDayoff(String dayoff) {
        this.dayoff = dayoff;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public boolean isWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(boolean status) {
        this.workStatus = status;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }
}
