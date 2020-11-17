package vietnam.pos.loaders.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

public class OrderRequest{

    private String note;

    private Long userId;

    @JsonProperty
    public boolean isPaid;

    private List<Long> productIds;

    private List<Float> quantities;

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }

    public List<Float> getQuantities() {
        return quantities;
    }

    public void setQuantities(List<Float> quantities) {
        this.quantities = quantities;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }
}
