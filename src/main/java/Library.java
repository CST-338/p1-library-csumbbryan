import Utilities.Code;
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
    System.out.println("Not implemented");
    return Code.NOT_IMPLEMENTED_ERROR;
  }

  public String getName() {

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

  static public Integer convertInt(String recordCountString, Code Code) {

  }

  static public LocalDate convertDate(String date, Code errorCode) {

  }

  static public int getLibraryCardNumber() {

  }

  private Code errorCode(int codeNumber) {

  }
}