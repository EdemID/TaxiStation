package elp.max.e.taxistation.service.client;

import elp.max.e.taxistation.dto.ClientDto;
import elp.max.e.taxistation.dto.DispatcherDto;
import elp.max.e.taxistation.dto.OrderNumberDto;
import elp.max.e.taxistation.exception.CallBeforeCompletionOfOrderException;
import elp.max.e.taxistation.exception.EntityNotFoundException;
import elp.max.e.taxistation.exception.ValidationDtoException;
import elp.max.e.taxistation.model.ClientEntity;
import elp.max.e.taxistation.repository.ClientRepository;
import elp.max.e.taxistation.service.ServiceInterface;
import elp.max.e.taxistation.service.client.converter.Client2ClientDto;
import elp.max.e.taxistation.service.client.converter.ClientDto2Client;
import elp.max.e.taxistation.service.dispatcher.DispatcherServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@org.springframework.stereotype.Service
public class ClientServiceImpl implements ServiceInterface<ClientDto> {

    private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

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
        logger.info("Find all clients.");
        return clientRepository.findAll()
                .stream()
                .map(Client2ClientDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDto findById(Long clientId) throws EntityNotFoundException {
        logger.info("Client search by id.");
        ClientEntity clientEntity;
        if (clientRepository.findById(clientId).isPresent()) {
            logger.info("Client with id={} found!", clientId);
            clientEntity = clientRepository.findById(clientId).get();
        } else {
            logger.info("Client with id={} not found!", clientId);
            throw new EntityNotFoundException("Клиент " + clientId + " не найден!");
        }
        return Client2ClientDto.convert(clientEntity);
    }

    @Transactional
    public ClientDto save(ClientDto dto) throws ValidationDtoException {
        validateDto(dto);
        ClientEntity clientEntity = clientRepository.save(ClientDto2Client.convert(dto));
        logger.info("Save the client.");
        logger.info("Client saved: {}!", dto);
        return Client2ClientDto.convert(clientEntity);
    }

    @Override
    @Transactional
    public ClientDto update(Long id, ClientDto dto) throws ValidationDtoException {
        validateDto(dto);
        ClientEntity clientEntity = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Клиент " + dto + " не найден!"));
        clientEntity.setName(dto.getName());
        clientEntity.setOrderNumber(dto.getOrderNumber());
        clientEntity = clientRepository.save(clientEntity);
        logger.info("Update the client with id: {}.", id);
        logger.info("Client updated: {}!", dto);
        return Client2ClientDto.convert(clientEntity);
    }

    @Transactional
    public void delete(Long id) {
    }

    public OrderNumberDto call(Long id) throws Exception {
        logger.info("Taxi call.");
        logger.info("Taxi call for client with id: {}!", id);
        //для гет-запроса, для пост-запроса возможно будет передаваться в параметрах
        ClientDto clientDto = findById(id);

        if (!clientDto.getOrderNumber().equals("No order")) {
            logger.info("The client called a taxi before completion. His order: {}", clientDto.getOrderNumber());
            throw new CallBeforeCompletionOfOrderException(clientDto.getOrderNumber());
        }

        DispatcherDto dispatcherDto = dispatcherService.getWorkingDispatcher();
        OrderNumberDto orderNumberDto = dispatcherService.assignCarToDriverAndCallClient(clientDto, dispatcherDto, this);
        clientDto.setOrderNumber(orderNumberDto.getNumber());
        save(clientDto);

        logger.info(
                "Order data:" + System.lineSeparator() +
                "-- Order id: {}" + System.lineSeparator() +
                "-- Order number: {}" + System.lineSeparator() +
                "-- Client name: {}" + System.lineSeparator() +
                "-- Dispatcher name: {}" + System.lineSeparator() +
                "-- Driver name: {}" + System.lineSeparator() +
                "-- Car number: {}" + System.lineSeparator(),
                orderNumberDto.getId(),
                orderNumberDto.getNumber(),
                orderNumberDto.getClient(),
                orderNumberDto.getDispatcher(),
                orderNumberDto.getDriver(),
                orderNumberDto.getCar()
        );
        return orderNumberDto;
    }

    @Override
    public void validateDto(ClientDto dto) throws ValidationDtoException {
        logger.info("Client validation: {}.", dto);
        if (isNull(dto)) {
            logger.info("Validation failed: {}!", "Client is null");
            throw new ValidationDtoException("Client is null");
        }
        if (isNull(dto.getName()) || dto.getName().isEmpty()) {
            logger.info("Validation failed: {}!", "Client name is empty");
            throw new ValidationDtoException("Client name is empty");
        }
    }
}
