package elp.max.e.taxistation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "car")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "number_car")
    private String numberCar;
    private Integer resource;
    private boolean busy;
    @ManyToOne
    @JoinColumn(name = "mechanic_id")
    private MechanicEntity mechanicEntity;
}
