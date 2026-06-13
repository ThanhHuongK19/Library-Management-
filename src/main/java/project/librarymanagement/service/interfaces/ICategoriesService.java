package project.librarymanagement.service.interfaces;

import project.librarymanagement.dto.request.CreateCategoryRequest;
import project.librarymanagement.dto.request.UpdateCategoryRequest;
import project.librarymanagement.entity.Categories;

import java.util.List;

public interface ICategoriesService {

    List<Categories> getAllCategories();

    Categories getCategoryById(Long categoryId);

    Categories getCategoryByName(String categoryName);

    Categories createCategory(CreateCategoryRequest request);

    Categories updateCategory(
            Long categoryId,
            UpdateCategoryRequest request
    );

    void deleteCategory(Long categoryId);
}