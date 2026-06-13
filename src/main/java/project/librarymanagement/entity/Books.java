package project.librarymanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Books {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "book_id")
  private Long bookId;

  @NotBlank(message = "Title is required")
  @Column(name = "title", nullable = false)
  private String title;

  @NotBlank(message = "Author is required")
  @Column(name = "author", nullable = false, length = 155)
  private String author;

  @Column(name = "isbn", unique = true, length = 20)
  private String isbn;

  @Column(name = "publisher", length = 155)
  private String publisher;

  @Column(name = "publish_year")
  @Min(value = 0, message = "Publish year must be positive")
  private Integer publishYear;

  @Column(name = "quantity", nullable = false)
  @Min(value = 0, message = "Quantity cannot be negative")
  private Integer quantity = 0;

  @Column(name = "summary", columnDefinition = "TEXT")
  private String summary;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Categories category;
}