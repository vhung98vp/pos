package vietnam.pos.repository.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vietnam.pos.models.orders.Import;

@Repository
public interface ImportRepository extends JpaRepository<Import, Long> {
}
