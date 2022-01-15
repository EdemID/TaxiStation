package elp.max.e.taxistation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "dispatcher")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DispatcherEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String dayoff;
    @Column(name = "start_lunch")
    private String startLunch;
    @Column(name = "end_lunch")
    private String endLunch;
    @Column(name = "workstatus", columnDefinition = "boolean DEFAULT true")
    private boolean workStatus;
}