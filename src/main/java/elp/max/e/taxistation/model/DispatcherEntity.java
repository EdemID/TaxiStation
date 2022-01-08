package elp.max.e.taxistation.model;

import javax.persistence.*;

@Entity
@Table(name = "dispatcher")
public class DispatcherEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String dayoff;
    @Column(name = "start_lunch")
    private String startLunch;
    @Column(name = "end_lunch")
    private String endLunch;
    @Column(name = "workstatus", columnDefinition = "boolean DEFAULT true")
    private boolean workStatus;

    public DispatcherEntity() {
    }

    public DispatcherEntity(String name, String dayoff, String startLunch, String endLunch, boolean workStatus) {
        this.name = name;
        this.dayoff = dayoff;
        this.startLunch = startLunch;
        this.endLunch = endLunch;
        this.workStatus = workStatus;
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

    public String getStartLunch() {
        return startLunch;
    }

    public void setStartLunch(String startLunch) {
        this.startLunch = startLunch;
    }

    public String getEndLunch() {
        return endLunch;
    }

    public void setEndLunch(String endLunch) {
        this.endLunch = endLunch;
    }

    public boolean isWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(boolean status) {
        this.workStatus = status;
    }
}
