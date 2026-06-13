package project.librarymanagement.controller;

import project.librarymanagement.entity.Categories;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.librarymanagement.service.interfaces.ICategoriesService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {

    private final ICategoriesService categoriesService;

    public CategoriesController(ICategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping("/get-all-categories")
    public ResponseEntity<List<Categories>> getAllCategories() {
        return ResponseEntity.ok(categoriesService.getAllCategories());
    }

    @GetMapping("/get-category/{id}")
    public ResponseEntity<Categories> getCategoryById(
            @PathVariable int id) {

        return ResponseEntity.ok(categoriesService.getCategoryById(id));
    }

    @PostMapping("/create-category")
    public ResponseEntity<Categories> createCategory(
            @Valid @RequestBody Categories category) {

        return new ResponseEntity<>(
                categoriesService.createCategory(category),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/update-category/{id}")
    public ResponseEntity<Categories> updateCategory(
            @PathVariable int id,
            @Valid @RequestBody Categories category) {

        return ResponseEntity.ok(
                categoriesService.updateCategory(id, category)
        );
    }

    @DeleteMapping("/delete-category/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable int id) {

        categoriesService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
