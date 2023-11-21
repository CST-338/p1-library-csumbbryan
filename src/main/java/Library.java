import Utilities.Code;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * @author Bryan Zanoli
 * @since October 30, 2023
 * <p>
 * Abstract: This class represents a digital library system. The library is initialized with a name
 * and contains a list of readers, a list of shelves, and a list of books. The library is
 * initialized with the information provided in the file. The library should be populated with the
 * books, shelves, and readers from the file. The method should return an error code indicating the
 * success or failure of the initialization.
 * <p>
 * The first line of the file will contain the number of books in the file. Each book will be
 * represented by a single line in the file. Each book line will contain the following information:
 * ISBN, title, subject, pageCount, author, dueDate. The next line of the file will contain the
 * number of shelves in the file. Each shelf will be represented by a single line in the file. Each
 * shelf line will contain the following information: shelfNumber, subject. The next line of the
 * file will contain the number of readers in the file. Each reader will be represented by a single
 * line in the file. Each reader line will contain the following information: cardNumber, name,
 * phone, bookCount, with the number of book ISBN and book Due Date equal to the bookCount value.
 * <p>
 * In addition to initialization of the Library, with methods for book, reader, and shelf
 * initialization, the library class contains getter and setter methods, as well as methods to
 * handle the administration of library management. This includes methods to add and remove books
 * from the library, add and remove shelves from the library, add and remove readers from the
 * library, and check out and return books to the library. Additional methods include methods to
 * list the books, shelves, and readers in the library, as well as methods to list the books on a
 * given shelf, and the books checked out by a given reader.
 */
public class Library {

  /**
   * Constant value used to keep track of the library's lending limit for each reader.
   */
  private static final int LENDING_LIMIT = 5;
  /**
   * Constant value used to keep track of the current integer value of the highest library card
   * number.
   */
  private static int libraryCard;
  /**
   * String value representing the name of the library.
   */
  private String name;
  /**
   * List of Reader objects representing the readers of the library.
   */
  private List<Reader> readers;
  /**
   * HashMap of Shelf objects representing the shelves of the library.
   */
  private HashMap<String, Shelf> shelves;
  /**
   * HashMap of Book objects representing the books of the library.
   */
  private HashMap<Book, Integer> books;

  /**
   * Constructor for Library object. Initializes the name of the library, and initializes the
   * ArrayList of readers, HashMap of shelves, and HashMap of books.
   *
   * @param name of the library
   */
  public Library(String name) {
    this.name = name;
    readers = new ArrayList<>();
    shelves = new HashMap<>();
    books = new HashMap<>();
  }

  /**
   * Method is used to convert a String representing an integer to an integer value. The method
   * accepts as parameters the String to be converted, and a Code value to be returned in the event
   * of an error. The method attempts to convert the String to an integer using the parseInt method.
   * If the String cannot be converted, the method prints out the String which caused the error, and
   * the error message associated with the Code value. The method then returns the Code value
   * associated with the error.
   *
   * @param recordCountString String to be converted to an integer
   * @param code              Code value to be returned in the event of an error
   * @return if successful, returns count of the number associated with the String, such as book
   * count or shelf count. If unsuccessful, returns numerical code associated with error.
   */
  static public Integer convertInt(String recordCountString, Code code) {
    Integer count;
    try {
      count = Integer.parseInt(recordCountString);
    } catch (NumberFormatException e) {
      System.out.println("Value which caused the error: " + recordCountString);
      System.out.println("Error message: " + code.getMessage());

      //This switch statement doesn't seem to work.
      switch (code) {
        case BOOK_COUNT_ERROR:
          System.out.println("Error: Could not read number of books");
          break;
        case PAGE_COUNT_ERROR:
          System.out.println("Error: could not parse page count");
          break;
        case DATE_CONVERSION_ERROR:
          System.out.println("Error: Could not parse date component");
          break;
        default:
          System.out.println("Error: Unknown conversion error");
          break;
      }
      return code.getCode();
    }
    return count;
  }

