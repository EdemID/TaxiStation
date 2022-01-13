package elp.max.e.taxistation.controller;

import elp.max.e.taxistation.dto.ClientDto;
import elp.max.e.taxistation.dto.OrderNumberDto;
import elp.max.e.taxistation.exception.ValidationDtoException;
import elp.max.e.taxistation.service.clientService.ClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientServiceImpl clientService;

    @Autowired
    public ClientController(ClientServiceImpl clientService) {
        this.clientService = clientService;
    }

    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ClientDto create(@RequestBody ClientDto clientDto) throws ValidationDtoException {
        return clientService.save(clientDto);
    }

    @PatchMapping(value = "/update/{clientId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ClientDto update(@PathVariable Long clientId, @RequestBody ClientDto clientDto) throws ValidationDtoException {
        return clientService.update(clientId, clientDto);
    }

    @GetMapping(value = "/all")
    public List<ClientDto> findAll() {
        return clientService.findAll();
    }

    @GetMapping(value = "/{clientId}", produces = APPLICATION_JSON_VALUE)
    public ClientDto findById(@PathVariable Long clientId) {
        return clientService.findById(clientId);
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
        return clientService.call(clientId);
    }

    /*
    Map<String, Object> response = new HashMap<>();
response.put("message", "Test data");
response.put("number", 1)
return ResponseEntity.ok(response);
     */
}
