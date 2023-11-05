import java.time.LocalDate;
import java.util.Objects;

/**
 * @author Bryan Zanoli
 * @since October 30, 2023
 * <p>
 * Abstract: Class which represents a book and its various attributes. Each instance of a book is a
 * separate instance of a Book object. Includes constant values needed to represent the input
 * indexes within input files. Also includes various methods needed to get and set values for each
 * field. Additional methods include equals, hashcode, and a custom toString method to output the
 * relevant book attribute values.
 */
public class Book {

  /**
   * Constant value used to track index of ISBN location within the import file.
   */
  public static final int ISBN_ = 0;
  /**
   * Constant value used to track index of Title location within the import file.
   */
  public static final int TITLE_ = 1;
  /**
   * Constant value used to track index of Subject location within the import file.
   */
  public static final int SUBJECT_ = 2;
  /**
   * Constant value used to track index of Page Count location within the import file.
   */
  public static final int PAGE_COUNT_ = 3;
  /**
   * Constant value used to track index of Author location within the import file.
   */
  public static final int AUTHOR_ = 4;
  /**
   * Constant value used to track index of Due Date location within the import file.
   */
  public static final int DUE_DATE_ = 5;

  /**
   * The String value used to store a given Book object's ISBN.
   */
  private String isbn;
  /**
   * The String value used to store a given Book object's title.
   */
  private String title;
  /**
   * The String value used to store a given Book object's subject.
   */
  private String subject;
  /**
   * The int value used to store a given Book object's page count.
   */
  private int pageCount;
  /**
   * The String value used to store a given Book object's Author.
   */
  private String author;
  /**
   * The String value used to store a given Book object's due date for library return.
   */
  private LocalDate dueDate; // = LocalDate.now().plusWeeks(1);

  /**
   * Full parameter constructor taking in all values for all Object fields.
   *
   * @param isbn
   * @param title
   * @param subject
   * @param pageCount
   * @param author
   * @param dueDate
   */
  public Book(String isbn, String title, String subject, int pageCount, String author,
      LocalDate dueDate) {
    this.isbn = isbn;
    this.title = title;
    this.subject = subject;
    this.pageCount = pageCount;
    this.author = author;
    this.dueDate = dueDate;
  }

  /**
   * Used to obtain a given book's ISBN in String form.
   *
   * @return String {@link Book#isbn}
   */
  public String getISBN() {
    return isbn;
  }

  /**
   * Used to set the ISBN of a given book.
   *
   * @param isbn
   */
  public void setISBN(String isbn) {
    this.isbn = isbn;
  }

  /**
   * Used to obtain a given book's title in string form.
   *
   * @return String {@link Book#title}
   */
  public String getTitle() {
    return title;
  }

  /**
   * Used to set the title of a given book.
   *
   * @param title
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Used to obtain the subject of a given book.
   *
   * @return String {@link Book#subject}
   */
  public String getSubject() {
    return subject;
  }

  /**
   * Used to set the subject for a given book.
   *
   * @param subject
   */
  public void setSubject(String subject) {
    this.subject = subject;
  }

  /**
   * Used to get the page count of a given book.
   *
   * @return int {@link Book#pageCount}
   */
  public int getPageCount() {
    return pageCount;
  }

  /**
   * Used to set the page count for a given book.
   *
   * @param pageCount
   */
  public void setPageCount(int pageCount) {
    this.pageCount = pageCount;
  }

  /**
   * Used to obtain the author of a given book.
   *
   * @return String {@link Book#author}
   */
  public String getAuthor() {
    return author;
  }

  /**
   * Used to set the author for a given book.
   *
   * @param author
   */
  public void setAuthor(String author) {
    this.author = author;
  }

  /**
   * Used to get the due date for a given book in LocalDate object type.
   *
   * @return LocalDate {@link Book#dueDate}
   */
  public LocalDate getDueDate() {
    return dueDate;
  }

  /**
   * Used to set the due date value of a given book. Requires a parameter of LocalDate instead of
   * String.
   *
   * @param dueDate
   */
  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }

  /**
   * Automatically generated equals function used to compare two books at a field by field level.
   *
   * @param o
   * @return boolean
   */
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

  /**
   * Automatically generated hashCode function used to generate a hash value for a book object.
   *
   * @return int
   */
  @Override
  public int hashCode() {
    return Objects.hash(isbn, title, subject, pageCount, author);
  }

  /**
   * Generates a string representation of the book by combining title with author and ISBN, the core
   * attributes of any given book.
   *
   * @return String {@link Book#title} + " by " + {@link Book#author} + " ISBN: " +
   * {@link Book#isbn}
   */
  public String toString() {
    return title + " by " + author + " ISBN: " + isbn;
  }

}