  /**
   * Method is used to convert a String representing a date to a LocalDate object. The method
   * accepts as parameters the String to be converted, and a Code value to be returned in the event
   * of an error (though no error should be found as if error occurs, a default date will be
   * returned). The method attempts to convert the String to a LocalDate object after converting
   * each of the Month, Day, and Year values into an integer. If the String cannot be converted, the
   * method prints out the String which caused the error, and the error message associated with the
   * Code value. The method then returns the Code value associated with the error.
   *
   * @param date      String to be converted to a LocalDate object
   * @param errorCode Code value to be returned in the event of an error
   * @return if successful, returns LocalDate object associated with the String. If unsuccessful,
   * returns LocalDate with 01-jan-1970 value.
   */
  static public LocalDate convertDate(String date, Code errorCode) {
    Integer year;
    Integer month;
    Integer day;
    if (date.equals("0000")) {
      return LocalDate.of(1970, 1, 1);
    } else {
      String[] tempStringArray = date.split("-");
      if (tempStringArray.length != 3 ||
          tempStringArray[0].length() != 4 ||
          tempStringArray[1].length() != 2 ||
          tempStringArray[2].length() != 2) {
        System.out.println("ERROR: date conversion error, could not parse " + date);
        System.out.println("Using default date (01-jan-1970)");
        return LocalDate.of(1970, 1, 1);
      }
      year = convertInt(tempStringArray[0], Code.DATE_CONVERSION_ERROR);
      month = convertInt(tempStringArray[1], Code.DATE_CONVERSION_ERROR);
      day = convertInt(tempStringArray[2], Code.DATE_CONVERSION_ERROR);
      if (year < 0) {
        System.out.println("Error converting date: Year " + year);
      }
      if (month < 0) {
        System.out.println("Error converting date: Month " + month);
      }
      if (day < 0) {
        System.out.println("Error converting date: Day " + day);
      }
      if (!(year < 0 && month < 0 && day < 0)) {
        return LocalDate.of(year, month, day);
      } else {
        System.out.println("Using default date (01-jan-1970)");
        return LocalDate.of(1970, 1, 1);
      }
    }
  }

  /**
   * Method is used to obtain the current libraryCard value plus 1.
   * @return
   */
  static public int getLibraryCardNumber() {
    return libraryCard++; //Should this also increment the variable, or just return the value?
  }

  /**
   * This method will initialize the library with the information provided in the file. The library
   * should be populated with the books, shelves, and readers from the file. The method should
   * return an error code indicating the success or failure of the initialization.
   * <p>
   * The first line of the file will contain the number of books in the file. Each book will be
   * represented by a single line in the file. Each book line will contain the following
   * information: ISBN, title, subject, pageCount, author, dueDate
   * <p>
   * The next line of the file will contain the number of shelves in the file. Each shelf will be
   * represented by a single line in the file. Each shelf line will contain the following
   * information: shelfNumber, subject
   * <p>
   * The next line of the file will contain the number of readers in the file. Each reader will be
   * represented by a single line in the file. Each reader line will contain the following
   * information: cardNumber, name, phone, bookCount, with the number of book ISBN and book Due Date
   * equal to the bookCount value.
   * <p>
   * The method should return an error code indicating the success or failure of the
   * initialization.
   * <p>
   * If the file is not found, the method should return Code.FILE_NOT_FOUND_ERROR. If the book count
   * is negative, the method should return Code.BOOK_COUNT_ERROR. If the shelf count is negative,
   * the method should return Code.SHELF_COUNT_ERROR. If the reader count is negative, the method
   * should return Code.READER_COUNT_ERROR. If the page count is negative, the method should return
   * Code.PAGE_COUNT_ERROR. If the date is not formatted correctly, the method should return
   * Code.DATE_CONVERSION_ERROR. If the shelf number is negative, the method should return
   * Code.SHELF_NUMBER_PARSE_ERROR. If the reader card number is negative, the method should return
   * Code.READER_CARD_NUMBER_ERROR. If the reader book count is negative, the method should return
   * Code.READER_BOOK_COUNT_ERROR. If the reader book count does not match the number of books, the
   * method should return Code.READER_BOOK_COUNT_MISMATCH_ERROR. If the reader book due date is not
   * formatted correctly, the method should return Code.READER_BOOK_DUE_DATE  *
   *
   * @param filename with the information to initialize the library
   * @return Code indicating success or failure of initialization for initBooks, initReader, and
   * initShelves.
   */
  //This method may be missing: return error code by parseInt return value
  public Code init(String filename) {
    Code code = null; //investigate this
    File file = new File(filename);
    Scanner fileScan;
    Integer bookCount;
    Integer shelfCount;
    Integer readerCount;
    try {
      fileScan = new Scanner(file);
    } catch (FileNotFoundException e) {
      return Code.FILE_NOT_FOUND_ERROR;
    }
    //obtain first number from first line
    if (fileScan.hasNext()) {
      bookCount = convertInt(fileScan.nextLine(), Code.BOOK_COUNT_ERROR);
      if (bookCount < 0) {
        return errorCode(bookCount);
      } else {
        code = initBooks(bookCount, fileScan);
        listBooks();
      }
      //call initShelves using number from next line, and pass scanner
    }
    if (fileScan.hasNext() && code == Code.SUCCESS) {
      shelfCount = convertInt(fileScan.nextLine(), Code.SHELF_COUNT_ERROR);
      if (shelfCount < 0) {
        return Code.SHELF_COUNT_ERROR;
      } else {
        code = initShelves(shelfCount, fileScan);
        listShelves();
      }
    }
    //call initReader using number from line, and pass scanner
    if (fileScan.hasNext() && code == Code.SUCCESS) {
      readerCount = convertInt(fileScan.nextLine(), Code.READER_COUNT_ERROR);
      if (readerCount < 0) {
        return Code.READER_COUNT_ERROR;
      } else {
        code = initReader(readerCount, fileScan);
        listReaders();
      }
    }
    fileScan.close();
    return code;
  }

