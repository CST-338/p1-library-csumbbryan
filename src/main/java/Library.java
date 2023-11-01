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
        code = initBooks(bookCount, fileScan);
        listBooks();
      }
    //call initShelves using number from next line, and pass scanner
    }
    if(fileScan.hasNext() && code == Code.SUCCESS) {
      shelfCount = convertInt(fileScan.nextLine(), Code.SHELF_COUNT_ERROR);
      if(shelfCount < 0) {
        return Code.SHELF_COUNT_ERROR;
      } else {
        initShelves(shelfCount, fileScan);
        listShelves();
      }
    }
    //call initReader using number from line, and pass scanner
    if(fileScan.hasNext() && code == Code.SUCCESS) {
      readerCount = convertInt(fileScan.nextLine(), Code.READER_COUNT_ERROR);
      if(readerCount < 0) {
        return Code.READER_COUNT_ERROR;
      } else {
        initReader(readerCount, fileScan);
        listReaders();
      }
    }
    fileScan.close();
    return code;
  }

  public String getName() {
    return this.name;
  }

  private Code initBooks(int bookCount, Scanner scan) {
    Code code = null;
    if(bookCount < 1) {
      return Code.LIBRARY_ERROR;
    }
    for(int i = 0; i<bookCount; i++) {
      List<String> tempString = new ArrayList<>();
      for(String str : scan.nextLine().split(",")) {
        tempString.add(str);
      }
      if(tempString.size() <= Book.DUE_DATE_) {
        return Code.UNKNOWN_ERROR;
      }
      if(convertInt(tempString.get(Book.PAGE_COUNT_), Code.PAGE_COUNT_ERROR) <= 0) {
        return Code.PAGE_COUNT_ERROR;
      }
      if(convertDate(tempString.get(Book.DUE_DATE_), Code.DUE_DATE_ERROR) == null) {
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

  private Code initShelves(int shelfCount, Scanner scan) {
    Code code = null;
    if(shelfCount < 1) {
      return Code.SHELF_COUNT_ERROR;
    }
    for(int i = 0; i<shelfCount; i++) {
      List<String> tempString = new ArrayList<>();
      for(String str : scan.nextLine().split(",")) {
        tempString.add(str);
      }
      if(tempString.isEmpty()) {
        return Code.UNKNOWN_ERROR;
      }

      if(convertInt(tempString.get(Shelf.SHELF_NUMBER_), Code.SHELF_COUNT_ERROR) < 0) {
        return Code.SHELF_NUMBER_PARSE_ERROR;
      }
      Shelf shelf = new Shelf(
          convertInt(tempString.get(Shelf.SHELF_NUMBER_), Code.SHELF_COUNT_ERROR),
          tempString.get(Shelf.SUBJECT));
      code = addShelf(shelf);
    }
    if(shelves.size() == shelfCount) {
      return Code.SUCCESS;
    } else { return Code.SHELF_NUMBER_PARSE_ERROR;}
  }

  private Code initReader(int readerCount, Scanner scan) {
    Code code = null;
    if(readerCount < 1) {
      return Code.READER_COUNT_ERROR;
    }
    for(int i = 0; i<readerCount; i++) {
      List<String> tempString = new ArrayList<>();
      for(String str : scan.nextLine().split(",")) {
        tempString.add(str);
      }
      if(tempString.isEmpty()) {
        return Code.UNKNOWN_ERROR;
      }

      Reader reader = new Reader(
          convertInt(tempString.get(Reader.CARD_NUMBER_), Code.READER_COUNT_ERROR), //THIS MAY NEED TO BE TESTED FIRST
          tempString.get(Reader.NAME_),
          tempString.get(Reader.PHONE_));
      addReader(reader);
      //for each reader, add a book, based on parsing book count
      for(int j = 4; j < tempString.size(); j+=2) {
        //find books based on using getBookByISBN
        Book book = getBookByISBN(tempString.get(j));
        if(book == null) {
          System.out.println("ERROR");
          break;
        } else {
          book.setDueDate(convertDate(tempString.get(j+1), Code.DUE_DATE_ERROR));
          checkOutBook(reader, book);
        }
      }
    }
    return Code.SUCCESS;
  }

  public Code addBook(Book newBook) {
    //If HashMap of books already contains a book, increment count and print string
    if(books.containsKey(newBook)) {
      int bookCount = books.get(newBook);
      books.replace(newBook,++bookCount);
      System.out.println(bookCount + " copies of " + newBook.getTitle() + " in the stacks");
    } else { //If book does not exist, add book to HashMap of books and set count to 1
      books.put(newBook, 1);
      System.out.println(newBook.getTitle() + " added to the stacks");
    }
    //If there is a shelf with a matching subject, add to shelf and return Code.SUCCESS
    if(shelves.containsKey(newBook.getSubject())) {
      addBookToShelf(newBook, getShelf(newBook.getSubject()));
      return Code.SUCCESS;
    } else {
      System.out.println("No shelf for " + newBook.getSubject() + " books");
      return Code.SHELF_EXISTS_ERROR;
    }
    //Otherwise print out "No shelf for [subject] books" with return Code.SHELF_EXISTS_ERROR
  }

  public Code returnBook(Reader reader, Book book) {
    //if reader does not have the book in their list, return error
    if(!reader.hasBook(book)) {
      System.out.println(reader.getName() + " doesn't have " + book.getTitle() + " checked out");
      return Code.READER_DOESNT_HAVE_BOOK_ERROR;
    }

    //check if book exists in library, else return error
    //remove book from reader
    //If successful, call returnBook(book) and return resulting code
    //If not successful, print error and return code
    Code code = null;
    if(!books.containsKey(book)) {
      return Code.BOOK_NOT_IN_INVENTORY_ERROR;
    } else {
      System.out.println(reader.getName() + " is returning " + book);
      code = reader.removeBook(book);
    }
    if(code == Code.SUCCESS) {
      return returnBook(book);
    } else {
      System.out.println("Could not return " + book);
      return code;
    }
  }

  public Code returnBook(Book book) {
    if(!shelves.containsKey(book.getSubject())) {
      System.out.println("No shelf for " + book);
      return Code.SHELF_EXISTS_ERROR;
    }
    return getShelf(book.getSubject()).addBook(book);
  }

  private Code addBookToShelf(Book book, Shelf shelf) {
    Code code = returnBook(book);
    if(code == Code.SUCCESS) {
      return Code.SUCCESS;
    }
    if(!book.getSubject().equals(shelf.getSubject())) {
      return Code.SHELF_SUBJECT_MISMATCH_ERROR;
    }
    code = shelf.addBook(book);
    if(code == Code.SUCCESS) {
      return Code.SUCCESS;
    } else {
      System.out.println("Could not add " + book + " to shelf");
      return code;
    }
  }

  public int listBooks() {
    int bookCount = 0;
    for(Book book : books.keySet()) {
      System.out.println(books.get(book) + " copies of " + book.toString());
      bookCount += books.get(book);
    }
    return bookCount;
  }

  public Code checkOutBook(Reader reader, Book book) {
    //Check if reader exists, else print out error
    if(!readers.contains(reader)) {
      System.out.println(reader.getName() + " doesn't have an account here");
      return Code.READER_NOT_IN_LIBRARY_ERROR;
    }

    //Check if reader is under spending limit, else print out error
    if(reader.getBooks().size() >= Library.LENDING_LIMIT) {
      System.out.println(reader.getName() + " has reached the lending limit, " + Library.LENDING_LIMIT);
      return Code.BOOK_LIMIT_REACHED_ERROR;
    }

    //Check if book is in HashMap, else print error
    if(!books.containsKey(book)) {
      System.out.println("ERROR: could not find " + book);
      return Code.BOOK_NOT_IN_INVENTORY_ERROR;
    }

    //Check if book is on shelf, else print error
    if(getShelf(book.getSubject()) == null) {
      System.out.println("no shelf for " + book.getSubject() + " books!");
      return Code.SHELF_EXISTS_ERROR;
    }

    //Check if shelf has enough copies (1 or more), else print error
    Shelf tempShelf = getShelf(book.getSubject());
    int bookCount = getShelf(book.getSubject()).getBookCount(book);
    if(getShelf(book.getSubject()).getBookCount(book) < 1) {
      System.out.println("ERROR: no copies of " + book + " remain");
      return Code.BOOK_NOT_IN_INVENTORY_ERROR;
    }

    //Check if reader.addBook() returns success, else print error
    Code codeReader = reader.addBook(book);
    if(codeReader != Code.SUCCESS) {
      System.out.println("Couldn't checkout " + book);
      return codeReader;
    }

    //Check if shelf.removeBook returns success, else print error
    Code codeShelf = getShelf(book.getSubject()).removeBook(book);
    if(codeShelf != Code.SUCCESS) {
      return codeShelf;
    }
    return Code.SUCCESS;
  }

  public Book getBookByISBN(String isbn) {
    Book book;
    for (Book tempBook : books.keySet()) {
      if(tempBook.getISBN().equals(isbn)) {
        book = tempBook;
        return book;
      }
    }
    System.out.println("ERROR: Could not find book with ISBN: " + isbn);
    return null;
  }

  public int listShelves() {
    return listShelves(false);
  }

  public int listShelves(boolean showbooks) {
    for(Shelf shelf : shelves.values()) {
      shelf.toString();
      if(showbooks) {shelf.listBooks();}
    }
  return shelves.size();
  }

  public Code addShelf(String shelfSubject) {
    //Create shelf object and assign shelf number as size of shelves plus one - THIS DOESN'T WORK
    //Integer shelfCount = shelves.size()+1;
    //Shelf newShelf = new Shelf(shelfCount, shelfSubject);


    //Call addShelf with new Shelf Object
    if(shelves.containsKey(shelfSubject)) {
      System.out.println("ERROR: (addShelf(String) Shelf already exists " + shelfSubject);
      return Code.SHELF_EXISTS_ERROR;
    } else {
      int shelfCount = shelves.size()+1;
      Shelf newShelf = new Shelf(shelfCount, shelfSubject);
      return addShelf(newShelf);
    }
  }

  public Code addShelf(Shelf shelf) {
    //No instruction on whether to use
    if(shelves.containsValue(shelf)) {
      System.out.println("ERROR: Shelf already exists " + shelf.getSubject());
      return Code.SHELF_EXISTS_ERROR;
    }

    //Below if statement not specified in instruction--added to ensure shelf always has shelf number
    if(shelf.getShelfNumber() <=0) {
      int shelfCount = shelves.size()+1;
      shelf.setShelfNumber(shelfCount);
    }
    shelves.put(shelf.getSubject(), shelf);
    for(Book book : books.keySet()) {
      if(book.getSubject().equals(shelf.getSubject())) {
        addBookToShelf(book, shelf);
        shelf.setBookCount(book, books.get(book)); //ADDED THIS TO ADDRESS SETTING COUNT ON INIT
      }
    }
    return Code.SUCCESS;
  }

  public Shelf getShelf(Integer shelfNumber) {
    Shelf returnShelf = new Shelf();
    boolean found = false;
    for (Shelf currentShelf : shelves.values()) {
      if(currentShelf.getShelfNumber() == shelfNumber) {
        returnShelf = currentShelf;
      }
    }
    if(found) {
      return returnShelf;
    } else {
      System.out.println("No shelf number " + shelfNumber + " found");
      return null;
    }
  }

  public Shelf getShelf(String subject) {
    if(shelves.containsKey(subject)) {
      return shelves.get(subject);//Not implemented
    } else {
      System.out.println("No shelf for " + subject + " found");
      return null;
    }
  }

  public int listReaders() {
    for(Reader reader : readers) {
      reader.toString();
    }
    return readers.size();
  }

  public int listReaders(boolean showBooks) {
    if(showBooks) {
      for(Reader reader : readers) {
        System.out.println(reader.getName() + " has the following books:");
        System.out.println(reader.getBooks());
      }
    } else {
      return listReaders();
    }
    return readers.size();
  }

  public Reader getReaderByCard(int cardNumber) {
    for(Reader reader : readers) {
      if (reader.getCardNumber() == cardNumber) {
        return reader;
      }
    }
    System.out.println("Could not find a reader with card #" + cardNumber);
    return null;
  }

  public Code addReader(Reader reader) {
    if(readers.contains(reader)) {
      return Code.READER_ALREADY_EXISTS_ERROR;
    }

    for(Reader rdr : readers) {
      if(rdr.getCardNumber() == reader.getCardNumber()) {
        System.out.println(rdr.getName() + " and " + reader.getName() + " have the same card number!");
        return Code.READER_CARD_NUMBER_ERROR;
      }
    }

    readers.add(reader);
    System.out.println(reader.getName() + " added to the library!");
    if(reader.getCardNumber() > libraryCard) {
      libraryCard = reader.getCardNumber();
    }
    return Code.SUCCESS;
  }

  public Code removeReader(Reader reader) {
    if(reader.getBookCount() > 0) {
      System.out.println(reader.getName() + " must return all books!");
      return Code.READER_STILL_HAS_BOOKS_ERROR;
    }
    if(!readers.contains(reader)) {
      System.out.println(reader + " is not part of this library");
      return Code.READER_NOT_IN_LIBRARY_ERROR;
    } else {
      readers.remove(reader);
      return Code.SUCCESS;
    }
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
    return libraryCard++; //Not implemented
  }

  private Code errorCode(int codeNumber) {
    for (Code code : Code.values()) {
      if (code.getCode() == codeNumber) {
        return code;
      }
    }
    return Code.UNKNOWN_ERROR;
  }

}