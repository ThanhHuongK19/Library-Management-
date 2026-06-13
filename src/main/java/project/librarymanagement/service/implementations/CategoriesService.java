package project.librarymanagement.service.implementations;

import project.librarymanagement.entity.Categories;
import project.librarymanagement.exception.BadRequestException;
import project.librarymanagement.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import project.librarymanagement.repository.BooksRepository;
import project.librarymanagement.repository.CategoriesRepository;
import project.librarymanagement.service.interfaces.ICategoriesService;

import java.util.List;

@Service
public class CategoriesService implements ICategoriesService {

    private final CategoriesRepository categoriesRepository;
    private final BooksRepository booksRepository;

    public CategoriesService(CategoriesRepository categoriesRepository,
                             BooksRepository booksRepository) {
        this.categoriesRepository = categoriesRepository;
        this.booksRepository = booksRepository;
    }

    @Override
    public List<Categories> getAllCategories() {
        return categoriesRepository.findAll();
    }

    @Override
    public Categories getCategoryById(int id) {
        return categoriesRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id
                        )
                );
    }

    @Override
    public Categories createCategory(Categories category) {

        if (category.getCategoryName() == null
                || category.getCategoryName().isBlank()) {
            throw new BadRequestException("Category name cannot be empty");
        }

        if (categoriesRepository.existsByCategoryName(
                category.getCategoryName())) {

            throw new BadRequestException(
                    "Category name already exists"
            );
        }

        return categoriesRepository.save(category);
    }

    @Override
    public Categories updateCategory(int id, Categories category) {

        Categories existingCategory = getCategoryById(id);

        if (category.getCategoryName() == null
                || category.getCategoryName().isBlank()) {
            throw new BadRequestException("Category name cannot be empty");
        }

        if (!existingCategory.getCategoryName()
                .equalsIgnoreCase(category.getCategoryName())
                && categoriesRepository.existsByCategoryName(
                category.getCategoryName())) {

            throw new BadRequestException(
                    "Category name already exists"
            );
        }

        existingCategory.setCategoryName(
                category.getCategoryName()
        );

        existingCategory.setDescription(
                category.getDescription()
        );

        return categoriesRepository.save(existingCategory);
    }

    @Override
    public void deleteCategory(int id) {

        Categories category = getCategoryById(id);

        if (!booksRepository.findByCategory(category).isEmpty()){
            throw new BadRequestException(
                    "Cannot delete category because it contains books"
            );
        }

        categoriesRepository.delete(category);
    }
}