package elp.max.e.taxistation.service.clientService;

import elp.max.e.taxistation.dto.ClientDto;
import elp.max.e.taxistation.model.ClientEntity;

public class ClientConverter {

    public static ClientEntity fromClientDtoToClientEntity(ClientDto clientDto) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(clientDto.getId());
        clientEntity.setName(clientDto.getName());
        clientEntity.setOrderNumber(clientDto.getOrderNumber());
        return clientEntity;
    }

    public static ClientDto fromClientEntityToClientDto(ClientEntity clientEntity) {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(clientEntity.getId());
        clientDto.setName(clientEntity.getName());
        clientDto.setOrderNumber(clientEntity.getOrderNumber());
        return clientDto;
    }
}