  /**
   * This method will return the name of the library.
   *
   * @return the name of the library
   */
  public String getName() {
    return this.name;
  }

  /**
   * Method is used to initialize the books in the library, upon reading them from the import file.
   * A scan corresponding to a Scanner object is passed in, along with the count of books the
   * initializer should expect to read and store to the Library object.
   * <p>
   * The method checks on various conditions to ensure the file contains book entries to read, and
   * each line is properly formatted. If there are no book entries, the {@link Code#LIBRARY_ERROR}
   * is returned. If the line is not properly formatted, the {@link Code#BOOK_COUNT_ERROR} is
   * returned. If the page count is not a positive integer, the {@link Code#PAGE_COUNT_ERROR} is
   * returned. If the date is not properly formatted, the {@link Code#DATE_CONVERSION_ERROR} is
   * returned. Otherwise, a new Book object is created for each book entry with the values from the
   * file passed in, based on the index of the String array. The addBook method is called to add a
   * book to the Library object's HashMap of books. The Code value from addBook is stored and
   * returned.
   *
   * @param bookCount represents the total number of book entries to process
   * @param scan      is a Scanner object which is initialized to read in from the import file,
   *                  pointing to the line with the first book entry.
   * @return a Code value indicating the success or failure of the method.
   */
  private Code initBooks(int bookCount, Scanner scan) {
    Code code = null;
    if (bookCount < 1) {
      return Code.LIBRARY_ERROR;
    }
    for (int i = 0; i < bookCount; i++) {
      List<String> tempString = new ArrayList<>();
      //USING RECOMMENDED COLLECTIONS.ADDALL METHOD
      Collections.addAll(tempString, scan.nextLine().split(","));

      //ADDED BELOW FUNCTIONALITY TO AVOID PARSING IMPROPER ENTRIES
      if (tempString.size() != Book.DUE_DATE_ + 1) {
        return Code.BOOK_COUNT_ERROR;
      }
      if (convertInt(tempString.get(Book.PAGE_COUNT_), Code.PAGE_COUNT_ERROR) <= 0) {
        return Code.PAGE_COUNT_ERROR;
      }
      if (convertDate(tempString.get(Book.DUE_DATE_), Code.DUE_DATE_ERROR) == null) {
        return Code.DATE_CONVERSION_ERROR;
      }
      Book book = new Book(
          tempString.get(Book.ISBN_),
          tempString.get(Book.TITLE_),
          tempString.get(Book.SUBJECT_),
          convertInt(tempString.get(Book.PAGE_COUNT_), Code.PAGE_COUNT_ERROR),
          tempString.get(Book.AUTHOR_),
          convertDate(tempString.get(Book.DUE_DATE_), Code.DUE_DATE_ERROR));
      addBook(book);
    }
    return Code.SUCCESS;
  }

