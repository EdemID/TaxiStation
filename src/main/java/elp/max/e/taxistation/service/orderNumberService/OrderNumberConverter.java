package elp.max.e.taxistation.service.orderNumberService;

import elp.max.e.taxistation.dto.OrderNumberDto;
import elp.max.e.taxistation.model.OrderNumberEntity;

public class OrderNumberConverter {

    public static OrderNumberEntity fromOrderNumberDtoToOrderNumberEntity(OrderNumberDto orderNumberDto) {
        OrderNumberEntity orderNumberEntity = new OrderNumberEntity();
        orderNumberEntity.setId(orderNumberDto.getId());
        orderNumberEntity.setNumber(orderNumberDto.getNumber());
        orderNumberEntity.setClient(orderNumberDto.getClient());
        orderNumberEntity.setDispatcher(orderNumberDto.getDispatcher());
        orderNumberEntity.setDriver(orderNumberDto.getDriver());
        orderNumberEntity.setCar(orderNumberDto.getCar());
        return orderNumberEntity;
    }

    public static OrderNumberDto fromOrderNumberEntityToOrderNumberDto(OrderNumberEntity orderNumberEntity) {
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
