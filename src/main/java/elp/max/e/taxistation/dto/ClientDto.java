package elp.max.e.taxistation.dto;

public class ClientDto {

    private Long id;
    private String name;
    private String orderNumber;

    public ClientDto() {

    }

    public ClientDto(Long id, String name, String orderNumber) {
        this.id = id;
        this.name = name;
        this.orderNumber = orderNumber;
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

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public String toString() {
        return "ClientDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                '}';
    }
}
