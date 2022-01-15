package elp.max.e.taxistation.service.orderNumber.converter;

import elp.max.e.taxistation.dto.OrderNumberDto;
import elp.max.e.taxistation.model.OrderNumberEntity;

public class Order2OrderDto {

    public static OrderNumberDto convert(OrderNumberEntity orderNumberEntity) {
        OrderNumberDto orderNumberDto = new OrderNumberDto();
        orderNumberDto.setId(orderNumberEntity.getId());
        orderNumberDto.setNumber(orderNumberEntity.getNumber());
        orderNumberDto.setClient(orderNumberEntity.getClient());
        orderNumberDto.setDispatcher(orderNumberEntity.getDispatcher());
        orderNumberDto.setDriver(orderNumberEntity.getDriver());
        orderNumberDto.setCar(orderNumberEntity.getCar());
        return orderNumberDto;
    }
}