  /**
   * Method is used to initialize the shelves in the library, upon reading them from the import
   * file. A scan corresponding to a Scanner object is passed in, along with the count of shelves
   * the initializer should expect to read and store to the Library object.
   * <p>
   * The method checks on various conditions to ensure the file contains shelf entries to read, and
   * each line is properly formatted. If there are no shelf entries, the
   * {@link Code#SHELF_COUNT_ERROR} is returned. If the line is not properly formatted, the
   * {@link Code#SHELF_NUMBER_PARSE_ERROR} is returned. Otherwise, a new Shelf object is created for
   * each shelf entry with the values from the file passed in, based on the index of the String
   * array. The addBook method is called to add a shelf to the Library object's HashMap of shelves.
   * The Code value from addShelves is stored and returned.
   *
   * @param shelfCount represents the total number of shelf entries to process
   * @param scan       is a Scanner object which is initialized to read in from the import file,
   *                   pointing to the line with the first shelf entry.
   * @return a Code value indicating the success or failure of the method.
   */
  private Code initShelves(int shelfCount, Scanner scan) {
    Code code = null;
    if (shelfCount < 1) {
      return Code.SHELF_COUNT_ERROR;
    }
    for (int i = 0; i < shelfCount; i++) {
      List<String> tempString = new ArrayList<>();
      //USING RECOMMENDED COLLECTIONS.ADDALL METHOD
      Collections.addAll(tempString, scan.nextLine().split(","));
      if (tempString.isEmpty()) {
        return Code.SHELF_COUNT_ERROR;
      }

      if (convertInt(tempString.get(Shelf.SHELF_NUMBER_), Code.SHELF_COUNT_ERROR) < 0) {
        return Code.SHELF_NUMBER_PARSE_ERROR;
      }
      Shelf shelf = new Shelf(
          convertInt(tempString.get(Shelf.SHELF_NUMBER_), Code.SHELF_COUNT_ERROR),
          tempString.get(Shelf.SUBJECT));
      code = addShelf(shelf);
    }
    if (shelves.size() == shelfCount) {
      return Code.SUCCESS;
    } else {
      return code;
    }
  }

  /**
   * Method is used to initialize the readers in the library, upon reading them from the import
   * file. A scan corresponding to a Scanner object is passed in, along with the count of readers
   * the initializer should expect to read and store to the Library object.
   * <p>
   * The method checks on various conditions to ensure the file contains reader entries to read, and
   * each line is properly formatted. If there are no reader entries, the
   * {@link Code#READER_COUNT_ERROR} is returned. If the line is not properly formatted, the
   * {@link Code#READER_COUNT_ERROR} is returned. Otherwise, a new Reader object is created for each
   * reader entry with the values from the file passed in, based on the index of the String array.
   * The addReader method is called to add a reader to the Library object's HashMap of readers. The
   * Code value from addReader is stored. For each reader, a book is added to the reader's list of
   * books, based on the number of books parsed from the file. The getBookByISBN method is called to
   * find the book based on the ISBN parsed from the file. If the book is not found, an error is
   * posted to the screen in form "ERROR: could not find book for [reader name]". Otherwise, the
   * book is checked out to the reader using checkOutBook method. No Codes are returned if a book
   * fails to be checked out as long as the reader is successfully added.
   *
   * @param readerCount represents the total number of reader entries to process
   * @param scan        is a Scanner object which is initialized to read in from the import file,
   *                    pointing to the line with the first reader entry.
   * @return a Code value indicating the success or failure of the method.
   */
  private Code initReader(int readerCount, Scanner scan) {
    Code code = null;
    if (readerCount < 1) {
      return Code.READER_COUNT_ERROR;
    }
    for (int i = 0; i < readerCount; i++) {
      List<String> tempString = new ArrayList<>();
      //USING RECOMMENDED COLLECTIONS.ADDALL METHOD
      Collections.addAll(tempString, scan.nextLine().split(","));
      if (tempString.isEmpty()) {
        return Code.READER_COUNT_ERROR;
      }

      Reader reader = new Reader(
          convertInt(tempString.get(Reader.CARD_NUMBER_), Code.READER_COUNT_ERROR),
          tempString.get(Reader.NAME_),
          tempString.get(Reader.PHONE_));
      code = addReader(reader);
      //for each reader, add a book, based on parsing book count
      for (int j = Reader.BOOK_START_; j < tempString.size(); j += 2) {
        //find books based on using getBookByISBN
        Book book = getBookByISBN(tempString.get(j));
        if (book == null) {
          System.out.println("ERROR" + ": Could not find book for "
              + reader.getName()); //ADDED LINE FOR FURTHER TROUBLESHOOTING
          break;
        } else {
          book.setDueDate(convertDate(tempString.get(j + 1), Code.DUE_DATE_ERROR));
          checkOutBook(reader, book);
        }
      }
    }
    return code;
  }

