package project.librarymanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponse {

    private Long categoryId;
    private String categoryName;
    private String description;
}
