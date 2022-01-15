package elp.max.e.taxistation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "service")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MechanicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // в бд repair_time, так и остался varchar - как изменить тип столбца с элементами?
    @Column(name = "repair_time")
    private Long repairTime;
    @Column(name = "resource", columnDefinition = "integer DEFAULT 5")
    private Integer resource;
    @Column(name = "busy", columnDefinition = "boolean DEFAULT false")
    private boolean busy;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mechanicEntity")
    private List<CarEntity> brokenCars;
}