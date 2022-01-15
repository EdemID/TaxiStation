package elp.max.e.taxistation.service.client.converter;

import elp.max.e.taxistation.dto.ClientDto;
import elp.max.e.taxistation.model.ClientEntity;

public class ClientDto2Client {

    public static ClientEntity convert(ClientDto clientDto) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(clientDto.getId());
        clientEntity.setName(clientDto.getName());
        clientEntity.setOrderNumber(clientDto.getOrderNumber());
        return clientEntity;
    }
}
