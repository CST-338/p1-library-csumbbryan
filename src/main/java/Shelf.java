import Utilities.Code;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author Bryan Zanoli
 * Title Library Project - Shelf.java
 * @since October 31, 2023
 * Abstract: the Shelf class used to represent a shelf within a digital library. Books can be added
 * to and removed from this shelf. Additional methods include the ability to get and set values
 * within/from the shelf as well as the ability to print out information about the shelf.
 */
public class Shelf {

  /**
   * Constant value used to keep track of the index for where {@link Shelf#shelfNumber} is stored in
   * the import file
   */
  public static final int SHELF_NUMBER_ = 0;
  /**
   * Constant value used to keep track of the index for where {@link Shelf#subject}
   * is stored in the import file.
   */
  public static final int SUBJECT = 1;
  /**
   * The integer which represents the given Shelf objects shelf number. Used to identify the shelf,
   * along with the subject.
   */
  private Integer shelfNumber;
  /**
   * The String corresponding the subject of the Shelf object. Used to identify and correlate
   * with the book subject for proper book placement.
   */
  private String subject;
  /**
   * A collection of Book objects along with the given book (key) count used as the HashMap's
   * value.
   */
  private HashMap<Book, Integer> books;

  /**
   * No parameter constructor. Initializes all fields to sets to default values.
   */
  public Shelf() {
    this.shelfNumber = 0;
    this.subject = "";
    this.books = new HashMap<>();
  }

  //Created the below so the code would compile.
  /**
   * Takes below parameters to set values for shelfNumber and Subject.
   * Field 'books' set to new HashMap for initialization.
   * @param shelfNumber
   * @param subject
   *
   */
  public Shelf(int shelfNumber, String subject) {
    this.shelfNumber = shelfNumber;
    this.subject = subject;
    this.books = new HashMap<>();
  }

  /**
   * Used to check the shelf number associated with the shelf.
   * @return shelfNumber
   */
  public int getShelfNumber() {
    return shelfNumber;
  }

  /**
   * Takes an integer and sets that value to the current shelf.
   * @param shelfNumber
   */
  public void setShelfNumber(int shelfNumber) {
    this.shelfNumber = shelfNumber;
  }

  /**
   * Used to return the string value of the subject associated with shelf object.
   * @return subject associated with the shelf
   */
  public String getSubject() {
    return subject;
  }

  /**
   * Sets the subject of a given shelf with the provide String value.
   * @param subject
   */
  public void setSubject(String subject) {
    this.subject = subject;
  }

  /**
   * Used to obtain the books HashMap for all books associated with the shelf.
   * @return </HashMap> books
   */
  public HashMap<Book, Integer> getBooks() {
    return books;
  }

  /**
   * Takes a hashmap of books as an input parameter and sets that to the books hashmap associated
   * with the shelf object.
   * @param books
   */
  public void setBooks(HashMap<Book, Integer> books) {
    this.books = books;
  }

  /**
   * IntelliJ created equals method to perform true comparison between two shelf objects.
   * @param o
   * @return true or false
   */
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

  /**
   * IntelliJ created hashCode method to create and return the hash for the shelf object.
   * @return hash of shelfNumber and subject
   */
  @Override
  public int hashCode() {
    return Objects.hash(shelfNumber, subject);
  }

  /**
   * Used to return a String representation of the Shelf object, including the
   * {@link Shelf#shelfNumber} and {@link Shelf#subject}
   * @return String shelfNumber + " " + subject
   */
  public String toString() {
    return shelfNumber + " : " + subject;
  }

  /**
   * Used to retrieve the total quantity of a specific book for a given shelf. If the shelf
   * does not contain the book referenced, then the method will return a -1.
   * @param book
   * @return int count of book or -1 if book not present
   */
  public int getBookCount(Book book) {
    return this.books.containsKey(book) ? this.books.get(book) : -1;
  }

  /**
   * Adds a book object to the shelf. If book already exists on the shelf, the count of the book
   * incremented by 1. If the book does not exist on the shelf and both book subject and
   * {@link Shelf#subject} match, a new book is added with book count set to 1.
   * If the book subject does not match the shelf subject, a
   * {@link Code#SHELF_SUBJECT_MISMATCH_ERROR} is returned. Otherwise, {@link Code#SUCCESS} is
   * returned.
   * @param book
   * @return Code value of Success if successful, otherwise shelf subject mismatch error.
   */
  public Code addBook(Book book) {
    for(Book hashBook : books.keySet()) {
      if(hashBook.equals(book)) {
        int count = books.get(book);
        books.replace(book, ++count);
        System.out.println(book.toString() + this.toString());
        return Code.SUCCESS;
      }
    }
    if(!books.containsKey(book) && book.getSubject().equals(this.subject)) {
      books.put(book, 1);
      System.out.println(book.toString() + this.toString());
      return Code.SUCCESS;
    }
    return Code.SHELF_SUBJECT_MISMATCH_ERROR;
  }

  /**
   * Removes a specified book from the given shelf. Checks whether the book exists on the shelf,
   * and if so, has greater than 0 copies. If not, returns a {@link Code#BOOK_NOT_IN_INVENTORY_ERROR}.
   * If so, decrements count of the given book on the shelf and returns {@link Code#SUCCESS}.
   * @param book
   * @return Code value of Success if successful, otherwise book not in inventory error.
   */
  public Code removeBook(Book book) {
    if(books.containsKey(book)) {
      if(books.get(book) == 0) {
        System.out.println(book.getTitle() + " is not on shelf " + this.subject);
        return Code.BOOK_NOT_IN_INVENTORY_ERROR;
      } else {
        int count = books.get(book);
        books.put(book, --count);
        return Code.SUCCESS;
      }

    } else {
      System.out.println("No copies of " + book.getTitle() + " remain on shelf " + this.subject);
      return Code.BOOK_NOT_IN_INVENTORY_ERROR;
    }
  }

  /**
   * Used to return a String of all books on the shelf. Obtains field values from each Book object
   * and stores them in a StringBuilder to be combined with Shelf field values.
   * @return String with number of books, shelf number, shelf subject, and a list of all books by
   * Title + Authur + ISBN + quantity of given book
   */
  public String listBooks() {
    StringBuilder bookList = new StringBuilder(new String());
    for(Book book : this.books.keySet()) {
      bookList.append(book.getTitle()).append(" by ").append(book.getAuthor()).append(" ISBN:")
          .append(book.getISBN()).append(" ").append(this.books.get(book)).append("\n");
    }
    return this.books.keySet().size()
        + " book on shelf: "
        + shelfNumber
        + " : " + this.subject
        + "\n" + bookList;
  }

  /**
   * Used to set the Book count associated with the shelf. Frequently called when adding a book.
   * Takes the Book object and a specific integer count, then checks whether book is in
   * inventory. If so, it will set the book count to the specified count. If not,
   * a {@link Code#BOOK_NOT_IN_INVENTORY_ERROR} will be returned.
   * @param book
   * @param count
   * @return Code of Success if book is found, and book not in inventory is book is not present.
   */
  //ADDED TO ADDRESS BOOK QUANTITY SETTING WHEN INITIALIZING
  public Code setBookCount(Book book, int count) {
    if(this.books.containsKey(book)) {
      this.books.replace(book, count);
      return Code.SUCCESS;
    } else {
      return Code.BOOK_NOT_IN_INVENTORY_ERROR;
    }

  }

}