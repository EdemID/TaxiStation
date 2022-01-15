package elp.max.e.taxistation.service.orderNumber;

import elp.max.e.taxistation.dto.OrderNumberDto;
import elp.max.e.taxistation.exception.ValidationDtoException;
import elp.max.e.taxistation.model.OrderNumberEntity;
import elp.max.e.taxistation.repository.OrderNumberRepository;
import elp.max.e.taxistation.service.ServiceInterface;
import elp.max.e.taxistation.service.orderNumber.converter.Order2OrderDto;
import elp.max.e.taxistation.service.orderNumber.converter.OrderDto2Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class OrderNumberServiceImpl implements ServiceInterface<OrderNumberDto> {

    private final OrderNumberRepository orderNumberRepository;

    public OrderNumberServiceImpl(OrderNumberRepository orderNumberRepository) {
        this.orderNumberRepository = orderNumberRepository;
    }

    @Override
    public List<OrderNumberDto> findAll() {
        return null;
    }

    @Override
    @Transactional
    public OrderNumberDto findById(Long id) {
        return null;
    }

    @Override
    public OrderNumberDto save(OrderNumberDto dto) throws ValidationDtoException {
        validateOrderNumberDto(dto);
        OrderNumberEntity orderNumberEntity = orderNumberRepository.save(OrderDto2Order.convert(dto));
        return Order2OrderDto.convert(orderNumberEntity);
    }

    @Override
    @Transactional
    public OrderNumberDto update(Long id, OrderNumberDto dto) throws ValidationDtoException {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    private void validateOrderNumberDto(OrderNumberDto dto) throws ValidationDtoException {
        if (isNull(dto)) {
            throw new ValidationDtoException("Object client is null");
        }
        if (isNull(dto.getNumber()) || dto.getNumber().isEmpty()) {
            throw new ValidationDtoException("Number is empty");
        }
    }

    @Override
    public void validateDto(OrderNumberDto dto) throws ValidationDtoException {

    }
}
