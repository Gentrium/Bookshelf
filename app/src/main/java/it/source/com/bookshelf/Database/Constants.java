package it.source.com.bookshelf.Database;

public class Constants {
    public static final String BOOK_TABLE = "books";
    public static final String BOOK_ID = "_id";
    public static final String BOOK_NAME = "bookName";
    public static final String BOOK_SIZE = "bookSize";
    public static final String BOOK_GENRE = "genre";
    public static final String BOOK_ISBN = "isbn";
    public static final String BOOK_COVER = "cover";

    public static final String AUTHORS_TABLE = "authors";
    public static final String AUTHOR_ID = "author_id";
    public static final String AUTHOR_NAME = "name";
    public static final String AUTHOR_LAST_NAME = "lastName";
    public static final String AUTHOR_PLUS_BOOK_TABLE = "author_book";

    public static final String queryTrimmedData = "\n" +
            "SELECT b." + BOOK_ID +
            " , b." + BOOK_NAME +
            " , b." + BOOK_COVER + ",\n" +
            " GROUP_CONCAT((a." + AUTHOR_NAME + " || ' ' || a." + AUTHOR_LAST_NAME + "), ', ') AS authors\n" +
            " FROM\n" + BOOK_TABLE + " b " +
            " INNER JOIN \n" +
            AUTHOR_PLUS_BOOK_TABLE + " ab " +
            " ON b." + BOOK_ID +
            " = ab." + BOOK_ID +
            " LEFT JOIN " + AUTHORS_TABLE + " a" +
            " ON ab." + AUTHOR_ID + " = a." + AUTHOR_ID +
            "  GROUP BY b." + BOOK_ID;

    public static final String GENRES_TABLE = "genres";
    public static final String GENRE_NAME = "genre";
    public static final String GENRE_ID = "genre_id";

    public static final String queryBookData = "\n" +
            "SELECT b." + BOOK_ID +
            " , b." + BOOK_NAME + // select books.name
            " , b." + BOOK_COVER +
            " , b." + BOOK_SIZE +
            " , b." + BOOK_ISBN +
            " , g." + GENRE_NAME +
            " , GROUP_CONCAT((a." + AUTHOR_NAME + " || ' ' || a." + AUTHOR_LAST_NAME + "), ', ') AS authors\n" +
            " FROM\n" + BOOK_TABLE + " b " + // from books
            " INNER JOIN \n" +
            AUTHOR_PLUS_BOOK_TABLE + " ab " +
            " ON b." + BOOK_ID +
            " = ab." + BOOK_ID +
            " LEFT JOIN " + AUTHORS_TABLE + " a" +
            " ON ab." + AUTHOR_ID + " = a." + AUTHOR_ID +
            " INNER JOIN " + GENRES_TABLE + " g" +
            " ON b." + BOOK_GENRE + " = g." + GENRE_ID +
            " WHERE b." + BOOK_ID + " = ";

    public static final String DATABASE_NAME = "bookshelf";

    public static final String CREATE_BOOKS_TABLE = "create table "
            + BOOK_TABLE + " ("
            + BOOK_ID + " integer primary key autoincrement, "
            + BOOK_NAME + " text not null unique, "
            + BOOK_SIZE + " integer not null, "
            + BOOK_GENRE + " integer not null, "
            + BOOK_ISBN + " integer unique, "
            + BOOK_COVER + " text);";

    public static final String CREATE_GENRES_TABLE = "create table "
            + GENRES_TABLE + " ( "
            + GENRE_ID + " integer primary key autoincrement, "
            + GENRE_NAME + " text not null unique ); ";

    public static final String CREATE_AUTHORS_BOOKS_TABLE = "create table "
            + AUTHOR_PLUS_BOOK_TABLE + " ( "
            + BOOK_ID + " integer, "
            + AUTHOR_ID + " integer);";

    public static final  String CREATE_AUTHOR_TABLE = "create table "
            + AUTHORS_TABLE + " ( "
            + AUTHOR_ID + " integer primary key autoincrement, "
            + AUTHOR_NAME + " text not null, "
            + AUTHOR_LAST_NAME + " text); ";

    public static final byte MODE_OPEN = 1;
    public static final byte MODE_ADDING = 0;
    public static final byte MODE_EDIT = 2;
    public static final String MODE_TYPE = "bookMode";
    public static final String BOOK_KEY = "book_key";

    public static final byte ADD_AUTHOR = 0;
    public static final byte ADD_GENRE = 1;
}