  /**
   * Method is used to add a book to the library. The method checks whether the book is already in
   * the library. If so, the book count is incremented. Otherwise, the book is added to the library
   * and the book count is set to 1. The method then checks whether the book's subject is already
   * represented by a shelf. If so, the book is added to the shelf. Otherwise, an error is printed
   * stating that no shelf exists, and {@link Code#SHELF_EXISTS_ERROR} is returned.
   *
   * @param newBook represents the book to be added to the library
   * @return a Code value indicating the success or failure of the method.
   */
  public Code addBook(Book newBook) {
    //If HashMap of books already contains a book, increment count and print string
    if (books.containsKey(newBook)) {
      int bookCount = books.get(newBook);
      books.replace(newBook, ++bookCount);
      System.out.println(bookCount + " copies of " + newBook.getTitle() + " in the stacks");
    } else { //If book does not exist, add book to HashMap of books and set count to 1
      books.put(newBook, 1);
      System.out.println(newBook.getTitle() + " added to the stacks");
    }
    //If there is a shelf with a matching subject, add to shelf and return Code.SUCCESS
    if (shelves.containsKey(newBook.getSubject())) {
      return addBookToShelf(newBook, getShelf(newBook.getSubject()));
    } else {
      System.out.println("No shelf for " + newBook.getSubject() + " books");
      return Code.SHELF_EXISTS_ERROR;
    }
    //Otherwise print out "No shelf for [subject] books" with return Code.SHELF_EXISTS_ERROR
  }

  /**
   * Method is used to return a book to the library (check it back in). The method accepts as passed
   * in the Reader object who is returning the book, and the Book object being returned. A set of
   * conditions are checked before the book is returned. If the reader does not have the book in
   * their list, the method prints out an error and returns
   * {@link Code#READER_DOESNT_HAVE_BOOK_ERROR}. If the Library does not contain the book, the
   * method prints out an error and returns {@link Code#BOOK_NOT_IN_INVENTORY_ERROR}. If the book is
   * successfully returned, the method calls {@link #returnBook(Book)} in an attempt to return the
   * book. If the {@link #returnBook(Book)} method is successful, the method returns the associated
   * Code value. Otherwise the method prints out an error and returns the current Code value.
   *
   * @param reader the Reader object which corresponds to the reader returning the book
   * @param book   the Book object which corresponds to the book being returned
   * @return code value indicating success or failure type depending on execution of the method
   */
  public Code returnBook(Reader reader, Book book) {
    //if reader does not have the book in their list, return error
    if (!reader.hasBook(book)) {
      System.out.println(reader.getName() + " doesn't have " + book.getTitle() + " checked out");
      return Code.READER_DOESNT_HAVE_BOOK_ERROR;
    }
    //check if book exists in library, else return error
    //remove book from reader
    Code code = null;
    if (!books.containsKey(book)) {
      return Code.BOOK_NOT_IN_INVENTORY_ERROR;
    } else {
      System.out.println(reader.getName() + " is returning " + book);
      code = reader.removeBook(book);
    }
    //If successful, call returnBook(book) and return resulting code
    //If not successful, print error and return code
    if (code == Code.SUCCESS) {
      return returnBook(book);
    } else {
      System.out.println("Could not return " + book);
      return code;
    }
  }

  /**
   * Method is used to return a book to the library (check it back in). This method is called by
   * {@link #returnBook(Reader, Book)} after the book is successfully removed from the reader's list
   * to add it back to the library and shelf with matching subject. If the shelf does not exist, an
   * error is printed and the method returns a {@link Code#SHELF_EXISTS_ERROR}. Otherwise, the book
   * is added to the shelf associated with the book's subject.
   *
   * @param book the Book object which corresponds to the book being returned
   * @return code value indicating success or failure type depending on execution of the method
   */
  public Code returnBook(Book book) {
    if (!shelves.containsKey(book.getSubject())) {
      System.out.println("No shelf for " + book);
      return Code.SHELF_EXISTS_ERROR;
    }
    Code code = getShelf(book.getSubject()).addBook(book);
    return code;
  }

  /**
   * Method is used to add a specified book to a specified shelf. The method uses
   * {@link #returnBook(Book)} to attempt to return the book to the library. If the book is
   * successfully returned, the method returns {@link Code#SUCCESS}. Otherwise, the method checks
   * whether the book's subject matches a shelf's subject and if not, returns a
   * {@link Code#SHELF_SUBJECT_MISMATCH_ERROR}. If there is a matching subject, the method calls the
   * shelf's addBook method and if successful, returns {@link Code#SUCCESS}. Otherwise, the method
   * prints out an error and returns the associated code.
   *
   * @param book
   * @param shelf
   * @return
   */
  private Code addBookToShelf(Book book, Shelf shelf) {
    Code code = returnBook(book);
    if (code == Code.SUCCESS) {
      return Code.SUCCESS;
    }
    if (!book.getSubject().equals(shelf.getSubject())) {
      return Code.SHELF_SUBJECT_MISMATCH_ERROR;
    }
    code = shelf.addBook(book);
    if (code == Code.SUCCESS) {
      return Code.SUCCESS;
    } else {
      System.out.println("Could not add " + book + " to shelf");
      return code;
    }
  }

