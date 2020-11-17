package vietnam.pos.loaders.responses;

public class OrderProductResponse {
    private Long productId;

    private String productName;

    private Float quantity;

    private Float sellPrice;

    public OrderProductResponse() {
    }

    public OrderProductResponse(Long productId, String productName, Float quantity, Float sellPrice) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.sellPrice = sellPrice;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public Float getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Float sellPrice) {
        this.sellPrice = sellPrice;
    }
}
