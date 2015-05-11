package it.source.com.bookshelf.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import it.source.com.bookshelf.Constants;

public class BooksDatabase {

    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private Context context;
    private static final String LOG_TAG = "logs";

    public void fillSomeData(){
        database.execSQL("INSERT " + Constants.BOOK_TABLE_NAME + " VALUES( N'Android in practice', 500, 1, 1);");
        database.execSQL("INSERT " + Constants.BOOK_TABLE_NAME + " VALUES( N'Thinking in java', 500, 1, 2);");
        database.execSQL("INSERT " + Constants.BOOK_TABLE_NAME + " VALUES( N'Java full guide', 400, 1, 3);");
        database.execSQL("INSERT " + Constants.BOOK_TABLE_NAME + " VALUES( N'Java 2 se', 300, 1, 4);");
        database.execSQL("INSERT " + Constants.BOOK_TABLE_NAME + " VALUES( N'Companions', 800, 2, 5);");
        database.execSQL("INSERT " + Constants.BOOK_TABLE_NAME + " VALUES( N'Game of thrones', 1100, 2, 6);");
        database.execSQL("INSERT " + Constants.GENRES_TABLE + " VALUES(N'Programming');");
        database.execSQL("INSERT " + Constants.GENRES_TABLE + " VALUES(N'Fantasy');");
        database.execSQL("INSERT " + Constants.AUTHORS_TABLE + " VALUES(N'Key', N'Horstmann');");
        database.execSQL("INSERT " + Constants.AUTHORS_TABLE + " VALUES(N'Harry', N'Cornell');");
        database.execSQL("INSERT " + Constants.AUTHORS_TABLE + " VALUES(N'Herbert', N'Shildt');");
        database.execSQL("INSERT " + Constants.AUTHORS_TABLE + " VALUES(N'Bruce', N'Ekkel');");
        database.execSQL("INSERT " + Constants.AUTHORS_TABLE + " VALUES(N'Robert', N'Salvatore');");
        database.execSQL("INSERT " + Constants.AUTHORS_TABLE + " VALUES(N'George', N'Martin');");
        database.execSQL("INSERT " + Constants.AUTHORS_TABLE + " VALUES(N'George', N'Martin');");
        // выводим в лог данные по должностям
        Cursor c;
        Log.d(LOG_TAG, "--- Table position ---");
        c = database.query(Constants.BOOK_TABLE_NAME, null, null, null, null, null, null);
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

    private static final String CREATE_BOOKS_TABLE = "create table "
            + Constants.BOOK_TABLE_NAME + " ("
            + Constants.BOOK_ID + " integer primary key autoincrement, "
            + Constants.BOOK_NAME + " text not null unique, "
            + Constants.BOOK_SIZE + " integer not null, "
            + Constants.BOOK_GENRE + " integer not null, "
            + Constants.BOOK_ISBN + " integer unique, "
            + Constants.BOOK_COVER + " text);";

    private static final  String CREATE_AUTHOR_TABLE = "create table"
            + Constants.AUTHORS_TABLE + " ( "
            + Constants.AUTHOR_ID + " integer primary key autoincrement, "
            + Constants.AUTHOR_NAME + " text not null, "
            + Constants.AUTHOR_LAST_NAME + " text); ";

    private static final String CREATE_GENRES_TABLE = "create table"
            + Constants.GENRES_TABLE + " ( "
            + Constants.GENRE_ID + " integer primary key autoincrement, "
            + Constants.GENRE_NAME + " text not null unique ); ";

    private static final String CREATE_AUTHORS_BOOKS_TABLE = "create table"
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
