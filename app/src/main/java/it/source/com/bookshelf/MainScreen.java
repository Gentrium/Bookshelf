package it.source.com.bookshelf;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import it.source.com.bookshelf.Database.BooksDatabase;


public class MainScreen extends ActionBarActivity {

    BooksDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        database = new BooksDatabase(this);
        database.open();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        switch (id) {
            case R.id.add_book:
                database.fillSomeData();
                break;
            case R.id.clear_all:
                database.deleteAllData();
                break;
            case R.id.add_genre:
                break;
            case R.id.action_exit:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void btnAddClick(View view) {

    }

    class MyCursorLoader extends CursorLoader{

        BooksDatabase database;

        public MyCursorLoader(Context context, BooksDatabase database) {
            super(context);
            this.database = database;

        }

        @Override
        public Cursor loadInBackground(){
            Cursor cursor = database.getDataForListView();
            return cursor;
        }
    }
    class  MyAdapter extends CursorAdapter{

        public MyAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return null;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

        }
    }
}
