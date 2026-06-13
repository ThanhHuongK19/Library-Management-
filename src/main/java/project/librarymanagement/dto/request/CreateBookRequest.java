package project.librarymanagement.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBookRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255)
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 155)
    private String author;

    @NotBlank(message = "ISBN is required")
    @Size(max = 20)
    private String isbn;

    @Size(max = 155)
    private String publisher;

    @Min(value = 1000)
    @Max(value = 9999)
    private Integer publishYear;

    @Min(value = 0)
    private Integer quantity;

    private String summary;

    @NotNull(message = "Category is required")
    private Long categoryId;

    public CreateBookRequest() {
    }
}