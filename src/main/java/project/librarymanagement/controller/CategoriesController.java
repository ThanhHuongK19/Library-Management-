package project.librarymanagement.controller;

import project.librarymanagement.entity.Categories;
import project.librarymanagement.service.interfaces.ICategoriesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {

    private final ICategoriesService categoriesService;

    public CategoriesController(
            ICategoriesService categoriesService
    ) {
        this.categoriesService = categoriesService;
    }

    @GetMapping
    public ResponseEntity<List<Categories>> getAllCategories() {

        return ResponseEntity.ok(
                categoriesService.getAllCategories()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categories> getCategoryById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                categoriesService.getCategoryById(id)
        );
    }

    @GetMapping("/name/{categoryName}")
    public ResponseEntity<Categories> getCategoryByName(
            @PathVariable String categoryName
    ) {

        return ResponseEntity.ok(
                categoriesService.getCategoryByName(categoryName)
        );
    }

    @PostMapping
    public ResponseEntity<Categories> createCategory(
            @Valid @RequestBody Categories category
    ) {

        return new ResponseEntity<>(
                categoriesService.createCategory(category),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categories> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody Categories category
    ) {

        return ResponseEntity.ok(
                categoriesService.updateCategory(
                        id,
                        category
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long id
    ) {

        categoriesService.deleteCategory(id);

        return ResponseEntity.noContent().build();
    }
}
