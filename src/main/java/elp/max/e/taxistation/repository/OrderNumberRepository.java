package elp.max.e.taxistation.repository;

import elp.max.e.taxistation.model.OrderNumberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderNumberRepository extends JpaRepository<OrderNumberEntity, Long> {
}
