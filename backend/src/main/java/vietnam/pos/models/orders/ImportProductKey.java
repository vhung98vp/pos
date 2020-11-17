package vietnam.pos.models.orders;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ImportProductKey implements Serializable {
    @Column(name = "import_id")
    private Long importId;

    @Column(name = "product_id")
    private Long productId;

    public ImportProductKey() {
    }

    public ImportProductKey(Long importId, Long productId) {
        this.importId = importId;
        this.productId = productId;
    }

    public Long getImportId() {
        return importId;
    }

    public void setImportId(Long importId) {
        this.importId = importId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
