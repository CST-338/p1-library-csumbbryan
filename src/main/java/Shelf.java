import Utilities.Code;
import java.util.HashMap;
import java.util.Objects;

public class Shelf {
  private static final int SHELF_NUMBER_ = 0;
  private static final int SUBJECT = 1;
  private int shelfNumber;
  private String subject;
  private HashMap<Book, Integer> books;

  public Shelf() {
    this.shelfNumber = 0;
    this.subject = "";
    this.books = new HashMap<>();
  }

  public Shelf(int shelfNumber, String subject) {
    this.shelfNumber = shelfNumber;
    this.subject = subject;
    this.books = new HashMap<>();
  }

  public int getShelfNumber() {
    return shelfNumber;
  }

  public void setShelfNumber(int shelfNumber) {
    this.shelfNumber = shelfNumber;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public HashMap<Book, Integer> getBooks() {
    return books;
  }

  public void setBooks(HashMap<Book, Integer> books) {
    this.books = books;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Shelf shelf = (Shelf) o;
    return shelfNumber == shelf.shelfNumber && Objects.equals(subject, shelf.subject);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shelfNumber, subject);
  }

  public String toString() {
    return shelfNumber + " : " + subject;
  }

  public int getBookCount(Book book) {
    return this.books.containsKey(book) ? this.books.get(book) : -1;
  }

  public Code addBook(Book book) {
    for(Book hashBook : books.keySet()) {
      if(hashBook.equals(book)) {
        int count = books.get(book);
        books.replace(book, ++count);
        System.out.println(book.toString() + this.toString());
        return Code.SUCCESS;
      }
    }
    if(!books.containsKey(book) && book.subject.equals(this.subject)) {
      books.put(book, 1);
      System.out.println(book.toString() + this.toString());
      return Code.SUCCESS;
    }
    return Code.SHELF_SUBJECT_MISMATCH_ERROR;
  }

  public Code removeBook(Book book) {
    if(books.containsKey(book)) {
      if(books.get(book) == 0) {
        System.out.println(book.title + " is not on shelf " + this.subject);
        return Code.BOOK_NOT_IN_INVENTORY_ERROR;
      } else {
        int count = books.get(book);
        books.put(book, --count);
        return Code.SUCCESS;
      }

    } else {
      System.out.println("No copies of " + book.title + " remain on shelf " + this.subject);
      return Code.BOOK_NOT_IN_INVENTORY_ERROR;
    }
  }

  public String listBooks() {
    StringBuilder bookList = new StringBuilder(new String());
    for(Book book : this.books.keySet()) {
      bookList.append(book.title).append(" by ").append(book.author).append(" ISBN:")
          .append(book.isbn).append(" ").append(this.books.get(book)).append("\n");
    }
    return this.books.keySet().size()
        + " book on shelf: "
        + shelfNumber
        + " : " + this.subject
        + "\n" + bookList;
  }

}