  /**
   * Method is used to print out the list of books in the library. The method iterates through the
   * HashMap of books and prints out the book title and number of copies. The method returns the
   * total number of books in the library as an integer.
   *
   * @return the count of the books in the library.
   */
  public int listBooks() {
    int bookCount = 0;
    for (Book book : books.keySet()) {
      System.out.println(books.get(book) + " copies of " + book.toString());
      bookCount += books.get(book);
    }
    return bookCount;
  }

  /**
   * Method is used to check out a specified book from the library for a specified reader. The
   * method checks whether the reader exists in the library. If not, the method prints out an error
   * and returns {@link Code#READER_NOT_IN_LIBRARY_ERROR}. If the reader has reached the lending
   * limit, the method prints out an error and returns {@link Code#BOOK_LIMIT_REACHED_ERROR}. If the
   * book is not in the library, the method prints out an error and returns
   * {@link Code#BOOK_NOT_IN_INVENTORY_ERROR}. If the book's subject does not have a shelf, the
   * method prints out an error and returns {@link Code#SHELF_EXISTS_ERROR}. If the shelf does not
   * have enough copies of the book, the method prints out an error and returns
   * {@link Code#BOOK_NOT_IN_INVENTORY_ERROR}. If the reader's addBook method returns an error, the
   * method prints out an error and returns the associated code. The method then attempts to remove
   * the book from the shelf and returns the Code (successful or otherwise) associated with its
   * return.
   *
   * @param reader the reader checking out the book
   * @param book   the book being checked out
   * @return Code value indicating success or failure type depending on execution of the method
   */
  public Code checkOutBook(Reader reader, Book book) {
    //Check if reader exists, else print out error
    if (!readers.contains(reader)) {
      System.out.println(reader.getName() + " doesn't have an account here");
      return Code.READER_NOT_IN_LIBRARY_ERROR;
    }

    //Check if reader is under spending limit, else print out error
    if (reader.getBooks().size() >= Library.LENDING_LIMIT) {
      System.out.println(
          reader.getName() + " has reached the lending limit, " + Library.LENDING_LIMIT);
      return Code.BOOK_LIMIT_REACHED_ERROR;
    }

    //Check if book is in HashMap, else print error
    if (!books.containsKey(book)) {
      System.out.println("ERROR: could not find " + book);
      return Code.BOOK_NOT_IN_INVENTORY_ERROR;
    }

    //Check if reader already has book checked out
    if(reader.hasBook(book)) {
      System.out.println(reader.getName() + " already has " + book.getTitle() + " checked out");
      return Code.BOOK_ALREADY_CHECKED_OUT_ERROR;
    }

    //Check if book is on shelf, else print error
    if (getShelf(book.getSubject()) == null) {
      System.out.println("no shelf for " + book.getSubject() + " books!");
      return Code.SHELF_EXISTS_ERROR;
    }

    //Check if shelf has enough copies (1 or more), else print error
    //Shelf tempShelf = getShelf(book.getSubject());
    //int bookCount = getShelf(book.getSubject()).getBookCount(book);
    if (getShelf(book.getSubject()).getBookCount(book) < 1) {
      System.out.println("ERROR: no copies of " + book + " remain");
      return Code.BOOK_NOT_IN_INVENTORY_ERROR;
    }

    //Check if reader.addBook() returns success, else print error
    Code codeReader = reader.addBook(book);
    if (codeReader != Code.SUCCESS) {
      System.out.println("Couldn't checkout " + book);
      return codeReader;
    }

    //Check if shelf.removeBook returns success, else print error
    Code code = getShelf(book.getSubject()).removeBook(book);
    return code;
  }

  /**
   * Method is used retrieve a book based on ISBN value. The method passes in an ISBN String and
   * iterates through the HashMap of books to find a book with a matching ISBN. If a book is found,
   * the method returns the book. Otherwise, the method prints out an error and returns null.
   *
   * @param isbn the ISBN value in String form of the book being searched for
   * @return the Book object with the matching ISBN value or null if the Book is not found
   */
  public Book getBookByISBN(String isbn) {
    Book book;
    for (Book tempBook : books.keySet()) {
      if (tempBook.getISBN().equals(isbn)) {
        book = tempBook;
        return book;
      }
    }
    System.out.println("ERROR: Could not find book with ISBN: " + isbn);
    return null;
  }

  /**
   * Method calls listShelves with a showbooks boolean value of false.
   *
   * @return integer return value of the listShelves(boolean) method.
   */
  public int listShelves() {
    return listShelves(false);
  }

