package elp.max.e.taxistation.controller;

import elp.max.e.taxistation.dto.ClientDto;
import elp.max.e.taxistation.dto.OrderNumberDto;
import elp.max.e.taxistation.exception.ValidationDtoException;
import elp.max.e.taxistation.service.client.ClientServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    private final ClientServiceImpl clientService;

    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ClientDto create(@RequestBody ClientDto clientDto) throws ValidationDtoException {
        logger.info("Get-request received with clientDto: {}", clientDto);
        ClientDto savedClientDto = clientService.save(clientDto);
        logger.info("The result is returned: {}", savedClientDto);
        return savedClientDto;
    }

    @PatchMapping(value = "/update/{clientId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ClientDto update(@PathVariable Long clientId, @RequestBody ClientDto clientDto) throws ValidationDtoException {
        logger.info("Get-request received with" + System.lineSeparator() +
                     "clientId: {}" + System.lineSeparator() +
                     "clientDto: {}", clientId, clientDto);
        ClientDto updatedClientDto = clientService.update(clientId, clientDto);
        logger.info("The result is returned: {}", updatedClientDto);
        return updatedClientDto;
    }

    @GetMapping(value = "/all")
    public List<ClientDto> findAll() {
        logger.info("Get-request 'findAll()' received");
        List<ClientDto> clientDtos = clientService.findAll();
        logger.info("The result is returned: {}", clientDtos);
        return clientDtos;
    }

    @GetMapping(value = "/{clientId}", produces = APPLICATION_JSON_VALUE)
    public ClientDto findById(@PathVariable Long clientId) {
        logger.info("Get-request received with clientId: {}", clientId);
        ClientDto clientDto = clientService.findById(clientId);
        logger.info("The result is returned: {}", clientDto);
        return clientDto;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(produces = APPLICATION_JSON_VALUE)
    public void delete(@RequestParam Long clientId) {
        clientService.delete(clientId);
    }

    /*
    @PostMapping(value = "/{clientId}/call")
    public String call(@PathVariable Long clientId, @RequestBody ClientDto clientDto) throws Exception {
        return clientService.call(clientId, clientDto);
    }
     */

    @GetMapping(value = "/{clientId}/call", produces = APPLICATION_JSON_VALUE)
    public OrderNumberDto call(@PathVariable Long clientId) throws Exception {
        logger.info("Get-request received with clientId: {}", clientId);
        OrderNumberDto orderNumberDto = clientService.call(clientId);
        logger.info("The result is returned: {}", orderNumberDto);
        return orderNumberDto;
    }

    /*
    Map<String, Object> response = new HashMap<>();
response.put("message", "Test data");
response.put("number", 1)
return ResponseEntity.ok(response);
     */
}
