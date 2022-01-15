package elp.max.e.taxistation.service.orderNumber.converter;

import elp.max.e.taxistation.dto.OrderNumberDto;
import elp.max.e.taxistation.model.OrderNumberEntity;

public class OrderDto2Order {

    public static OrderNumberEntity convert(OrderNumberDto orderNumberDto) {
        OrderNumberEntity orderNumberEntity = new OrderNumberEntity();
        orderNumberEntity.setId(orderNumberDto.getId());
        orderNumberEntity.setNumber(orderNumberDto.getNumber());
        orderNumberEntity.setClient(orderNumberDto.getClient());
        orderNumberEntity.setDispatcher(orderNumberDto.getDispatcher());
        orderNumberEntity.setDriver(orderNumberDto.getDriver());
        orderNumberEntity.setCar(orderNumberDto.getCar());
        return orderNumberEntity;
    }


}
