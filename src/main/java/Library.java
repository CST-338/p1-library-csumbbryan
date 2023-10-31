import Utilities.Code;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Library {
  private static final int LENDING_LIMIT = 5;
  private String name;
  private static int libraryCard;
  private List<Reader> readers;
  private HashMap<String, Shelf> shelves;
  private HashMap<Book, Integer> books;

  public Library(String name) {
    System.out.println("Not implemented");
  }

  public Code init(String filename) {
    Code code = Code.SUCCESS; //investigate this
    File file = new File(filename);
    Scanner fileScan;
    int bookCount = 0;
    int shelfCount = 0;
    int readerCount = 0;
    try {
      fileScan = new Scanner(file);
    } catch (FileNotFoundException e) {
      return Code.FILE_NOT_FOUND_ERROR;
    }
    //obtain first number from first line
    if(fileScan.hasNext()) {
      bookCount = convertInt(fileScan.nextLine(), code);
    }
    //call initBooks using number from first line, and pass scanner
    if(bookCount < 0) {
      //handle code to return proper error code

    } else {
      initBooks(bookCount, fileScan);
      listBooks();
    }
    if(fileScan.hasNext()) {
      shelfCount = convertInt(fileScan.nextLine(), code);
    }
    if(shelfCount < 0) {
      //handle code to return proper error code
    } else {
      initShelves(shelfCount, fileScan);
      listShelves();
    }
    if(fileScan.hasNext()) {
      readerCount = convertInt(fileScan.nextLine(), code);
    }
    if(readerCount < 0) {
      //handle code to return proper error code
    } else {
      initReader(readerCount, fileScan);
      listReaders();
      }
    }

    //call listBooks()
    //scan the next line and call intConvert
    //use int from line to call initShelves
    //call listShelves()
    //scan next line and call intConvert
    //call initReader using int from line
    //call listReader

  }

  public String getName() {
    return this.name;
  }

  private Code initBooks(int bookCount, Scanner scan) {

  }

  private Code initShelves(int shelfCount, Scanner scan) {

  }

  private Code initReader(int readerCount, Scanner scan) {

  }

  public Code addBook(Book newBook) {

  }

  public Code returnBook(Reader reader, Book book) {

  }

  public Code returnBook(Book book) {

  }

  private Code addBookToShelf(Book book, Shelf shelf) {

  }

  public int listBooks() {

  }

  public Code checkOutBook(Reader reader, Book book) {

  }

  public Book getBookByISBN(String isbn) {

  }

  public int listShelves() {
    return listShelves(false);
  }

  public int listShelves(boolean showbooks) {

  }

  public Code addShelf(String shelfSubject) {

  }

  public Code addShelf(Shelf shelf) {

  }

  public Shelf getShelf(Integer shelfNumber) {

  }

  public Shelf getShelf(String subject) {

  }

  public int listReaders() {

  }

  public int listReaders(boolean showBooks) {

  }

  public Reader getReaderByCard(int cardNumber) {

  }

  public Code addReader(Reader reader) {

  }

  public Code removeReader() {

  }

  /**
   *
   * @param recordCountString
   * @param code
   * @return if successful, returns count of the number associated with the String, such as book count or
   * shelf count. If unsuccessful, returns numerical code associated with error.
   */
  static public Integer convertInt(String recordCountString, Code code) {
    Integer count;
    try {
      count = Integer.parseInt(recordCountString);
    } catch (NumberFormatException e) {
      System.out.println("Value which caused the error: " + recordCountString);
      System.out.println("Error message: " + code.getMessage());
      return code.getCode();
    }
    switch(code) {
      case BOOK_COUNT_ERROR:
        System.out.println("Error: Could not read number of books");
      case PAGE_COUNT_ERROR:
        System.out.println("Error: could not parse page count");
      case DATE_CONVERSION_ERROR:
        System.out.println("Error: Could not parse date component");
      default:
        System.out.println("Error: Unknown conversion error");
    }
    return count;
  }

  static public LocalDate convertDate(String date, Code errorCode) {

  }

  static public int getLibraryCardNumber() {

  }

  private Code errorCode(int codeNumber) {

  }
}