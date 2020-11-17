package vietnam.pos.loaders.responses;

import vietnam.pos.models.products.EUnit;

public class ProductResponse {
    private Long id;

    private String name;

    private String sku;

    private float inpPrice;

    private float sellPrice;

    private float quantity;

    private EUnit unit;

    private Long pictureId;

    private Long categoryId;

    private String pictureUri;

    public ProductResponse() {
    }

    public ProductResponse(Long id, String name, String sku, float inpPrice, float sellPrice, float quantity, EUnit unit) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.inpPrice = inpPrice;
        this.sellPrice = sellPrice;
        this.quantity = quantity;
        this.unit = unit;
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

    public EUnit getUnit() {
        return unit;
    }

    public void setUnit(EUnit unit) {
        this.unit = unit;
    }

    public Long getPictureId() {
        return pictureId;
    }

    public void setPictureId(Long pictureId) {
        this.pictureId = pictureId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPictureUri() {
        return pictureUri;
    }

    public void setPictureUri(String pictureUri) {
        this.pictureUri = pictureUri;
    }

    public float getInpPrice() {
        return inpPrice;
    }

    public void setInpPrice(float inpPrice) {
        this.inpPrice = inpPrice;
    }

    public float getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(float sellPrice) {
        this.sellPrice = sellPrice;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }
}