  /**
   * Method is used to print out the list of shelves in the library. A boolean value, showbooks, is
   * passed into the method to determine whether the books on the shelves should be printed. The
   * method iterates through the HashMap of shelves and prints out the shelf number and subject, as
   * well as each book on the shelf if showbooks is true. The method returns the total number of
   * shelves in the library as an integer.
   *
   * @param showbooks a boolean value to determine whether to print out the books on the shelves
   * @return the number of shelves in the library
   */
  public int listShelves(boolean showbooks) {
    for (Shelf shelf : shelves.values()) {
      if (showbooks) {
        shelf.listBooks();
      } else {
        System.out.println(shelf.toString());
      }
    }
    return shelves.size();
  }

  /**
   * Method is used to add a shelf by a passed in String value representing the shelf Subject to the
   * library. The method checks whether the shelf is already in the library. If so, the method
   * prints out an error and returns {@link Code#SHELF_EXISTS_ERROR}. Otherwise, the
   * {@link #addShelf(Shelf)} method is called to add the shelf to the library and the associated
   * Code value is returned.
   *
   * @param shelfSubject the subject of the shelf being added
   * @return Code value indicating success or failure type depending on execution of the method
   */
  public Code addShelf(String shelfSubject) {
    if (shelves.containsKey(shelfSubject)) {
      System.out.println("ERROR: (addShelf(String) Shelf already exists " + shelfSubject);
      return Code.SHELF_EXISTS_ERROR;
    } else {
      int shelfCount = shelves.size() + 1;
      Shelf newShelf = new Shelf(shelfCount, shelfSubject);
      return addShelf(newShelf);
    }
  }

  /**
   * Method is used to add a shelf by a passed in Shelf object to the library. The method checks
   * whether the shelf is already in the library. If so, the method prints out an error and returns
   * {@link Code#SHELF_EXISTS_ERROR}. If the shelf number is less than or equal to 0, the method
   * sets the shelf count to the current HashMap shelves size + 1. The method then iterates through
   * the HashMap of books and, using the {@link #addBookToShelf(Book, Shelf)} method, adds the book
   * to the shelf if the book's subject matches the shelf's subject, storing the code value for
   * later return.The method then calls the {@link Shelf#setBookCount(Book, int)} method to set the
   * book's count on the shelf.
   *
   * @param shelf the Shelf object being added
   * @return Code value indicating success or failure type depending on execution of the method
   */
  public Code addShelf(Shelf shelf) {
    //No instruction on whether to use
    Code code = Code.SUCCESS;
    if (shelves.containsValue(shelf)) {
      System.out.println("ERROR: Shelf already exists " + shelf.getSubject());
      return Code.SHELF_EXISTS_ERROR;
    }
    //Below if statement not specified in instruction--added to ensure shelf always has shelf number
    if (shelf.getShelfNumber() <= 0) {
      int shelfCount = shelves.size() + 1;
      shelf.setShelfNumber(shelfCount);
    }
    shelves.put(shelf.getSubject(), shelf);
    for (Book book : books.keySet()) {
      if (book.getSubject().equals(shelf.getSubject())) {
        code = addBookToShelf(book, shelf);
        shelf.setBookCount(book, books.get(book)); //ADDED THIS TO ADDRESS SETTING COUNT ON INIT
      }
    }
    return code;
  }

  /**
   * The Method is used to obtain a Shelf object by passing in the integer value representing the
   * shelfNumber. The method iterates through the HashMap of shelves and returns the shelf with a
   * matching shelfNumber. If no shelf is found, the method prints out an error and returns null.
   *
   * @param shelfNumber the integer value representing the shelfNumber of the shelf being searched
   * @return the Shelf object with the matching shelfNumber or null if the Shelf is not found
   */
  public Shelf getShelf(Integer shelfNumber) {
    for (Shelf currentShelf : shelves.values()) {
      if (currentShelf.getShelfNumber() == shelfNumber) {
        return currentShelf;
      }
    }
    //If for loop doesn't find shelf, print no shelf found and return null
    System.out.println("No shelf number " + shelfNumber + " found");
    return null;
  }

  /**
   * Method is used to obtain a Shelf object by passing in the String value representing the
   * shelfSubject. The method iterates through the HashMap of shelves and returns the shelf with a
   * matching shelfSubject. If no shelf is found, the method prints out an error and returns null.
   *
   * @param subject the String value representing the shelfSubject of the shelf being searched
   * @return the Shelf object with the matching shelfSubject or null if the Shelf is not found
   */
  public Shelf getShelf(String subject) {
    if (shelves.containsKey(subject)) {
      return shelves.get(subject);//Not implemented
    } else {
      System.out.println("No shelf for " + subject + " found");
      return null;
    }
  }

