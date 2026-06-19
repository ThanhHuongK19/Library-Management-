package project.librarymanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.librarymanagement.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriesRepository
        extends JpaRepository<Categories, Long> {

    Optional<Categories> findCategoryByCategoryName(
            String categoryName
    );

    boolean existsCategoryByCategoryName(
            String categoryName
    );

    Page<Categories> findByCategoryNameContainingIgnoreCase(String keyword, Pageable pageable);
}