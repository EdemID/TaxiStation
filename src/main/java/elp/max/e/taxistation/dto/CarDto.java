package elp.max.e.taxistation.dto;

public class CarDto {

    private Long id;
    private String numberCar;
    private Integer resource;
    private boolean busy;

    public CarDto() {
    }

    public CarDto(Long id, String numberCar, Integer resource, boolean busy) {
        this.id = id;
        this.numberCar = numberCar;
        this.resource = resource;
        this.busy = busy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "CarDto{" +
                "id=" + id +
                ", numberCar='" + numberCar + '\'' +
                ", resource=" + resource +
                ", busy=" + busy +
                '}';
    }
}
