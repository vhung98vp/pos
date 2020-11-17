package vietnam.pos.loaders.responses;

import java.util.List;

public class OrderResponse {

    private Long id;

    private Float total;

    private String note;

    private String createOn;

    private String updateOn;

    private String username;

    private String status;

    private List<OrderProductResponse> orderProducts;

    public OrderResponse() {
    }

    public OrderResponse(Long id, Float total, String note, String createOn, String updateOn, String username, String status) {
        this.id = id;
        this.total = total;
        this.note = note;
        this.createOn = createOn;
        this.updateOn = updateOn;
        this.username = username;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCreateOn() {
        return createOn;
    }

    public void setCreateOn(String createOn) {
        this.createOn = createOn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUpdateOn() {
        return updateOn;
    }

    public void setUpdateOn(String updateOn) {
        this.updateOn = updateOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderProductResponse> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProductResponse> orderProducts) {
        this.orderProducts = orderProducts;
    }
}
