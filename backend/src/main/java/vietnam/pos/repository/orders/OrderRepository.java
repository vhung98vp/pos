package vietnam.pos.repository.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vietnam.pos.models.orders.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
