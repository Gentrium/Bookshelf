package it.source.com.bookshelf.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import it.source.com.bookshelf.Constants;

public class BooksDatabase {
    private static final String LOG_TAG = "logs";
    private static String queryBookData = "\n" +
            "SELECT b." + Constants.BOOK_ID +
            " , b." + Constants.BOOK_NAME + // select books.name
            " , b." + Constants.BOOK_COVER +
            " , b." + Constants.BOOK_SIZE +
            " , b." + Constants.BOOK_ISBN +
            " , g." + Constants.GENRE_NAME +
            " , GROUP_CONCAT((a." + Constants.AUTHOR_NAME + " || ' ' || a." + Constants.AUTHOR_LAST_NAME + "), ', ') AS authors\n" + // group( author.names) as authors
            " FROM\n" + Constants.BOOK_TABLE + " b " + // from books
            " INNER JOIN \n" +
            Constants.AUTHOR_PLUS_BOOK_TABLE + " ab " +
            " ON b." + Constants.BOOK_ID +
            " = ab." + Constants.BOOK_ID +
            " LEFT JOIN " + Constants.AUTHORS_TABLE + " a" +
            " ON ab." + Constants.AUTHOR_ID + " = a." + Constants.AUTHOR_ID +
            " INNER JOIN " + Constants.GENRES_TABLE + " g" +
            " ON b." + Constants.BOOK_GENRE + " = g." + Constants.GENRE_ID +
            " WHERE b." + Constants.BOOK_ID + " = ";
    private static SQLiteDatabase database;
    private String queryTrimmedData = "\n" +

            "SELECT b." + Constants.BOOK_ID +
            " , b." + Constants.BOOK_NAME + // select books.name
            " , b." + Constants.BOOK_COVER + ",\n" +// ,books.cover
            " GROUP_CONCAT((a." + Constants.AUTHOR_NAME + " || ' ' || a." + Constants.AUTHOR_LAST_NAME + "), ', ') AS authors\n" + // group( author.names) as authors
            " FROM\n" + Constants.BOOK_TABLE + " b " + // from books
            " INNER JOIN \n" +
            Constants.AUTHOR_PLUS_BOOK_TABLE + " ab " +
            " ON b." + Constants.BOOK_ID +
            " = ab." + Constants.BOOK_ID +
            " LEFT JOIN " + Constants.AUTHORS_TABLE + " a" +
            " ON ab." + Constants.AUTHOR_ID + " = a." + Constants.AUTHOR_ID +
            "  GROUP BY b." + Constants.BOOK_ID;
    private DBHelper dbHelper;
    private Context context;

    public BooksDatabase(Context context) {
        this.context = context;
    }

    public static Cursor selectBook(int id) {
        Cursor c = database.rawQuery(queryBookData + (id + 1), null);
        return c;
    }

    public static void deleteBook(int id) {
        logCursor(database.query(Constants.BOOK_TABLE,null,null,null,null,null,null));
        database.delete(Constants.AUTHOR_PLUS_BOOK_TABLE, Constants.BOOK_ID + " = " + id, null);
        database.delete(Constants.BOOK_TABLE, Constants.BOOK_ID + " = " + id, null);
        Log.d(LOG_TAG,"------------------------------------------------");
        logCursor(database.query(Constants.BOOK_TABLE, null, null, null, null, null, null));
    }

