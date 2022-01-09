package elp.max.e.taxistation.service.clientService;

import elp.max.e.taxistation.dto.ClientDto;
import elp.max.e.taxistation.dto.DispatcherDto;
import elp.max.e.taxistation.dto.OrderNumberDto;
import elp.max.e.taxistation.model.ClientEntity;
import elp.max.e.taxistation.repository.ClientRepository;
import elp.max.e.taxistation.service.ServiceInterface;
import elp.max.e.taxistation.service.dispatcherService.DispatcherServiceImpl;
import elp.max.e.taxistation.utils.DtoNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@org.springframework.stereotype.Service
public class ClientServiceImpl implements ServiceInterface<ClientDto> {

    private final ClientRepository clientRepository;
    private final DispatcherServiceImpl dispatcherService;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository,
                             DispatcherServiceImpl dispatcherService) {
        this.clientRepository = clientRepository;
        this.dispatcherService = dispatcherService;
    }

    @Override
    @Transactional
    public List<ClientDto> findAll() {
        return clientRepository.findAll()
                .stream()
                .map(ClientConverter::fromClientEntityToClientDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDto findById(Long clientId) throws EntityNotFoundException {
        ClientEntity clientEntity;
        if (clientRepository.findById(clientId).isPresent()) {
            clientEntity = clientRepository.findById(clientId).get();
        } else {
            throw new EntityNotFoundException("Клиент " + clientId + " не найден!");
        }
        return ClientConverter.fromClientEntityToClientDto(clientEntity);
    }

    @Transactional
    public ClientDto save(ClientDto dto) throws ValidationException {
        validateDto(dto);
        ClientEntity clientEntity = clientRepository.save(ClientConverter.fromClientDtoToClientEntity(dto));
        return ClientConverter.fromClientEntityToClientDto(clientEntity);
    }

    @Override
    @Transactional
    public ClientDto update(Long id, ClientDto dto) throws ValidationException {
        validateDto(dto);
        ClientEntity clientEntity = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Клиент " + dto + " не найден!"));
        clientEntity.setName(dto.getName());
        clientEntity = clientRepository.save(clientEntity);
        return ClientConverter.fromClientEntityToClientDto(clientEntity);
    }

    @Transactional
    public void delete(Long id) {
    }

    public OrderNumberDto call(Long id) throws Exception {

        //для гет-запроса, для пост-запроса возможно будет передаваться в параметрах
        ClientDto clientDto = findById(id);


        DispatcherDto dispatcherDto = dispatcherService.getWorkerDispatcher();
        if (dispatcherDto == null) {
            throw new DtoNotFoundException("Диспетчер");
        }

        OrderNumberDto orderNumberDto = dispatcherService.assignCarToDriverAndCallClient(clientDto, dispatcherDto);
        if (orderNumberDto == null) {
            throw new DtoNotFoundException("Наряд-заказ");
        }

        // в диспетчере не сможем добавить clientService, так здесь уже есть dispatcherService, иначе будет цикл бинов
        clientDto.setOrderNumber(orderNumberDto.getNumber());
        save(clientDto);

        /**
         *  данные заказ-наряда
         */
        System.out.println(orderNumberDto.getId());
        System.out.println(orderNumberDto.getNumber());
        System.out.println(orderNumberDto.getClient());
        System.out.println(orderNumberDto.getDispatcher());
        System.out.println(orderNumberDto.getDriver());
        System.out.println(orderNumberDto.getCar());

        return orderNumberDto;
    }

    @Override
    public void validateDto(ClientDto dto) throws ValidationException {
        if (isNull(dto)) {
            throw new ValidationException("Object client is null");
        }
        if (isNull(dto.getName()) || dto.getName().isEmpty()) {
            throw new ValidationException("Name is empty");
        }
    }
}
