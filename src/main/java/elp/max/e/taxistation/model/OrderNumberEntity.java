package elp.max.e.taxistation.model;

import javax.persistence.*;

@Entity
@Table(name = "order_number")
public class OrderNumberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;
    private String client;
    private String dispatcher;
    private String driver;
    private String car;


    public OrderNumberEntity() {
    }

    public OrderNumberEntity(String number, String client, String dispatcher, String driver, String car) {
        this.number = number;
        this.client = client;
        this.dispatcher = dispatcher;
        this.driver = driver;
        this.car = car;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(String dispatcher) {
        this.dispatcher = dispatcher;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }
}
