import Utilities.Code;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bryan Zanoli
 * @since October 30, 2023
 * <br>
 * Abstract: Unit tests for all relevant Library class methods. Excludes basic getters and setters,
 * which pass testing through other calling methods.
 */
class LibraryTest {

  Library csumb = null;

  String library00 = "Library00.csv";
  String library01 = "Library01.csv";
  String badBooks0 = "badBooks0.csv";
  String badBooks1 = "badBooks1.csv";
  String badShelves0 = "badShelves0.csv";
  String badShelves1 = "badShelves1.csv";
  String badReader0 = "badReader0.csv";
  String badReader1 = "badReader1.csv";

  @BeforeEach
  void setUp() {
    csumb = new Library("CSUMB");
  }

  @AfterEach
  void tearDown() {
    csumb = null;
  }

  @Test
  void init_test() {
    //NEW CODE:
    assertEquals(Code.FILE_NOT_FOUND_ERROR, csumb.init("nope.csv"));
    assertEquals(Code.BOOK_COUNT_ERROR, csumb.init(badBooks0));
    assertEquals(Code.BOOK_COUNT_ERROR, csumb.init(badBooks1));
    assertEquals(Code.SHELF_COUNT_ERROR, csumb.init(badShelves0));

    //Test that badShelves1 fails due to incorrect Shelf values
    assertEquals(Code.SHELF_NUMBER_PARSE_ERROR,
        csumb.init(badShelves1)); //This was Reader Count Error

    //Test that badReader0 fails due to incorrect reader count value
    assertEquals(Code.READER_COUNT_ERROR, csumb.init(badReader0));

    //Test that Drew Clinkenbeard books cannot be read in but init otherwise succeeds:
    assertEquals(Code.SUCCESS, csumb.init(badReader1)); //Is this the right error?
    assertEquals(0, csumb.getReaderByCard(1).getBookCount());
  }

  @Test
  void addBook() {
    //Before Test Setup
    Book theArena = new Book(
        "0101",
        "The Arena",
        "fantasy",
        300,
        "Robert Smith",
        LocalDate.now()
    );
    Book chemistry = new Book(
        "h2o",
        "Chemistry",
        "education",
        450,
        "Arthur Nobel",
        LocalDate.now()
    );
    String fantasy = "fantasy";
    String education = "education";
    //Conditions:
    //Shelf doesn't exist
    assertEquals(Code.SHELF_EXISTS_ERROR, csumb.addBook(theArena));

    //Book does not exist and count set to 1
    csumb.init(library00);
    assertEquals(Code.SUCCESS, csumb.addBook(chemistry));
    assertEquals(1, csumb.getShelf(education).getBookCount(chemistry));

    //Book exists already and book count increased by 1
    assertEquals(Code.SUCCESS, csumb.addShelf(fantasy));
    csumb.addBook(theArena);
    assertEquals(2, csumb.getShelf(fantasy).getBookCount(theArena));
  }

  @Test
  void returnBook() {
    csumb.init(library00);
    Book hitchhikers = csumb.getBookByISBN("42-w-87");
    Book Dune = csumb.getBookByISBN("34-w-34");
    Reader reader = csumb.getReaderByCard(4);

    //Test that reader doesn't have Dune
    assertEquals(Code.READER_DOESNT_HAVE_BOOK_ERROR, csumb.returnBook(reader, Dune));
    //Test that reader has Hitchhikers Guide to the Galaxy
    assertEquals(Code.SUCCESS, csumb.returnBook(reader, hitchhikers));
    //Test that reader no longer has Hitchhikers Guide to the Galaxy
    assertEquals(Code.READER_DOESNT_HAVE_BOOK_ERROR, csumb.returnBook(reader, hitchhikers));
    //Test that the reader now only has 1 book
    assertEquals(1, reader.getBooks().size());
  }

