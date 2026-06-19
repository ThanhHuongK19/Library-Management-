package project.librarymanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.librarymanagement.dto.response.CategoryResponse;
import project.librarymanagement.entity.Categories;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toCategoryResponse(Categories category);
}
