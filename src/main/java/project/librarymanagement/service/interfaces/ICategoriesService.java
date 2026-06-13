package project.librarymanagement.service.interfaces;

import project.librarymanagement.entity.Categories;

import java.util.List;

public interface ICategoriesService {

    List<Categories> getAllCategories();

    Categories getCategoryById(Long categoryId);

    Categories getCategoryByName(String categoryName);

    Categories createCategory(Categories category);

    Categories updateCategory(
            Long categoryId,
            Categories category
    );

    void deleteCategory(Long categoryId);
}