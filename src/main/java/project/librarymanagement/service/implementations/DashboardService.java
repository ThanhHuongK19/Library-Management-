package project.librarymanagement.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.librarymanagement.entity.BorrowRecords;
import project.librarymanagement.repository.BooksRepository;
import project.librarymanagement.repository.BorrowRecordsRepository;
import project.librarymanagement.repository.UsersRepository;
import project.librarymanagement.service.interfaces.IDashboardService;

import java.sql.Timestamp;

@Service
public class DashboardService implements IDashboardService {
    @Autowired
    private BooksRepository booksRepository;
    @Autowired
    private BorrowRecordsRepository borrowRecordsRepository;
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public long getTotalBooks() { return booksRepository.count(); }

    @Override
    public long getActiveBorrows() {
        return borrowRecordsRepository.countByStatus(BorrowRecords.BorrowStatus.BORROWED);
    }

    @Override
    public long getOverdueRecords() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        return borrowRecordsRepository.countByStatusAndDueDateBefore(
                BorrowRecords.BorrowStatus.BORROWED, currentTimestamp);
    }

    @Override
    public long getTotalUsers() { return usersRepository.count(); }
}
