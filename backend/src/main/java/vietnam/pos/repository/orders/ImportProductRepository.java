package vietnam.pos.repository.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vietnam.pos.models.orders.ImportProduct;
import vietnam.pos.models.orders.ImportProductKey;

@Repository
public interface ImportProductRepository extends JpaRepository<ImportProduct, ImportProductKey> {
}
