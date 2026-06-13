package project.librarymanagement.service.interfaces;

import project.librarymanagement.entity.Categories;
import java.util.List;

public interface ICategoriesService {
    List<Categories> getAllCategories();

    Categories getCategoryById(int id);

    Categories createCategory(Categories category);

    Categories updateCategory(int id, Categories category);

    void deleteCategory(int id);
}
