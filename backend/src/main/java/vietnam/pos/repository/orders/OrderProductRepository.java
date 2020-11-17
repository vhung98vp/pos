package vietnam.pos.repository.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vietnam.pos.models.orders.OrderProduct;
import vietnam.pos.models.orders.OrderProductKey;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProductKey> {

}
