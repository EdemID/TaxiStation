package elp.max.e.taxistation.controller;

import elp.max.e.taxistation.dto.OrderNumberDto;
import elp.max.e.taxistation.service.clientService.ClientServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class ClientControllerTest {

    private final ClientController clientController;
    private final ClientServiceImpl clientService;

    @Autowired
    ClientControllerTest(ClientController clientController, ClientServiceImpl clientService) {
        this.clientController = clientController;
        this.clientService = clientService;
    }

    @Test
    void call() throws Exception {
        OrderNumberDto actualOrderNumber = clientController.call(1L);
        String expectedClientName = clientService.findById(1L).getName();
        assertEquals(expectedClientName, actualOrderNumber.getClient());

        OrderNumberDto actualOrderNumberSecond = clientController.call(2L);
        String expectedClientNameSecond = clientService.findById(2L).getName();
        assertEquals(expectedClientNameSecond, actualOrderNumberSecond.getClient());
    }
}