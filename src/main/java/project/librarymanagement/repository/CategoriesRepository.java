package project.librarymanagement.repository;

import project.librarymanagement.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    List<Categories> findByCategoryNameContainingIgnoreCase(String keyword);
}