package javalab4;

import java.util.*;
import java.util.stream.Collectors;

public class Library {
    private List<Book> books;
    private Set<String> uniqueAuthors;
    private Map<String, Integer> authorCounts;

    public Library() {
        books = new ArrayList<>();
        uniqueAuthors = new HashSet<>();
        authorCounts = new HashMap<>();
    }

    // Добавить книгу в библиотеку
    public void addBook(Book book) {
        books.add(book);
        uniqueAuthors.add(book.getAuthor());
        authorCounts.put(book.getAuthor(), 
                        authorCounts.getOrDefault(book.getAuthor(), 0) + 1);
    }

    // Удалить книгу из библиотеки
    public void removeBook(Book book) {
        if (books.remove(book)) {
            // Уменьшаем счетчик книг автора
            int count = authorCounts.getOrDefault(book.getAuthor(), 0);
            if (count > 1) {
                authorCounts.put(book.getAuthor(), count - 1);
            } else {
                authorCounts.remove(book.getAuthor());
                uniqueAuthors.remove(book.getAuthor());
            }
        }
    }

    //  Найти все книги
    public List<Book> getAllBooks() {
        return Collections.unmodifiableList(new ArrayList<>(books));
    }

    // Найти все книги определенного автора
    public List<Book> findBooksByAuthor(String author) {
        return books.stream()
                   .filter(book -> book.getAuthor().equals(author))
                   .collect(Collectors.toList());
    }

    // Найти все книги, изданные в определенный год
    public List<Book> findBooksByYear(int year) {
        return books.stream()
                   .filter(book -> book.getYear() == year)
                   .collect(Collectors.toList());
    }

    // Вывести список всех книг в библиотеке
    public void printAllBooks() {
        if (books.isEmpty()) {
            System.out.println("There are no books in the library.");
            return;
        }
        System.out.println("List of all books in the library:");
        books.forEach(System.out::println);
    }

    // Вывести список уникальных авторов
    public void printUniqueAuthors() {
        if (uniqueAuthors.isEmpty()) {
            System.out.println("There are no authors in the library.");
            return;
        }
        System.out.println("Unique authors in the library:");
        uniqueAuthors.forEach(System.out::println);
    }

    // Вывести статистику по количеству книг каждого автора
    public void printAuthorStatistics() {
        if (authorCounts.isEmpty()) {
            System.out.println("No data for statistics.");
            return;
        }
        System.out.println("Statistics by Authors:");
        authorCounts.forEach((author, count) -> 
            System.out.println(author + ": " + count + " book(s)"));
    }
}