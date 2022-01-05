package elp.max.e.taxistation.repository;

import elp.max.e.taxistation.model.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, Long> {
    CarEntity findByNumberCar(String number);
}
