package elp.max.e.taxistation.repository;

import elp.max.e.taxistation.model.DispatcherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DispatcherRepository extends JpaRepository<DispatcherEntity, Long> {
}