    public void fillSomeData() {
        ContentValues cv = new ContentValues();
        cv.put(Constants.BOOK_NAME, "Android in practice");
        cv.put(Constants.BOOK_SIZE, 500);
        cv.put(Constants.BOOK_GENRE, 1);
        cv.put(Constants.BOOK_ISBN, 1);
        database.insert(Constants.BOOK_TABLE, null, cv);
        cv.clear();
        cv.put(Constants.BOOK_NAME, "Thinking in Java");
        cv.put(Constants.BOOK_SIZE, 400);
        cv.put(Constants.BOOK_GENRE, 1);
        cv.put(Constants.BOOK_ISBN, 2);
        database.insert(Constants.BOOK_TABLE, null, cv);
        cv.clear();
        cv.put(Constants.BOOK_NAME, "Java full guide");
        cv.put(Constants.BOOK_SIZE, 300);
        cv.put(Constants.BOOK_GENRE, 1);
        cv.put(Constants.BOOK_ISBN, 3);
        database.insert(Constants.BOOK_TABLE, null, cv);
        cv.clear();
        cv.put(Constants.BOOK_NAME, "Java 2 se");
        cv.put(Constants.BOOK_SIZE, 300);
        cv.put(Constants.BOOK_GENRE, 1);
        cv.put(Constants.BOOK_ISBN, 4);
        database.insert(Constants.BOOK_TABLE, null, cv);
        cv.clear();
        cv.put(Constants.BOOK_NAME, "Companions");
        cv.put(Constants.BOOK_SIZE, 800);
        cv.put(Constants.BOOK_GENRE, 2);
        cv.put(Constants.BOOK_ISBN, 5);
        database.insert(Constants.BOOK_TABLE, null, cv);
        cv.clear();
        cv.put(Constants.BOOK_NAME, "Game of thrones");
        cv.put(Constants.BOOK_SIZE, 1800);
        cv.put(Constants.BOOK_GENRE, 2);
        cv.put(Constants.BOOK_ISBN, 6);
        database.insert(Constants.BOOK_TABLE, null, cv);
        cv.clear();
        cv.put(Constants.GENRE_NAME, "Programming");
        database.insert(Constants.GENRES_TABLE, null, cv);
        cv.clear();
        cv.put(Constants.GENRE_NAME, "Fantasy");
        database.insert(Constants.GENRES_TABLE, null, cv);
        cv.clear();
        cv.put(Constants.AUTHOR_NAME, "Key");
        cv.put(Constants.AUTHOR_LAST_NAME, "Horstmann");
        database.insert(Constants.AUTHORS_TABLE, null, cv);
        cv.clear();
        cv.put(Constants.AUTHOR_NAME, "Harry");
        cv.put(Constants.AUTHOR_LAST_NAME, "Cornell");
        database.insert(Constants.AUTHORS_TABLE, null, cv);
        cv.clear();
        cv.put(Constants.AUTHOR_NAME, "Herbert");
        cv.put(Constants.AUTHOR_LAST_NAME, "Shildt");
        database.insert(Constants.AUTHORS_TABLE, null, cv);
        cv.clear();
        cv.put(Constants.AUTHOR_NAME, "Bruce");
        cv.put(Constants.AUTHOR_LAST_NAME, "Ekkel");
        database.insert(Constants.AUTHORS_TABLE, null, cv);
        cv.clear();
        cv.put(Constants.AUTHOR_NAME, "Robert");
        cv.put(Constants.AUTHOR_LAST_NAME, "Salvatore");
        database.insert(Constants.AUTHORS_TABLE, null, cv);
        cv.clear();
        cv.put(Constants.AUTHOR_NAME, "George");
        cv.put(Constants.AUTHOR_LAST_NAME, "Martin");
        database.insert(Constants.AUTHORS_TABLE, null, cv);
        cv.clear();
        cv.put(Constants.AUTHOR_ID, 1);
        cv.put(Constants.BOOK_ID, 1);
        database.insert(Constants.AUTHOR_PLUS_BOOK_TABLE, null, cv);
        cv.clear();
        cv.put(Constants.AUTHOR_ID, 1);
        cv.put(Constants.BOOK_ID, 2);
        database.insert(Constants.AUTHOR_PLUS_BOOK_TABLE, null, cv);
        cv.clear();
        cv.put(Constants.AUTHOR_ID, 2);
        cv.put(Constants.BOOK_ID, 3);
        database.insert(Constants.AUTHOR_PLUS_BOOK_TABLE, null, cv);
        cv.clear();
        cv.put(Constants.AUTHOR_ID, 2);
        cv.put(Constants.BOOK_ID, 4);
        database.insert(Constants.AUTHOR_PLUS_BOOK_TABLE, null, cv);
        cv.clear();
        cv.put(Constants.AUTHOR_ID, 3);
        cv.put(Constants.BOOK_ID, 5);
        database.insert(Constants.AUTHOR_PLUS_BOOK_TABLE, null, cv);
        cv.clear();
        cv.put(Constants.AUTHOR_ID, 2);
        cv.put(Constants.BOOK_ID, 5);
        database.insert(Constants.AUTHOR_PLUS_BOOK_TABLE, null, cv);
        cv.clear();
        cv.put(Constants.AUTHOR_ID, 3);
        cv.put(Constants.BOOK_ID, 1);
        database.insert(Constants.AUTHOR_PLUS_BOOK_TABLE, null, cv);
        cv.clear();
        cv.put(Constants.BOOK_NAME, "Android in practice2");
        cv.put(Constants.BOOK_SIZE, 500);
        cv.put(Constants.BOOK_GENRE, 1);
        cv.put(Constants.BOOK_ISBN, 9);
        database.insert(Constants.BOOK_TABLE, null, cv);
        cv.clear();
        cv.put(Constants.BOOK_NAME, "Android in practice3");
        cv.put(Constants.BOOK_SIZE, 500);
        cv.put(Constants.BOOK_GENRE, 1);
        cv.put(Constants.BOOK_ISBN, 7);
        database.insert(Constants.BOOK_TABLE, null, cv);
        cv.clear();
        cv.put(Constants.BOOK_NAME, "Android in practice4");
        cv.put(Constants.BOOK_SIZE, 500);
        cv.put(Constants.BOOK_GENRE, 1);
        cv.put(Constants.BOOK_ISBN, 8);
        database.insert(Constants.BOOK_TABLE, null, cv);
        cv.clear();
        cv.put(Constants.AUTHOR_ID, 3);
        cv.put(Constants.BOOK_ID, 6);
        database.insert(Constants.AUTHOR_PLUS_BOOK_TABLE, null, cv);
        cv.clear();
        cv.put(Constants.AUTHOR_ID, 3);
        cv.put(Constants.BOOK_ID, 7);
        database.insert(Constants.AUTHOR_PLUS_BOOK_TABLE, null, cv);
        cv.clear();
        cv.put(Constants.AUTHOR_ID, 3);
        cv.put(Constants.BOOK_ID, 8);
        database.insert(Constants.AUTHOR_PLUS_BOOK_TABLE, null, cv);
        cv.clear();
        cv.put(Constants.AUTHOR_ID, 3);
        cv.put(Constants.BOOK_ID, 9);
        database.insert(Constants.AUTHOR_PLUS_BOOK_TABLE, null, cv);
        cv.clear();

        // выводим в лог данные по должностям
        Cursor c;
        Log.d(LOG_TAG, "--- table books ---");
        c = database.query(Constants.BOOK_TABLE, null, null, null, null, null, null);
        logCursor(c);
        c.close();
        Log.d(LOG_TAG, "--- Table a_b ---");
        c = database.query(Constants.AUTHOR_PLUS_BOOK_TABLE, null, null, null, null, null, null);
        logCursor(c);
        c.close();
        Log.d(LOG_TAG, "--- ---");
//        Log.d(LOG_TAG, "--- Table concat test  ---");
//        c = database.rawQuery("SELECT GROUP_CONCAT ( " + Constants.BOOK_NAME + "  ) AS order_summary" +
//                " FROM " + Constants.BOOK_TABLE +
//                " WHERE " + Constants.BOOK_GENRE + " = 1", null);
//        logCursor(c);
//        c.close();
//        Log.d(LOG_TAG, "--- ---");

        Log.d(LOG_TAG, "--- Table 1 ---");
        c = database.rawQuery(queryTrimmedData, null);
        logCursor(c);
        c.close();
        Log.d(LOG_TAG, "--- ---");

    }

