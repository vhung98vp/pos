package vietnam.pos.loaders.responses;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ManagerResponse {
    private AtomicReference<Float> orderDayTotal;

    private AtomicInteger orderByDay;

    private AtomicReference<Float> impDayTotal;

    private AtomicInteger impByDay;

    public ManagerResponse() {
    }

    public ManagerResponse(AtomicReference<Float> orderDayTotal, AtomicInteger orderByDay, AtomicReference<Float> impDayTotal, AtomicInteger impByDay) {
        this.orderDayTotal = orderDayTotal;
        this.orderByDay = orderByDay;
        this.impDayTotal = impDayTotal;
        this.impByDay = impByDay;
    }

    public AtomicReference<Float> getOrderDayTotal() {
        return orderDayTotal;
    }

    public void setOrderDayTotal(AtomicReference<Float> orderDayTotal) {
        this.orderDayTotal = orderDayTotal;
    }

    public AtomicInteger getOrderByDay() {
        return orderByDay;
    }

    public void setOrderByDay(AtomicInteger orderByDay) {
        this.orderByDay = orderByDay;
    }

    public AtomicReference<Float> getImpDayTotal() {
        return impDayTotal;
    }

    public void setImpDayTotal(AtomicReference<Float> impDayTotal) {
        this.impDayTotal = impDayTotal;
    }

    public AtomicInteger getImpByDay() {
        return impByDay;
    }

    public void setImpByDay(AtomicInteger impByDay) {
        this.impByDay = impByDay;
    }
}
