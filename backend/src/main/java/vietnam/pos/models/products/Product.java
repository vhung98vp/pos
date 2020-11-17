package vietnam.pos.models.products;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import vietnam.pos.models.media.Picture;

import javax.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",length = 100, nullable = false)
    private String name;

    @Column(name = "sku", length = 20)
    private String sku;

    @Column(name = "inp_price", nullable = false)
    private Float inpPrice;

    @Column(name = "sell_price", nullable = false)
    private Float sellPrice;

    @Column(name = "quantity", nullable = false)
    private Float quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit", length = 20)
    private EUnit unit;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "picture_id")
    private Picture picture;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;

    public Product() {
    }

    public Product(String name, String sku, Float inpPrice, Float sellPrice, Float quantity, EUnit unit) {
        this.name = name;
        this.sku = sku;
        this.inpPrice = inpPrice;
        this.sellPrice = sellPrice;
        this.quantity = quantity;
        this.unit = unit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Float getInpPrice() {
        return inpPrice;
    }

    public void setInpPrice(Float inpPrice) {
        this.inpPrice = inpPrice;
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

    public EUnit getUnit() {
        return unit;
    }

    public void setUnit(EUnit unit) {
        this.unit = unit;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
