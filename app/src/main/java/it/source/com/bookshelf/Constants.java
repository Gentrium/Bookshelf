package it.source.com.bookshelf;

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

    public static final String GENRES_TABLE = "genres";
    public static final String GENRE_NAME = "genre";
    public static final String GENRE_ID = "genre_id";

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


}
