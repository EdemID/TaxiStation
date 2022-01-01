package elp.max.e.taxistation.repository;

import elp.max.e.taxistation.model.MechanicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MechanicRepository extends JpaRepository<MechanicEntity, Long> {
}
