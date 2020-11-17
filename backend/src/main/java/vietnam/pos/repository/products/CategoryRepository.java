package vietnam.pos.repository.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vietnam.pos.models.products.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}