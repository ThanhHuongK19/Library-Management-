package project.librarymanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookResponse {

    private Long bookId;
    private String title;
    private String author;
    private String isbn;
    private Integer quantity;
    private String publisher;
    private Integer publishYear;

    private Long categoryId;
    private String categoryName;
}
