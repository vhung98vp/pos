package vietnam.pos.models.orders;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import vietnam.pos.models.products.Product;

import javax.persistence.*;

@Entity
@Table(name = "import_products")
public class ImportProduct {
    @EmbeddedId
    private ImportProductKey id;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("importId")
    @JoinColumn(name = "import_id")
    private Import imp;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "sell_price", nullable = false)
    private Float sellPrice;

    @Column(name = "quantity", nullable = false)
    private Float quantity;

    public ImportProduct() {
    }

    public ImportProduct(ImportProductKey id, Import imp, Product product, Float sellPrice, Float quantity) {
        this.id = id;
        this.imp = imp;
        this.product = product;
        this.sellPrice = sellPrice;
        this.quantity = quantity;
    }

    public ImportProductKey getId() {
        return id;
    }

    public void setId(ImportProductKey id) {
        this.id = id;
    }

    public Import getImp() {
        return imp;
    }

    public void setImp(Import imp) {
        this.imp = imp;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Float getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Float sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }
}
