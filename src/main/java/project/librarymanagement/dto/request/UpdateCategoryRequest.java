package project.librarymanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateCategoryRequest {
    @NotBlank
    @Size(max = 100)
    private String categoryName;

    private String description;
}
