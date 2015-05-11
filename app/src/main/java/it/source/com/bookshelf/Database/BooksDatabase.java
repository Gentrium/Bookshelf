package it.source.com.bookshelf.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import it.source.com.bookshelf.Constants;

public class BooksDatabase {
    String sqlzapros = "SELECT " + Constants.BOOK_NAME + " , "
            + Constants.BOOK_COVER
            + ",  (SELECT "
            + Constants.AUTHOR_NAME + " FROM "
            + Constants.AUTHORS_TABLE + " WHERE "
            + Constants.BOOK_TABLE + "." + Constants.BOOK_ID
    + " = " + Constants.AUTHOR_PLUS_BOOK_TABLE + "." + Constants.BOOK_ID
    + " AND " + Constants.AUTHOR_PLUS_BOOK_TABLE + "." + Constants.AUTHOR_ID
    + " = " + Constants.AUTHORS_TABLE + "." + Constants.AUTHOR_ID +") FROM "
            + Constants.BOOK_TABLE ;

    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private Context context;
    private static final String LOG_TAG = "logs";

    public BooksDatabase(Context context) {
        this.context = context;
    }

    // temp method

    public void fillSomeData(){
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
        cv.clear();
        cv.put(Constants.AUTHOR_ID, 1);
        cv.put(Constants.BOOK_ID, 2);
        cv.clear();
        cv.put(Constants.AUTHOR_ID, 2);
        cv.put(Constants.BOOK_ID, 3);
        cv.clear();
        cv.put(Constants.AUTHOR_ID, 2);
        cv.put(Constants.BOOK_ID, 4);
        cv.clear();
        cv.put(Constants.AUTHOR_ID, 3);
        cv.put(Constants.BOOK_ID, 5);
        cv.clear();

        // выводим в лог данные по должностям
        Cursor c;
        Log.d(LOG_TAG, "--- Table position ---");
        c = database.query(Constants.BOOK_TABLE, null, null, null, null, null, null);
        logCursor(c);
        c.close();
        Log.d(LOG_TAG, "--- ---");
        Log.d(LOG_TAG, "--- Table  ---");
        c = database.rawQuery("SELECT GROUP_CONCAT ( " + Constants.BOOK_NAME + "  ) AS order_summary" +
                " FROM " + Constants.BOOK_TABLE +
                " WHERE " + Constants.BOOK_GENRE + " = 1", null);
        logCursor(c);
        c.close();
        Log.d(LOG_TAG, "--- ---");

        Log.d(LOG_TAG, "--- Table 1 ---");
        c = database.rawQuery(sqlzapros, null);
        logCursor(c);
        c.close();
        Log.d(LOG_TAG, "--- ---");

    }
    void logCursor(Cursor c) {
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

    public void deleteAllData(){
        database.delete(Constants.BOOK_TABLE, null, null);
        database.delete(Constants.AUTHORS_TABLE, null, null);
        database.delete(Constants.GENRES_TABLE, null, null);
        database.delete(Constants.AUTHOR_PLUS_BOOK_TABLE, null, null);
    }

    private static final String CREATE_BOOKS_TABLE = "create table "
            + Constants.BOOK_TABLE + " ("
            + Constants.BOOK_ID + " integer primary key autoincrement, "
            + Constants.BOOK_NAME + " text not null unique, "
            + Constants.BOOK_SIZE + " integer not null, "
            + Constants.BOOK_GENRE + " integer not null, "
            + Constants.BOOK_ISBN + " integer unique, "
            + Constants.BOOK_COVER + " text);";

    private static final  String CREATE_AUTHOR_TABLE = "create table "
            + Constants.AUTHORS_TABLE + " ( "
            + Constants.AUTHOR_ID + " integer primary key autoincrement, "
            + Constants.AUTHOR_NAME + " text not null, "
            + Constants.AUTHOR_LAST_NAME + " text); ";

    private static final String CREATE_GENRES_TABLE = "create table "
            + Constants.GENRES_TABLE + " ( "
            + Constants.GENRE_ID + " integer primary key autoincrement, "
            + Constants.GENRE_NAME + " text not null unique ); ";

    private static final String CREATE_AUTHORS_BOOKS_TABLE = "create table "
            + Constants.AUTHOR_PLUS_BOOK_TABLE + " ( "
            + Constants.BOOK_ID + " integer, "
            + Constants.AUTHOR_ID + " integer);";

    // SELECT Name , COVER FROM BOOKS INNER JOIN

    public void open(){
        dbHelper = new DBHelper(context, Constants.DATABASE_NAME, null, 1);
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        if(dbHelper != null)
            dbHelper.close();
    }

    public Cursor getDataForListView(){

        return database.rawQuery("sqlzapros",null);
    }

    private class DBHelper extends SQLiteOpenHelper{
        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_BOOKS_TABLE);
            db.execSQL(CREATE_AUTHOR_TABLE);
            db.execSQL(CREATE_AUTHORS_BOOKS_TABLE);
            db.execSQL(CREATE_GENRES_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