  @Test
  void testReturnBook() {
    csumb.init(library00);
    Book theArena = new Book(
        "0101",
        "The Arena",
        "fantasy",
        300,
        "Robert Smith",
        LocalDate.now()
    );
    //Test that theArena's subject does not have a corresponding shelf
    assertEquals(Code.SHELF_EXISTS_ERROR, csumb.returnBook(theArena));
    //Add the shelf for theArena
    csumb.addShelf("fantasy");
    //Test that theArena's subject now has a corresponding shelf and can be added
    assertEquals(Code.SUCCESS, csumb.returnBook(theArena));
    //Test that theArena was added to the shelf but not added to the library due to nature of
    //returnBook(book) method
    assertNull(csumb.getBookByISBN("0101"));
  }

  @Test
  void listBooks() {
    assertEquals(0, csumb.listBooks());
    csumb.init(library00);
    assertEquals(9, csumb.listBooks());
  }

  @Test
  void checkOutBook() {
    Reader notInLibrary = new Reader(1, "Bob Smith", "555-555-0001");
    Book theArena = new Book(
        "0101",
        "The Arena",
        "fantasy",
        300,
        "Robert Smith",
        LocalDate.now()
    );
    Book chemistry = new Book(
        "h2o",
        "Chemistry",
        "education",
        450,
        "Arthur Nobel",
        LocalDate.now()
    );
    String badISBN = "0001";

    //Check if Reader has an account with the Library
    assertEquals(Code.READER_NOT_IN_LIBRARY_ERROR,
        csumb.checkOutBook(notInLibrary, csumb.getBookByISBN(badISBN)));
    csumb.removeReader(notInLibrary);

    //Check if book is in HashMap, else print error
    csumb.init(library00);
    Reader reader1 = csumb.getReaderByCard(3);
    Reader reader2 = csumb.getReaderByCard(4);
    Book dune = csumb.getBookByISBN("34-w-34");
    Book count = csumb.getBookByISBN("5297");

    assertEquals(Code.BOOK_NOT_IN_INVENTORY_ERROR,
        csumb.checkOutBook(reader1, csumb.getBookByISBN(badISBN)));

    //Check if book is in library, else print error
    assertEquals(Code.BOOK_NOT_IN_INVENTORY_ERROR,
        csumb.checkOutBook(reader1, theArena));

    //Check if book is on shelf, else print error
    csumb.addBook(theArena);

    assertEquals(Code.SHELF_EXISTS_ERROR,
        csumb.checkOutBook(reader1, theArena));

    //Check if reader already has booked checked out
    csumb.checkOutBook(reader1, dune);

    assertEquals(Code.BOOK_ALREADY_CHECKED_OUT_ERROR,
        csumb.checkOutBook(reader1, dune));

    //Check if shelf has enough copies (1 or more), else print error
    assertEquals(Code.BOOK_NOT_IN_INVENTORY_ERROR,
        csumb.checkOutBook(reader2, dune));

    //Check if reader is under spending limit, else print out error
    csumb.addShelf("fantasy");
    csumb.addBook(chemistry);
    csumb.checkOutBook(reader1, theArena);
    csumb.checkOutBook(reader1, count);

    assertEquals(Code.BOOK_LIMIT_REACHED_ERROR,
        csumb.checkOutBook(reader1, chemistry));
  }

  @Test
  void getBookByISBN() {
    csumb.init(library00);
    Book hitchhikers = new Book(
        "42-w-87",
        "Hitchhikers Guide To the Galaxy",
        "sci-fi",
        42,
        "Douglas Adams",
        LocalDate.now()
    );
    Book dune = new Book(
        "34-w-34",
        "NotDune",
        "sci-fi",
        42,
        "Frank Herbert",
        LocalDate.now()
    );
    String hitchhikersISBN = "42-w-87";
    String duneISBN = "34-w-34";
    String notInLibrary = "0001";

    assertEquals(hitchhikers, csumb.getBookByISBN(hitchhikersISBN));
    assertNotEquals(dune, csumb.getBookByISBN(duneISBN));
    assertNull(csumb.getBookByISBN(notInLibrary));
  }

