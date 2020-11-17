package vietnam.pos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vietnam.pos.loaders.requests.ProductRequest;
import vietnam.pos.loaders.responses.MessageResponse;
import vietnam.pos.loaders.responses.ProductResponse;
import vietnam.pos.models.media.Picture;
import vietnam.pos.models.products.Product;
import vietnam.pos.repository.media.PictureRepository;
import vietnam.pos.repository.products.CategoryRepository;
import vietnam.pos.repository.products.ProductRepository;
import vietnam.pos.services.products.ProductService;

import java.io.*;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    PictureRepository pictureRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductService productService;

    //Picture
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @PostMapping(value = "/picture/upload")
    public ResponseEntity<?> uploadPicture(@RequestParam MultipartFile file) throws IOException {
        var picture = new Picture(file.getContentType(), file.getBytes());
        pictureRepository.save(picture);
        return ResponseEntity.ok(picture.getId());
    }

    //Product
    @GetMapping("/all")
    public List<ProductResponse> getAllProducts(){
        var products = productRepository.findAll();
        List<ProductResponse> responses = new ArrayList<>();
        products.forEach((product) -> {
            responses.add(productService.convertProductToResponse(product));
        });
        return responses;
    }

    @GetMapping("/all/{categoryId}")
    public List<ProductResponse> getAllProductsByCategoryId(@PathVariable Long categoryId){
        var products = new ArrayList<Product>();
        if(categoryId == -1)
            products.addAll(productRepository.findAll());
        else if(categoryId == 0)
            productRepository.findAll().forEach(product -> {
                if(product.getCategory() == null)
                    products.add(product);
            });
        else {
            var category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Error: Category not found."));
            products.addAll(productService.getAllProductsByCategory(category));
        }
        List<ProductResponse> responses = new ArrayList<>();
        products.forEach((product) -> {
            responses.add(productService.convertProductToResponse(product));
        });
        return responses;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Product not found."));
        var response = productService.convertProductToResponse(product);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> addNewProduct(@RequestBody ProductRequest productRequest) {
        Product product = productService.convertRequestToProduct(null, productRequest);
        productRepository.save(product);
        return ResponseEntity.ok(new MessageResponse("Product added successfully!"));
    }

    @PutMapping("/edit/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,
                                           @RequestBody ProductRequest productRequest) {
        Product prd = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Product not found."));
        var product = productService.convertRequestToProduct(prd, productRequest);
        productRepository.save(product);
        return ResponseEntity.ok(new MessageResponse("Product updated successfully!"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> deleteProduct(@PathVariable Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Product not found."));

        //delete
        if(product.getPicture() != null)
            pictureRepository.delete(product.getPicture());
        if(product.getCategory() != null)
            product.setCategory(null);
        productRepository.delete(product);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

}
