import Utilities.Code;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Bryan Zanoli
 * @since OCtober 30, 2023
 *
 * Abstract: This class represents a library systems reader record, associating certain personal
 * information (name and phone number) with the reader's identifying value (library card) and list
 * of checked out books. Constants are defined to represent the index values for reader import
 * from file.
 *
 * The standard getter and setter methods are included to allow obtaining or modifying each private
 * field. In addition, methods are defined for the addition and removal of books from the reader's
 * list with proper checking to ensure the book is not already checked out in the case of adding a
 * book, or is still present in the case of removing a book.
 */

public class Reader {

  /**
   * Constant value used to keep track of the index for where {@link Reader#CARD_NUMBER_} is stored in
   * the import file
   */
  public static final int CARD_NUMBER_ = 0;
  /**
   * Constant value used to keep track of the index for where {@link Reader#NAME_} is stored in
   * the import file
   */
  public static final int NAME_ = 1;
  /**
   * Constant value used to keep track of the index for where {@link Reader#PHONE_} is stored in
   * the import file
   */
  public static final int PHONE_ = 2;
  /**
   * Constant value used to keep track of the index for where {@link Reader#BOOK_COUNT_} is stored in
   * the import file
   */
  public static final int BOOK_COUNT_ = 3;
  /**
   * Constant value used to keep track of the index for where {@link Reader#BOOK_START_} is stored in
   * the import file
   */
  public static final int BOOK_START_ = 4;

  /**
   * integer value representing the library card number for the given reader. Provided during
   * initialization of constructor and unique per reader.
   */
  private int cardNumber;
  /**
   * String name representing the library member's full name. Provided during initialization
   *  of constructor.
   */
  private String name;
  /**
   * Phone number for the given reader. Provided during initialization of constructor.
   */
  private String phone;
  /**
   * A list representing all books the reader has currently checked out. Each book within the list
   * is represented by a Book object.
   */
  private List<Book> books;

  /**
   * Constructor requiring below fields as input, and initializing the books List field to an empty
   * ArrayList.
   * @param cardNumber
   * @param name
   * @param phone
   */
  public Reader(int cardNumber, String name, String phone) {
    this.cardNumber = cardNumber;
    this.name = name;
    this.phone = phone;
    books = new ArrayList<>();
  }

  /**
   * Used to add a book to the list of checked out books for a reader. Checks whether a specified
   * book is already checked out and if so return's {@link Code#BOOK_ALREADY_CHECKED_OUT_ERROR}.
   * If book is not checked out, adds the Book object to the books array and
   * returns {@link Code#SUCCESS} once complete.
   * @param book
   * @return
   */
  public Code addBook(Book book) {
    for (Book compare : books) {
      if(compare.equals(book)) {
        return Code.BOOK_ALREADY_CHECKED_OUT_ERROR;
      }
    }
    books.add(book);
    return Code.SUCCESS;
  }

  /**
   * Used to remove a book from the list of checked out books for a reader (such as in the case
   * of returning a book to the library). Checks whether a specified book is checked out by
   * the reader and if not, returns {@link Code#READER_DOESNT_HAVE_BOOK_ERROR}. If reader does
   * have book checked out, removes the Book object from the books array and returns
   * {@link Code#SUCCESS}.
   * @param book
   * @return
   */
  public Code removeBook(Book book) { //SHOULD THIS BE UPDATED WITH HASBOOK?
    /*
    if(books.isEmpty()) {return Code.READER_DOESNT_HAVE_BOOK_ERROR;}
    for (Book compare : books) {
      if (compare.equals(book)) {
        books.remove(book);
        return Code.SUCCESS;
      } else {
        return Code.READER_DOESNT_HAVE_BOOK_ERROR;
      }
    }
    return Code.READER_COULD_NOT_REMOVE_BOOK_ERROR; */
    if (this.hasBook(book)) {
      books.remove(book);
      if(!this.hasBook(book)) {
        return Code.READER_COULD_NOT_REMOVE_BOOK_ERROR;
      } else {
        return Code.SUCCESS;
      }
    } else {
      return Code.READER_DOESNT_HAVE_BOOK_ERROR;
    }
  }

  /**
   * Used to determine whether a given reader has the specified book. Takes Book object and returns
   * a boolean value based on the result of the comparison.
   * @param book
   * @return {@code true} or {@code false} using comparison function between book in books
   * ArrayList and Book object passed in
   */
  public boolean hasBook(Book book) {
    for (Book compare : books) {
      if(compare.equals(book)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Used to obtain the total number of books the reader has checked out.
   * @return the size of the books ArrayList as an int
   */
  public int getBookCount() {
    return books.size();
  }

  /**
   * Used to obtain the ArrayList storing the Book objects for a give reader.
   * @return a List of Books
   */
  public List<Book> getBooks() {
    return books;
  }

  /**
   * Used to initialize the books list or replace all books within the list.
   * @param books
   */
  public void setBooks(List<Book> books) {
    this.books = books;
  }

  /**
   * Used to obtain the given reader's library card number.
   * @return an int as the card number.
   */
  public int getCardNumber() {
    return cardNumber;
  }

  /**
   * Used to set the given reader's library card number based on a passed integer.
   * @param cardNumber
   */
  public void setCardNumber(int cardNumber) {
    this.cardNumber = cardNumber;
  }

  /**
   * Used to obtain the name of the given reader.
   * @return the String value of the reader's name.
   */
  public String getName() {
    return name;
  }

  /**
   * Used to set the name for a give reader based on a passed String value representing the name.
   * @param name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Used to get the phone number for the given reader.
   * @return the reader's phone number as a String.
   */
  public String getPhone() {
    return phone;
  }

  /**
   * Used to set the phone number for a given reader based on the passed in String value.
   * @param phone
   */
  public void setPhone(String phone) {
    this.phone = phone;
  }

  /**
   * Automatically created equals method to compare two reader objects by
   * {@link Reader#cardNumber}, {@link Reader#name}, and {@link Reader#phone}
   * @param o
   * @return {@code true} if all fields above match or {@code false}
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Reader reader = (Reader) o;
    return cardNumber == reader.cardNumber && Objects.equals(name, reader.name)
        && Objects.equals(phone, reader.phone);
  }

  /**
   * Automatically created hashCode method to return the hash value of a given reader, based on
   * {@link Reader#cardNumber}, {@link Reader#name}, and {@link Reader#phone}
   * @return an integer representing the hash value
   */
  @Override
  public int hashCode() {
    return Objects.hash(cardNumber, name, phone);
  }

  /**
   * Creates a String representation of a reader by listing the {@link Reader#name} with the
   * {@link Reader#cardNumber} and a list of the associated {@link Reader#books}.
   * @return a String of the reader per above.
   */
  public String toString() {
    return name + " (#" + cardNumber + ") " + books;
  }

}