package project.librarymanagement.mapper;

import org.springframework.stereotype.Component;
import project.librarymanagement.dto.response.BookResponse;
import project.librarymanagement.entity.Books;

// Mappper thu cong

@Component
public class BookMapper {

    public BookResponse toBookResponse(Books book) {
        if (book == null) {
            return null;
        }

        Long categoryId = null;
        String categoryName = null;

        if (book.getCategory() != null) {
            categoryId = book.getCategory().getCategoryId();
            categoryName = book.getCategory().getCategoryName();
        }

        return new BookResponse(
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getQuantity(),
                book.getPublisher(),
                book.getPublishYear(),
                categoryId,
                categoryName
        );
    }
}