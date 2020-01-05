package pl.javastart.library.model;

import java.util.Objects;

public class Book extends Publication {
    private int pages;

    public static final String TYPE = "Książka";

    private String author;
    private String isbn;

    public Book(String title, String author, int year, int pages, String publisher, String isbn) {
        super(title, publisher, year);
        this.pages = pages;
        this.author = author;
        this.isbn = isbn;

    }


    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String toString() {
        return super.toString() + ", " + author + ", " + pages + ", " + isbn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Book book = (Book) o;
        return pages == book.pages &&
                author.equals(book.author) &&
                isbn.equals(book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pages, author, isbn);
    }

    @Override
    public String toCsv() {
        return (TYPE + ";") + getTitle() + ";" + getPublisher() + ";" + getYear() + ";" + author + ";" + pages + ";" + isbn + "";
    }
}


