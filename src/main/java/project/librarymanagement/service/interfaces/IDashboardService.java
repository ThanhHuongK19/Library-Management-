package project.librarymanagement.service.interfaces;

public interface IDashboardService {
    long getTotalBooks();
    long getActiveBorrows();
    long getOverdueRecords();
    long getTotalUsers();
}
