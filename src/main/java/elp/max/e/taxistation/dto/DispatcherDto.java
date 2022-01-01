package elp.max.e.taxistation.dto;

public class DispatcherDto {

    private Long id;
    private String name;
    private String dayoff;
    private String startLunch;
    private String endLunch;
    private boolean workStatus;

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

    public void setWorkStatus(boolean workStatus) {
        this.workStatus = workStatus;
    }
}
