package vietnam.pos.loaders.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ImportRequest {
    private Float total;

    private String note;

    private Long userId;

    @JsonProperty
    private boolean isPaid;

    private List<Long> productIds;

    private List<Float> quantities;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
