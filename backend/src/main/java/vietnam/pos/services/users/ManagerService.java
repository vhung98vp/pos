package vietnam.pos.services.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vietnam.pos.loaders.responses.ManagerResponse;
import vietnam.pos.models.orders.EOrderStatus;
import vietnam.pos.repository.orders.ImportRepository;
import vietnam.pos.repository.orders.OrderRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ManagerService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ImportRepository importRepository;

    public ManagerResponse getReport(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        var today = formatter.format(new Date());

        var orderDayTotal = new AtomicReference<>((float) 0);
        var orderByDay = new AtomicInteger();
        var impDayTotal = new AtomicReference<>((float) 0);
        var impByDay = new AtomicInteger();

        orderRepository.findAll().forEach(order -> {
            if(today.equals(order.getCreatedOn().substring(0,10)) && order.getStatus() != EOrderStatus.CANCELED){
                orderDayTotal.updateAndGet(v -> v + order.getTotal());
                orderByDay.addAndGet(1);
            }
        });

        importRepository.findAll().forEach(imp -> {
            if(today.equals(imp.getCreatedOn().substring(0,10)) && imp.getStatus() != EOrderStatus.CANCELED)
            {
                impDayTotal.updateAndGet(v -> v + imp.getTotal());
                impByDay.addAndGet(1);
            }
        });

        return new ManagerResponse(orderDayTotal, orderByDay, impDayTotal, impByDay);
    }
}
