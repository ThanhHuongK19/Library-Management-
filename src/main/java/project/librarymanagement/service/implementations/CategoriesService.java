package project.librarymanagement.service.implementations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import project.librarymanagement.dto.request.CreateCategoryRequest;
import project.librarymanagement.dto.request.UpdateCategoryRequest;
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
    public Page<Categories> getAllCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("categoryName").ascending());
        return categoriesRepository.findAll(pageable);
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
    @Transactional
    public Categories createCategory(
            CreateCategoryRequest request
    ) {

        if (categoriesRepository
                .existsCategoryByCategoryName(
                        request.getCategoryName()
                )) {

            throw new BadRequestException(
                    "Category already exists: "
                            + request.getCategoryName()
            );
        }

        Categories category = new Categories();

        category.setCategoryName(
                request.getCategoryName()
        );

        category.setDescription(
                request.getDescription()
        );

        return categoriesRepository.save(category);
    }

    @Override
    public Categories updateCategory(
            Long categoryId,
            UpdateCategoryRequest request
    ) {
        Categories existingCategory = getCategoryById(categoryId);

        if (!existingCategory.getCategoryName()
                .equalsIgnoreCase(request.getCategoryName())
                && categoriesRepository.existsCategoryByCategoryName(
                request.getCategoryName()
        )) {
            throw new BadRequestException(
                    "Category already exists: " + request.getCategoryName()
            );
        }

        existingCategory.setCategoryName(request.getCategoryName());
        existingCategory.setDescription(request.getDescription());

        return categoriesRepository.save(existingCategory);
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

    @Override
    public Page<Categories> searchCategories(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("categoryName").ascending());
        return categoriesRepository.findByCategoryNameContainingIgnoreCase(keyword, pageable);
    }
}