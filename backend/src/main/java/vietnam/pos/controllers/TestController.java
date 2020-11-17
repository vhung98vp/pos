package vietnam.pos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vietnam.pos.loaders.responses.ImportItemResponse;
import vietnam.pos.loaders.responses.ManagerResponse;
import vietnam.pos.loaders.responses.OrderItemResponse;
import vietnam.pos.models.users.User;
import vietnam.pos.repository.users.UserRepository;
import vietnam.pos.services.orders.ImportService;
import vietnam.pos.services.orders.OrderService;
import vietnam.pos.services.users.ManagerService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    private UserRepository userRepository;

	@Autowired
	OrderService orderService;

	@Autowired
	ImportService importService;

	@Autowired
	private ManagerService managerService;

	@GetMapping("/all")
	public String allAccess() {
		return "Welcome to Pos Application";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public List<User> adminAccess() {
		return userRepository.findAll();
	}

	@GetMapping("/manager")
	@PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
	public ResponseEntity<ManagerResponse> managerAccess() {
		return ResponseEntity.ok(managerService.getReport());
	}

	@GetMapping("/warehouse")
	@PreAuthorize("hasRole('WAREHOUSE') or hasRole('MANAGER') or hasRole('ADMIN')")
	public List<ImportItemResponse> warehouseAccess() {
		return importService.getAllImports();
	}

	@GetMapping("/cashier")
	@PreAuthorize("hasRole('CASHIER') or hasRole('MANAGER') or hasRole('ADMIN')")
	public List<OrderItemResponse> cashierAccess() {
		return orderService.getAllOrders();
	}
}
