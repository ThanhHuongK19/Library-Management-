package project.librarymanagement.controller;

import project.librarymanagement.dto.request.CreateCategoryRequest;
import project.librarymanagement.dto.request.UpdateCategoryRequest;
import project.librarymanagement.dto.response.CategoryResponse;
import project.librarymanagement.entity.Categories;
import project.librarymanagement.service.interfaces.ICategoriesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {

        List<CategoryResponse> categories =
                categoriesService.getAllCategories()
                        .stream()
                        .map(this::toCategoryResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                toCategoryResponse(
                        categoriesService.getCategoryById(id)
                )
        );
    }

    @GetMapping("/name/{categoryName}")
    public ResponseEntity<CategoryResponse> getCategoryByName(
            @PathVariable String categoryName
    ) {

        return ResponseEntity.ok(
                toCategoryResponse(
                        categoriesService.getCategoryByName(categoryName)
                )
        );
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CreateCategoryRequest request
    ) {

        Categories createdCategory =
                categoriesService.createCategory(request);

        return new ResponseEntity<>(
                toCategoryResponse(createdCategory),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequest request
    ) {
        return ResponseEntity.ok(
                toCategoryResponse(
                        categoriesService.updateCategory(id, request)
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

    private CategoryResponse toCategoryResponse(
            Categories category
    ) {
        return new CategoryResponse(
                category.getCategoryId(),
                category.getCategoryName(),
                category.getDescription()
        );
    }
}