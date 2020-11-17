package vietnam.pos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vietnam.pos.loaders.requests.CategoryRequest;
import vietnam.pos.loaders.responses.CategoryItem;
import vietnam.pos.loaders.responses.CategoryResponse;
import vietnam.pos.loaders.responses.MessageResponse;
import vietnam.pos.models.products.Category;
import vietnam.pos.services.products.CategoryService;
import vietnam.pos.repository.products.CategoryRepository;
import vietnam.pos.repository.products.ProductRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryService categoryService;

    @GetMapping("/all")
    public List<CategoryItem> getAllCategories(){
        return categoryService.getAllCategories((long)0);
    }

    @GetMapping("/all/{id}")
    public List<CategoryItem> getListParentCategories(@PathVariable Long id){
        List<CategoryItem> treeCategories = new ArrayList<CategoryItem>();
        treeCategories.addAll(categoryService.getAllCategories(id));
        return treeCategories;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Category not found."));
        var response = categoryService.convertCategoryToResponse(category);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> addNewCategory(@RequestBody CategoryRequest categoryRequest){
        Category category = categoryService.convertRequestToCategory(null, categoryRequest);
        categoryRepository.save(category);
        return ResponseEntity.ok(new MessageResponse("Category added successfully!"));
    }

    @PutMapping("/edit/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateCategory(@PathVariable Long id,
                                           @RequestBody CategoryRequest categoryRequest) {
        Category cat = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Category not found."));
        Category category = categoryService.convertRequestToCategory(cat, categoryRequest);
        categoryRepository.save(category);
        return ResponseEntity.ok(new MessageResponse("Category updated successfully!"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> deleteCategory(@PathVariable Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Category not found."));
        // edit sublist
        var subList = categoryService.getAllSubCategories(category);
        subList.forEach(item -> {
            item.setParentCategory(null);
            categoryRepository.save(item);
        });

        productRepository.findAll().forEach(product -> {
            if(product.getCategory() == category) {
                product.setCategory(category.getParentCategory());
                productRepository.save(product);
            }
        });
        //delete
        category.setParentCategory(null);
        categoryRepository.delete(category);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
