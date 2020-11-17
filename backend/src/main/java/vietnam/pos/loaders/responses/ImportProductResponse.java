package vietnam.pos.loaders.responses;

public class ImportProductResponse {
    private Long productId;

    private String productName;

    private Float quantity;

    private Float inpPrice;

    public ImportProductResponse() {
    }

    public ImportProductResponse(Long productId, String productName, Float quantity, Float inpPrice) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.inpPrice = inpPrice;
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

    public Float getInpPrice() {
        return inpPrice;
    }

    public void setInpPrice(Float inpPrice) {
        this.inpPrice = inpPrice;
    }
}
