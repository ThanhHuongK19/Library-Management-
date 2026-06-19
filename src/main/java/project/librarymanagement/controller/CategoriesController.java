package project.librarymanagement.controller;

import org.springframework.data.domain.Page;
import project.librarymanagement.dto.request.CreateCategoryRequest;
import project.librarymanagement.dto.request.UpdateCategoryRequest;
import project.librarymanagement.dto.response.CategoryResponse;
import project.librarymanagement.entity.Categories;
import project.librarymanagement.mapper.CategoryMapper;
import project.librarymanagement.service.interfaces.ICategoriesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {

    private final ICategoriesService categoriesService;
    private final CategoryMapper categoryMapper;

    public CategoriesController(
            ICategoriesService categoriesService,
            CategoryMapper categoryMapper
    ) {
        this.categoriesService = categoriesService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Categories> categoriesPage = categoriesService.getAllCategories(page, size);
        Page<CategoryResponse> responsePage = categoriesPage.map(categoryMapper::toCategoryResponse);

        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                categoryMapper.toCategoryResponse(
                        categoriesService.getCategoryById(id)
                )
        );
    }

    @GetMapping("/name/{categoryName}")
    public ResponseEntity<CategoryResponse> getCategoryByName(
            @PathVariable String categoryName
    ) {

        return ResponseEntity.ok(
                categoryMapper.toCategoryResponse(
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
                categoryMapper.toCategoryResponse(createdCategory),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequest request
    ) {
        return ResponseEntity.ok(
                categoryMapper.toCategoryResponse(
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
}