  @Test
  void listShelves() {
    assertEquals(0, csumb.listShelves());
    csumb.init(library00);
    assertEquals(3, csumb.listShelves());
  }

  @Test
  void addShelf() {
    int shelfCountStart = 0;
    int shelfCountNext = 1;
    String history = "history";
    String fantasy = "fantasy";
    String sciFi = "sci-fi";

    assertEquals(shelfCountStart, csumb.listShelves());
    assertEquals(Code.SUCCESS, csumb.addShelf(history));
    assertEquals(shelfCountNext, csumb.listShelves());

    csumb.init(library00);

    assertEquals(Code.SHELF_EXISTS_ERROR, csumb.addShelf(sciFi));
    int shelfCount = csumb.listShelves();
    assertEquals(Code.SUCCESS, csumb.addShelf(fantasy));
    assertEquals(shelfCount+1, csumb.getShelf(fantasy).getShelfNumber());
  }

  @Test
  void testAddShelf() {
    int shelfCountStart = 0;
    int shelfCountNext = 1;
    String fantasy = "fantasy";
    String sciFi = "sci-fi";
    Shelf shelf = new Shelf(shelfCountNext, fantasy);

    assertEquals(shelfCountStart, csumb.listShelves());
    assertEquals(Code.SUCCESS, csumb.addShelf(shelf));
    assertEquals(Code.SHELF_EXISTS_ERROR, csumb.addShelf(shelf));

    csumb.init(library00);

    assertEquals(Code.SHELF_EXISTS_ERROR, csumb.addShelf(csumb.getShelf(sciFi)));
  }

  @Test
  void getShelf() {
    String fantasy = "fantasy";
    int fantasyShelfNumber = 1;
    Shelf shelf = new Shelf(fantasyShelfNumber, fantasy);

    assertNull(csumb.getShelf(fantasyShelfNumber));
    assertEquals(Code.SUCCESS, csumb.addShelf(shelf));
    assertEquals(shelf, csumb.getShelf(fantasyShelfNumber));
  }

  @Test
  void testGetShelf() {
    String fantasy = "fantasy";
    int fantasyShelfNumber = 1;
    Shelf shelf = new Shelf(fantasyShelfNumber, fantasy);
    assertNull(csumb.getShelf(shelf.getSubject()));
    assertEquals(Code.SUCCESS, csumb.addShelf(shelf));
    assertEquals(shelf, csumb.getShelf(shelf.getSubject()));
  }

  @Test
  void listReaders() {
    int noReaders = 0;
    int startingReaders = 4;
    assertEquals(noReaders, csumb.listReaders());
    csumb.init(library00);
    assertEquals(startingReaders, csumb.listReaders());
  }

  @Test
  void testListReaders() {
    boolean showBooks = true;
    int noReaders = 0;
    int startingReaders = 4;
    assertEquals(noReaders, csumb.listReaders(showBooks));
    csumb.init(library00);
    assertEquals(startingReaders, csumb.listReaders(showBooks));
  }

  @Test
  void getReaderByCard() {
    int cardNumber = 1;
    int cardNumberNotInLibrary = 0;
    Reader reader = new Reader(cardNumber, "Bob Smith", "555-555-0001");

    assertNull(csumb.getReaderByCard(cardNumber));
    assertEquals(Code.SUCCESS, csumb.addReader(reader));
    assertEquals(reader, csumb.getReaderByCard(cardNumber));
    assertNull(csumb.getReaderByCard(cardNumberNotInLibrary));
  }

