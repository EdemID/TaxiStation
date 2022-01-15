package elp.max.e.taxistation.service.client.converter;

import elp.max.e.taxistation.dto.ClientDto;
import elp.max.e.taxistation.model.ClientEntity;

public class Client2ClientDto {

    public static ClientDto convert(ClientEntity clientEntity) {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(clientEntity.getId());
        clientDto.setName(clientEntity.getName());
        clientDto.setOrderNumber(clientEntity.getOrderNumber());
        return clientDto;
    }
}
