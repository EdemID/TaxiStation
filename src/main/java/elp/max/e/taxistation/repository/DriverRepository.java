package elp.max.e.taxistation.repository;

import elp.max.e.taxistation.model.DriverEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<DriverEntity, Long> {
}
