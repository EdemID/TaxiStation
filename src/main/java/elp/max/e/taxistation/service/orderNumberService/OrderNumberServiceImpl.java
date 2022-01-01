package elp.max.e.taxistation.service.orderNumberService;

import elp.max.e.taxistation.dto.OrderNumberDto;
import elp.max.e.taxistation.model.OrderNumberEntity;
import elp.max.e.taxistation.repository.OrderNumberRepository;
import elp.max.e.taxistation.service.ServiceInterface;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
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
    public OrderNumberDto save(OrderNumberDto dto) throws ValidationException {
        validateOrderNumberDto(dto);
        OrderNumberEntity orderNumberEntity = orderNumberRepository.save(OrderNumberConverter.fromOrderNumberDtoToOrderNumberEntity(dto));
        return OrderNumberConverter.fromOrderNumberEntityToOrderNumberDto(orderNumberEntity);
    }

    @Override
    public void delete(Long id) {

    }

    private void validateOrderNumberDto(OrderNumberDto dto) throws ValidationException {
        if (isNull(dto)) {
            throw new ValidationException("Object client is null");
        }
        if (isNull(dto.getNumber()) || dto.getNumber().isEmpty()) {
            throw new ValidationException("Number is empty");
        }
    }
}
