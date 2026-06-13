package project.librarymanagement.service.implementations;

import project.librarymanagement.entity.Categories;
import project.librarymanagement.exception.BadRequestException;
import project.librarymanagement.exception.ResourceNotFoundException;
import project.librarymanagement.repository.BooksRepository;
import project.librarymanagement.repository.CategoriesRepository;
import project.librarymanagement.service.interfaces.ICategoriesService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesService implements ICategoriesService {

    private final CategoriesRepository categoriesRepository;
    private final BooksRepository booksRepository;

    public CategoriesService(
            CategoriesRepository categoriesRepository,
            BooksRepository booksRepository
    ) {
        this.categoriesRepository = categoriesRepository;
        this.booksRepository = booksRepository;
    }

    @Override
    public List<Categories> getAllCategories() {
        return categoriesRepository.findAll();
    }

    @Override
    public Categories getCategoryById(Long categoryId) {
        return categoriesRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: "
                                        + categoryId
                        )
                );
    }

    @Override
    public Categories getCategoryByName(
            String categoryName
    ) {
        return categoriesRepository
                .findCategoryByCategoryName(categoryName)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found: "
                                        + categoryName
                        )
                );
    }

    @Override
    public Categories createCategory(
            Categories category
    ) {
        if (categoriesRepository
                .existsCategoryByCategoryName(
                        category.getCategoryName()
                )) {

            throw new BadRequestException(
                    "Category already exists: "
                            + category.getCategoryName()
            );
        }

        return categoriesRepository.save(category);
    }

    @Override
    public Categories updateCategory(
            Long categoryId,
            Categories category
    ) {
        Categories existingCategory =
                getCategoryById(categoryId);

        if (!existingCategory.getCategoryName()
                .equalsIgnoreCase(
                        category.getCategoryName())
                &&
                categoriesRepository
                        .existsCategoryByCategoryName(
                                category.getCategoryName()
                        )) {

            throw new BadRequestException(
                    "Category already exists: "
                            + category.getCategoryName()
            );
        }

        existingCategory.setCategoryName(
                category.getCategoryName()
        );

        existingCategory.setDescription(
                category.getDescription()
        );

        return categoriesRepository.save(
                existingCategory
        );
    }

    @Override
    public void deleteCategory(Long categoryId) {

        Categories category =
                getCategoryById(categoryId);

        if (booksRepository
                .existsBooksByCategory(category)) {

            throw new BadRequestException(
                    "Cannot delete category because it contains books"
            );
        }

        categoriesRepository.delete(category);
    }
}