    static void logCursor(Cursor c) {
        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : c.getColumnNames()) {
                        str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                    }
                    Log.d(LOG_TAG, str);
                } while (c.moveToNext());
            }
        } else
            Log.d(LOG_TAG, "Cursor is null");
    }

    public void deleteAllData() {

        context.deleteDatabase(Constants.DATABASE_NAME);
        open();

//        database.delete(Constants.BOOK_TABLE, null, null);
//        database.delete(Constants.AUTHORS_TABLE, null, null);
//        database.delete(Constants.GENRES_TABLE, null, null);
//        database.delete(Constants.AUTHOR_PLUS_BOOK_TABLE, null, null);

    }

    public void addGenre(String genre) {
        ContentValues cv = new ContentValues();
        cv.put(Constants.GENRE_NAME, genre);
        database.insert(Constants.GENRES_TABLE, null, cv);
        Cursor c;
        Log.d(LOG_TAG, "--- table books ---");
        c = database.query(Constants.GENRES_TABLE, null, null, null, null, null, null);
        logCursor(c);
        c.close();
    }

    public void addAuthor(String name, String lastName) {
        ContentValues cv = new ContentValues();
        cv.put(Constants.AUTHOR_NAME, name);
        cv.put(Constants.AUTHOR_LAST_NAME, lastName);
        database.insert(Constants.AUTHORS_TABLE, null, cv);
        Cursor c;
        Log.d(LOG_TAG, "--- table books ---");
        c = database.query(Constants.AUTHORS_TABLE, null, null, null, null, null, null);
        logCursor(c);
        c.close();
    }

    public void addAuthor(String name) {
        ContentValues cv = new ContentValues();
        cv.put(Constants.AUTHOR_NAME, name);
        database.insert(Constants.AUTHORS_TABLE, null, cv);
        Cursor c;
        Log.d(LOG_TAG, "--- table books ---");
        c = database.query(Constants.AUTHORS_TABLE, null, null, null, null, null, null);
        logCursor(c);
        c.close();
    }

    public void open() {
        dbHelper = new DBHelper(context, Constants.DATABASE_NAME, null, 1);
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        if (dbHelper != null)
            dbHelper.close();
    }

    public Cursor getDataForListView() {

        return database.rawQuery(queryTrimmedData, null);
    }

    public static Cursor getAuthorsList() {
        return database.query(Constants.AUTHORS_TABLE,
                new String[]{Constants.AUTHOR_NAME, Constants.AUTHOR_LAST_NAME},
                null, null, null, null, null, null);
    }
    public static Cursor getGenres(){
        return database.query(Constants.GENRES_TABLE,new String[]{Constants.GENRE_NAME}, null, null, null, null, null, null);
    }

    private class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(Constants.CREATE_BOOKS_TABLE);
            db.execSQL(Constants.CREATE_AUTHOR_TABLE);
            db.execSQL(Constants.CREATE_AUTHORS_BOOKS_TABLE);
            db.execSQL(Constants.CREATE_GENRES_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
