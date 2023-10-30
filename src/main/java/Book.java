import java.time.LocalDate;
import java.util.Objects;

/**
 * @author Bryan Zanoli
 * @since October 30, 2023
 *
 * Abstract: ...
 */
public class Book {

  public static final int ISBN_ = 0;
  public static final int TITLE_ = 1;
  public static final int SUBJECT_ = 2;
  public static final int PAGE_COUNT_ = 3;
  public static final int AUTHOR_ = 4;
  public static final int DUE_DATE_ = 5;

  String isbn;
  String title;
  String subject;
  int pageCount;
  String author;
  LocalDate dueDate; // = LocalDate.now().plusWeeks(1);

  public Book(String isbn, String title,String subject, int pageCount, String author, LocalDate dueDate) {
    this.isbn = isbn;
    this.title = title;
    this.subject = subject;
    this.pageCount = pageCount;
    this.author = author;
    this.dueDate = dueDate;
  }

  public String getISBN() {
    return isbn;
  }

  public void setISBN(String isbn) {
    this.isbn = isbn;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public int getPageCount() {
    return pageCount;
  }

  public void setPageCount(int pageCount) {
    this.pageCount = pageCount;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Book book = (Book) o;
    return pageCount == book.pageCount && Objects.equals(isbn, book.isbn)
        && Objects.equals(title, book.title) && Objects.equals(subject,
        book.subject) && Objects.equals(author, book.author);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isbn, title, subject, pageCount, author);
  }

  public String toString() {
    return title + " by " + author + " ISBN: " + isbn;
  }

}