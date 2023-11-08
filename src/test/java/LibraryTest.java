import Utilities.Code;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals(Code.BOOK_COUNT_ERROR, csumb.init(badBooks1) );
        assertEquals(Code.SHELF_COUNT_ERROR,csumb.init(badShelves0));

        //Test that badShelves1 fails due to incorrect Shelf values
        assertEquals(Code.SHELF_NUMBER_PARSE_ERROR, csumb.init(badShelves1)); //This was Reader Count Error

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
        assertEquals(Code.SHELF_EXISTS_ERROR,csumb.addBook(theArena));

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
    }

    @Test
    void testReturnBook() {
    }

    @Test
    void listBooks() {
    }

    @Test
    void checkOutBook() {
    }

    @Test
    void getBookByISBN() {
    }

    @Test
    void listShelves() {
    }

    @Test
    void addShelf() {
    }

    @Test
    void testAddShelf() {
    }

    @Test
    void getShelf() {
        //BE SURE TO TEST getShelf WITH NON-EXISTENT SHELF
        Shelf shelf = new Shelf(1, "fantasy");
        assertNull(csumb.getShelf(shelf.getSubject()));
        assertEquals(Code.SUCCESS, csumb.addShelf(shelf));
    }

    @Test
    void testGetShelf() {
    }

    @Test
    void listReaders() {
    }

    @Test
    void testListReaders() {
    }

    @Test
    void getReaderByCard() {
    }

    @Test
    void addReader() {
    }

    @Test
    void removeReader() {
    }

    @Test
    void convertInt() {
    }

    @Test
    void convertDate() {
    }

    @Test
    void getLibraryCardNumber() {
    }
}