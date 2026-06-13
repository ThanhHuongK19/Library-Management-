package project.librarymanagement.entity;

import java.sql.Timestamp;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "borrow_records")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BorrowRecords {

  public enum BorrowStatus {
    BORROWED,
    RETURNED,
    OVERDUE
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private Users user;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book_id")
  private Books book;

  @Column(name = "borrow_date")
  private Timestamp borrowDate;

  @NotNull
  @Column(name = "due_date")
  private Timestamp dueDate;

  @Column(name = "return_date")
  private Timestamp returnDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 50)
  private BorrowStatus status = BorrowStatus.BORROWED;

  @Column(columnDefinition = "TEXT")
  private String notes;
}
