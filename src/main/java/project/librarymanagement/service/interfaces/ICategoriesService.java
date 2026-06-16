package project.librarymanagement.service.interfaces;

import org.springframework.data.domain.Page;
import project.librarymanagement.dto.request.CreateCategoryRequest;
import project.librarymanagement.dto.request.UpdateCategoryRequest;
import project.librarymanagement.entity.Categories;

import java.util.List;

public interface ICategoriesService {

    Page<Categories> getAllCategories(int page, int size);

    Categories getCategoryById(Long categoryId);

    Categories getCategoryByName(String categoryName);

    Categories createCategory(CreateCategoryRequest request);

    Categories updateCategory(
            Long categoryId,
            UpdateCategoryRequest request
    );

    void deleteCategory(Long categoryId);

    Page<Categories> searchCategories(String keyword, int page, int size);
}