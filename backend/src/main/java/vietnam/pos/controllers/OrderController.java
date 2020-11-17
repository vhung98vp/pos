package vietnam.pos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vietnam.pos.loaders.requests.OrderRequest;
import vietnam.pos.loaders.responses.MessageResponse;
import vietnam.pos.loaders.responses.OrderResponse;
import vietnam.pos.models.orders.Order;
import vietnam.pos.repository.orders.OrderRepository;
import vietnam.pos.services.orders.OrderService;

import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CASHIER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Order not found."));
        var response = orderService.convertOrderToResponse(order);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/paid/{id}")
    @PreAuthorize("hasRole('CASHIER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> paidOrder(@PathVariable Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Order not found."));
        orderService.paidOrder(order);
        return ResponseEntity.ok(new MessageResponse("Order paid successfully!"));
    }

    @PutMapping("/cancel/{id}")
    @PreAuthorize("hasRole('CASHIER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Order not found."));
        orderService.cancelOrder(order);
        return ResponseEntity.ok(new MessageResponse("Order cancelled successfully!"));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('CASHIER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('CASHIER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest){
        orderService.createOrder(orderRequest);
        return ResponseEntity.ok(new MessageResponse("Order created successfully!"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CASHIER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> deleteOrder(@PathVariable Long id){
        orderService.deleteOrder(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
