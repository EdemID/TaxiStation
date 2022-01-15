package elp.max.e.taxistation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderNumberDto {

    private Long id;
    private String number;
    private String client;
    private String dispatcher;
    private String driver;
    private String car;
}
