package elp.max.e.taxistation.dto;

public class DriverDto {

    private Long id;
    private String name;
    private String dayoff;
    private String car;
    private boolean workStatus;
    private boolean busy;

    public DriverDto() {
    }

    public DriverDto(Long id, String name, String dayoff, String car, boolean workStatus, boolean busy) {
        this.id = id;
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

    public void setWorkStatus(boolean workStatus) {
        this.workStatus = workStatus;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }
}
