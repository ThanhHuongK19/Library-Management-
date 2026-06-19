package project.librarymanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.librarymanagement.dto.response.BorrowRecordResponse;
import project.librarymanagement.entity.BorrowRecords;

// Su dung thu vien MapStruct, code ngan gon hon

@Mapper(componentModel = "spring") //dang ky lam Spring Bean
public interface BorrowRecordMapper {
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "book.bookId", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    BorrowRecordResponse toBorrowRecordResponse(BorrowRecords record);
}