  /**
   * The method prints out the readers in the library. The method iterates through the HashMap of
   * readers and uses the {@link Reader#toString()} method to print out the reader's information.
   *
   * @return the number of readers in the library
   */
  public int listReaders() {
    for (Reader reader : readers) {
      System.out.println(reader.toString());
    }
    return readers.size();
  }

  /**
   * The method prints out the readers in the library and the books they have checked out. The
   * method iterates through the HashMap of readers and uses the {@link Reader#toString()} method to
   * print out the reader's information. If showBooks is true, the method uses the
   * {@link Reader#getBooks()} method to print out the books the reader has checked out.
   *
   * @param showBooks a boolean value to determine whether to print out the books checked out by the
   *                  readers
   * @return the number of readers in the library
   */
  public int listReaders(boolean showBooks) {
    if (showBooks) {
      for (Reader reader : readers) {
        System.out.println(reader.getName() + " has the following books:");
        System.out.println(reader.getBooks());
      }
    } else {
      return listReaders();
    }
    return readers.size();
  }

  /**
   * Method is used to obtain a Reader object by passing in the integer value representing the
   * cardNumber. The method iterates through the HashMap of readers and returns the reader with a
   * matching cardNumber. If no reader is found, the method prints out an error and returns null.
   *
   * @param cardNumber the integer value representing the cardNumber of the reader being searched
   * @return the Reader object with the matching cardNumber or null if the Reader is not found
   */
  public Reader getReaderByCard(int cardNumber) {
    for (Reader reader : readers) {
      if (reader.getCardNumber() == cardNumber) {
        return reader;
      }
    }
    System.out.println("Could not find a reader with card #" + cardNumber);
    return null;
  }

  /**
   * The method attempts to add a specified reader to the library. The method checks whether the
   * reader is already in the library. If so, the method prints out an error and returns
   * {@link Code#READER_ALREADY_EXISTS_ERROR}. If the specified reader's cardNumber is equal to
   * another reader's cardNumber, the method prints out an error and returns
   * {@link Code#READER_CARD_NUMBER_ERROR}. If no conflicting condition is met, the reader is added
   * to {@link Library#readers} and the line "reader.getName() added to the library!" is printed. If
   * the reader's cardNumber is greater than the current libraryCard value, the libraryCard value is
   * set to the reader's cardNumber. The method returns {@link Code#SUCCESS}.
   *
   * @param reader the reader being added to the library
   * @return Code value indicating success or failure type depending on execution of the method
   */
  public Code addReader(Reader reader) {
    if (readers.contains(reader)) {
      return Code.READER_ALREADY_EXISTS_ERROR;
    }

    for (Reader rdr : readers) {
      if (rdr.getCardNumber() == reader.getCardNumber()) {
        System.out.println(rdr.getName()
            + " and "
            + reader.getName()
            + " have the same card number!");
        return Code.READER_CARD_NUMBER_ERROR;
      }
    }
    readers.add(reader);
    System.out.println(reader.getName() + " added to the library!");
    if (reader.getCardNumber() > libraryCard) {
      libraryCard = reader.getCardNumber();
    }
    return Code.SUCCESS;
  }

  /**
   * Method is used to remove a reader from the library. The method checks whether the reader has
   * books still checked out, and if so, prints out an error and returns
   * {@link Code#READER_STILL_HAS_BOOKS_ERROR}. If the reader is not in the library, the method
   * prints out an error and returns {@link Code#READER_NOT_IN_LIBRARY_ERROR}. Otherwise, the reader
   * is removed from the library and {@link Code#SUCCESS} is returned.
   *
   * @param reader the reader being removed from the library
   * @return Code value indicating success or failure type depending on execution of the method
   */
  public Code removeReader(Reader reader) {
    if (reader.getBookCount() > 0) {
      System.out.println(reader.getName() + " must return all books!");
      return Code.READER_STILL_HAS_BOOKS_ERROR;
    }
    if (!readers.contains(reader)) {
      System.out.println(reader + " is not part of this library");
      return Code.READER_NOT_IN_LIBRARY_ERROR;
    } else {
      readers.remove(reader);
      return Code.SUCCESS;
    }
  }

  /**
   * Method is used to convert an integer in a Code object. If the Code object is not found based on
   * codeNumber, a {@link Code#UNKNOWN_ERROR} is returned.
   *
   * @param codeNumber the integer value representing the Code object
   * @return the Code object associated with the codeNumber
   */
  private Code errorCode(int codeNumber) {
    for (Code code : Code.values()) {
      if (code.getCode() == codeNumber) {
        return code;
      }
    }
    return Code.UNKNOWN_ERROR;
  }
}