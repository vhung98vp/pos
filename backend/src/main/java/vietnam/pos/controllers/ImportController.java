package vietnam.pos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vietnam.pos.loaders.requests.ImportRequest;
import vietnam.pos.loaders.responses.ImportResponse;
import vietnam.pos.loaders.responses.MessageResponse;
import vietnam.pos.models.orders.Import;
import vietnam.pos.repository.orders.ImportRepository;
import vietnam.pos.services.orders.ImportService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/imports")
public class ImportController {
    @Autowired
    private ImportRepository importRepository;

    @Autowired
    private ImportService importService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('WAREHOUSE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ImportResponse> getImportById(@PathVariable Long id){
        Import imp = importRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Import not found."));
        var response = importService.convertImportToResponse(imp);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/paid/{id}")
    @PreAuthorize("hasRole('WAREHOUSE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> paidImport(@PathVariable Long id){
        Import imp = importRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Import not found."));
        importService.paidImport(imp);
        return ResponseEntity.ok(new MessageResponse("Import paid successfully!"));
    }

    @PutMapping("/cancel/{id}")
    @PreAuthorize("hasRole('WAREHOUSE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> cancelImport(@PathVariable Long id){
        Import imp = importRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Import not found."));
        importService.cancelImport(imp);
        return ResponseEntity.ok(new MessageResponse("Import cancelled successfully!"));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('WAREHOUSE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public List<Import> getAllImports(){
        return importRepository.findAll();
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('WAREHOUSE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> createOrder(@RequestBody ImportRequest importRequest){
        importService.createImport(importRequest);
        return ResponseEntity.ok(new MessageResponse("Import created successfully!"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('WAREHOUSE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> deleteOrder(@PathVariable Long id){
        importService.deleteImport(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