  @Test
  void addReader() {
    int cardNumber = 1;
    int cardNumberNotInLibrary = 0;
    Reader reader = new Reader(cardNumber, "Bob Smith", "555-555-0001");

    //Ensure reader is added successfully
    assertEquals(Code.SUCCESS, csumb.addReader(reader));

    //Check if reader already exists
    assertEquals(Code.READER_ALREADY_EXISTS_ERROR, csumb.addReader(reader));

    csumb.removeReader(reader);
    csumb.init(library00);

    //Check if another reader already has the same card number
    assertEquals(Code.READER_CARD_NUMBER_ERROR, csumb.addReader(reader));

    //Ensure reader count is incremented
    int readerCount = csumb.listReaders();
    assertEquals(Code.SUCCESS, csumb.addReader(
        new Reader(Library.getLibraryCardNumber(), "Bob Smith", "555-555-0001")));
    assertEquals(readerCount+1, csumb.listReaders());
  }

  @Test
  void removeReader() {
    int cardNumber = 1;
    String fantasy = "fantasy";
    Reader reader = new Reader(cardNumber, "Bob Smith", "555-555-0001");
    Book theArena = new Book(
        "0101",
        "The Arena",
        "fantasy",
        300,
        "Robert Smith",
        LocalDate.now()
    );

    //Check that reader cannot be removed if not in library
    assertEquals(Code.READER_NOT_IN_LIBRARY_ERROR, csumb.removeReader(reader));

    //Check that reader can be removed after being added
    csumb.addReader(reader);
    assertEquals(1, csumb.listReaders());

    //Check that reader cannot be removed if they have books checked out
    csumb.addBook(theArena);
    csumb.addShelf(fantasy);
    csumb.checkOutBook(reader, theArena);
    assertEquals(Code.READER_STILL_HAS_BOOKS_ERROR,
        csumb.removeReader(reader));

    //Check that reader can be removed after returning all books
    csumb.returnBook(reader, theArena);
    assertEquals(Code.SUCCESS, csumb.removeReader(reader));
    assertEquals(0, csumb.listReaders());
  }

  @Test
  void convertInt() {
    String correctInt = "2020";
    String badInt = "20.3";

    //Test successful return of proper integer
    assertEquals(2020, Library.convertInt(correctInt, Code.SUCCESS));

    //Test return of bad integer with BOOK_COUNT_ERROR
    assertEquals(Code.BOOK_COUNT_ERROR.getCode(),
        Library.convertInt(badInt, Code.BOOK_COUNT_ERROR));

    //Test return of bad integer with PAGE_COUNT_ERROR
    assertEquals(Code.PAGE_COUNT_ERROR.getCode(),
        Library.convertInt(badInt, Code.PAGE_COUNT_ERROR));

    //Test return of bad integer with DATE_CONVERSION_ERROR
    assertEquals(Code.DATE_CONVERSION_ERROR.getCode(),
        Library.convertInt(badInt, Code.DATE_CONVERSION_ERROR));

    assertEquals(Code.SHELF_NUMBER_PARSE_ERROR.getCode(),
        Library.convertInt(badInt, Code.SHELF_NUMBER_PARSE_ERROR));
  }

  @Test
  void convertDate() {
    String correctDate = "2020-01-01";
    String invalidDate = "202-13-001";
    String badDate = "20$3-01-01";
    Code code = Code.SUCCESS;

    assertEquals(LocalDate.of(2020, 1, 1), Library.convertDate(correctDate, code));
    assertEquals(LocalDate.of(1970, 1, 1), Library.convertDate(invalidDate, code));
    assertEquals(LocalDate.of(1970, 1, 1), Library.convertDate(badDate, code));
  }

  @Test
  void getLibraryCardNumber() {
    csumb.init(library00);
    int readerCount = csumb.listReaders();
    assertEquals(readerCount+1, Library.getLibraryCardNumber());

    //Add a reader with a card count well above current level, and test to ensure that represents
    //the new card number
    int newCardNumber = readerCount + 5;
    Reader reader = new Reader(newCardNumber, "Bob Smith", "555-555-0001");
    csumb.addReader(reader);
    assertEquals(newCardNumber + 1, Library.getLibraryCardNumber());
  }
}