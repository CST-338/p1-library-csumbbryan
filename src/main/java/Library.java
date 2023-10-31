import Utilities.Code;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
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
    this.name = name;
    readers = new ArrayList<>();
    shelves = new HashMap<>();
    books = new HashMap<>();
    System.out.println("Barely implemented");
  }

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
    if(fileScan.hasNext()) {
      bookCount = convertInt(fileScan.nextLine(), Code.BOOK_COUNT_ERROR);
      if(bookCount < 0) {
        return Code.BOOK_COUNT_ERROR;
      } else {
        initBooks(bookCount, fileScan);
        listBooks();
      }
    //call initShelves using number from next line, and pass scanner
    }
    if(fileScan.hasNext()) {
      shelfCount = convertInt(fileScan.nextLine(), Code.SHELF_COUNT_ERROR);
      if(shelfCount < 0) {
        return Code.SHELF_COUNT_ERROR;
      } else {
        initShelves(shelfCount, fileScan);
        listShelves();
      }
    }
    //call initReader using number from line, and pass scanner
    if(fileScan.hasNext()) {
      readerCount = convertInt(fileScan.nextLine(), Code.READER_COUNT_ERROR);
      if(readerCount < 0) {
        return Code.READER_COUNT_ERROR;
      } else {
        initReader(readerCount, fileScan);
        listReaders();
      }
    }
    return Code.SUCCESS;
  }

  public String getName() {
    return this.name;
  }

  private Code initBooks(int bookCount, Scanner scan) {
    Code code = null;
      if(bookCount < 0) {
      return Code.LIBRARY_ERROR;
    }
    for(int i = 0; i<bookCount; i++) {
      List<String> tempString = new ArrayList<>();
      for(String str : scan.nextLine().split(",")) {
        tempString.add(str);
      }
      if(tempString.size() < Book.DUE_DATE_-1) {
        return Code.UNKNOWN_ERROR;
      }
      //Troubleshooting Begins
      System.out.println("Adding Book: ISBN: "
          + tempString.get(Book.ISBN_)
          + " Title: " + tempString.get(Book.TITLE_)
          + " Subject: " + tempString.get(Book.SUBJECT_)
          + " Page Count: " + convertInt(tempString.get(Book.PAGE_COUNT_), Code.BOOK_COUNT_ERROR)
          + " Author: " + tempString.get(Book.AUTHOR_)
          + " Due Date: " + convertDate(tempString.get(Book.DUE_DATE_), Code.DUE_DATE_ERROR)
          );
      //Troubleshooting Ends
      Book book = new Book(
          tempString.get(Book.ISBN_),
          tempString.get(Book.TITLE_),
          tempString.get(Book.SUBJECT_),
          convertInt(tempString.get(Book.PAGE_COUNT_), Code.BOOK_COUNT_ERROR),
          tempString.get(Book.AUTHOR_),
          convertDate(tempString.get(Book.DUE_DATE_), Code.DUE_DATE_ERROR));
      addBook(book);
    }
  return Code.SUCCESS;
  }

  private Code initShelves(int shelfCount, Scanner scan) {
    return Code.NOT_IMPLEMENTED_ERROR;
  }

  private Code initReader(int readerCount, Scanner scan) {
    return Code.NOT_IMPLEMENTED_ERROR;
  }

  public Code addBook(Book newBook) {
    return Code.NOT_IMPLEMENTED_ERROR;
  }

  public Code returnBook(Reader reader, Book book) {
    return Code.NOT_IMPLEMENTED_ERROR;
  }

  public Code returnBook(Book book) {
    return Code.NOT_IMPLEMENTED_ERROR;
  }

  private Code addBookToShelf(Book book, Shelf shelf) {
    return Code.NOT_IMPLEMENTED_ERROR;
  }

  public int listBooks() {
    return -1; //Not implemented
  }

  public Code checkOutBook(Reader reader, Book book) {
    return Code.NOT_IMPLEMENTED_ERROR;
  }

  public Book getBookByISBN(String isbn) {
    return null; //Not implemented
  }

  public int listShelves() {
    return listShelves(false);
  }

  public int listShelves(boolean showbooks) {
    return -1; //Not implemented
  }

  public Code addShelf(String shelfSubject) {
    return Code.NOT_IMPLEMENTED_ERROR;
  }

  public Code addShelf(Shelf shelf) {
    return Code.NOT_IMPLEMENTED_ERROR;
  }

  public Shelf getShelf(Integer shelfNumber) {
    return null; //Not implemented
  }

  public Shelf getShelf(String subject) {
    return null; //Not implemented
  }

  public int listReaders() {
    return -1; //Not implemented
  }

  public int listReaders(boolean showBooks) {
    return -1; //Not implemented
  }

  public Reader getReaderByCard(int cardNumber) {
    return null; //Not implemented
  }

  public Code addReader(Reader reader) {
    return Code.NOT_IMPLEMENTED_ERROR;
  }

  public Code removeReader() {
    return Code.NOT_IMPLEMENTED_ERROR;
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
      switch (code) {
        case BOOK_COUNT_ERROR:
          System.out.println("Error: Could not read number of books");
        case PAGE_COUNT_ERROR:
          System.out.println("Error: could not parse page count");
        case DATE_CONVERSION_ERROR:
          System.out.println("Error: Could not parse date component");
        default:
          System.out.println("Error: Unknown conversion error");
      }
      return code.getCode();
    }
    return count;
  }

  //Should the if statements be cleaned up below? Could we call convertDate recursively?
  //
  static public LocalDate convertDate(String date, Code errorCode) {
    Integer year;
    Integer month;
    Integer day;
    if (date.equals("0000")) {
      return LocalDate.of(1970, 1, 1);
    } else {
      String[] tempStringArray = date.split("-");
      if(tempStringArray.length != 3) {
        System.out.println("ERROR: date conversion error, could not parse " + date);
        System.out.println("Using default date (01-jan-1970");
        return LocalDate.of(1970,1,1);
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

  static public int getLibraryCardNumber() {
    return -1; //Not implemented
  }

  private Code errorCode(int codeNumber) {
    return Code.NOT_IMPLEMENTED_ERROR;
  }
}