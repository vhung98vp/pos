package vietnam.pos.services.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vietnam.pos.loaders.requests.OrderRequest;
import vietnam.pos.loaders.responses.ImportItemResponse;
import vietnam.pos.loaders.responses.OrderItemResponse;
import vietnam.pos.loaders.responses.OrderProductResponse;
import vietnam.pos.loaders.responses.OrderResponse;
import vietnam.pos.models.orders.EOrderStatus;
import vietnam.pos.models.orders.Order;
import vietnam.pos.models.orders.OrderProduct;
import vietnam.pos.models.orders.OrderProductKey;
import vietnam.pos.models.products.Product;
import vietnam.pos.models.users.User;
import vietnam.pos.repository.orders.OrderProductRepository;
import vietnam.pos.repository.orders.OrderRepository;
import vietnam.pos.repository.products.ProductRepository;
import vietnam.pos.repository.users.UserRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderProductRepository orderProductRepository;


    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public List<OrderItemResponse> getAllOrders(){
        var responseList = new ArrayList<OrderItemResponse>();
        orderRepository.findAll().forEach(order -> {
            responseList.add(new OrderItemResponse(order.getId(), order.getTotal(), order.getNote(),
                    order.getCreatedOn(),order.getUpdatedOn(), order.getUser().getUsername(), order.getStatus().toString()
            ));
        });
        return responseList;
    }

    public OrderResponse convertOrderToResponse(Order order){
        var orderResponse = new OrderResponse(order.getId(), order.getTotal(), order.getNote(),
                order.getCreatedOn(), order.getUpdatedOn(), order.getUser().getUsername(), order.getStatus().toString()
        );
        var listOrderProducts = new ArrayList<OrderProductResponse>();
        orderProductRepository.findAll().forEach(orderProduct -> {
            if(orderProduct.getOrder() == order) {
                listOrderProducts.add(new OrderProductResponse(
                        orderProduct.getProduct().getId(),
                        orderProduct.getProduct().getName(),
                        orderProduct.getQuantity(),
                        orderProduct.getSellPrice()
                ));
            }
        });
        orderResponse.setOrderProducts(listOrderProducts);
        return orderResponse;
    }

    public void createOrder(OrderRequest orderRequest){
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        Order order = new Order(
                (float) 0,
                orderRequest.isPaid() ? EOrderStatus.PAID : EOrderStatus.PLACED,
                orderRequest.getNote(),
                formatter.format(new Date()),
                user);
        orderRepository.save(order);
        //Products
        List<Long> productIds = orderRequest.getProductIds();
        List<Float> quantities = orderRequest.getQuantities();
        var total = (float)0;
        for (int i=0; i<productIds.size(); i++){
            Product product = productRepository.findById(productIds.get(i))
                    .orElseThrow(() -> new RuntimeException("Error: Product not found."));

            var qty = product.getQuantity();
            product.setQuantity(qty - quantities.get(i));
            productRepository.save(product);

            OrderProductKey orderProductKey = new OrderProductKey(order.getId(),productIds.get(i));
            OrderProduct orderProduct = new OrderProduct(orderProductKey, order,
                    product, product.getSellPrice(), quantities.get(i));
            orderProductRepository.save(orderProduct);

            total += quantities.get(i)*product.getSellPrice();
        }
        order.setTotal(total);
        orderRepository.save(order);
    }

    public void paidOrder(Order order){
        if(order.getStatus() == EOrderStatus.CANCELED){
            orderProductRepository.findAll().forEach(orderProduct ->{
                if(orderProduct.getOrder() == order){
                    Product product = orderProduct.getProduct();
                    var qty = product.getQuantity();
                    product.setQuantity(qty - orderProduct.getQuantity());
                    productRepository.save(product);
                }
            });
        }
        order.setStatus(EOrderStatus.PAID);
        order.setUpdatedOn(formatter.format(new Date()));
        orderRepository.save(order);
    }

    public void cancelOrder(Order order){
        if(order.getStatus() != EOrderStatus.CANCELED){
            orderProductRepository.findAll().forEach(orderProduct -> {
                if(orderProduct.getOrder() == order){
                    Product product = orderProduct.getProduct();
                    var qty = product.getQuantity();
                    product.setQuantity(qty + orderProduct.getQuantity());
                    productRepository.save(product);
                }
            });
            order.setStatus(EOrderStatus.CANCELED);
            order.setUpdatedOn(formatter.format(new Date()));
            orderRepository.save(order);
        }
    }

    public void deleteOrder(Long orderId){
        var orderProducts = orderProductRepository.findAll().stream()
                .filter(orderProduct -> orderProduct.getOrder().getId().equals(orderId))
                .collect(Collectors.toList());
        orderProducts.forEach(orderProduct -> {
            orderProduct.setOrder(null);
            orderProduct.setProduct(null);
            orderProductRepository.delete(orderProduct);
        });
        orderRepository.deleteById(orderId);
    }
}
