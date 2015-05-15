package it.source.com.bookshelf;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import it.source.com.bookshelf.Database.BooksDatabase;


public class MainScreen extends ActionBarActivity {

    BooksDatabase database;
    ListView lvBooks;
    MyAdapter myAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        database = new BooksDatabase(this);
        database.open();

        lvBooks = (ListView)findViewById(R.id.lv_books);
        myAdapter = new MyAdapter(this, database.getDataForListView(), 0);
        lvBooks.setAdapter(myAdapter);

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

    @Override
    protected void onResume() {
        super.onResume();
    }

    private class  MyAdapter extends CursorAdapter{

        private LayoutInflater mLayoutInflater;

        private MyAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ImageView photoIV = (ImageView) view
                    .findViewById(R.id.iv_book_cover);
            TextView tvBookName = (TextView) view
                    .findViewById(R.id.tv_book_name);
            TextView tvAuthors = (TextView) view
                    .findViewById(R.id.tv_authors);

            tvBookName.setText(cursor.getString(1));
            tvAuthors.setText(cursor.getString(3));

            /**
             * Sets Name and Lastname of contact in one textbox.

            if (cursor.getString(2).equals("")) {
                tvName.setText(cursor.getString(1));
            } else {
                tvName.setText(cursor.getString(2)
                        + ' '
                        + cursor.getString(1));
            }*/


            /**
             * Sets image and clears unused images
             */
            try{
                Bitmap photo = MediaStore.Images.Media
                        .getBitmap(getContentResolver(),
                                Uri.parse(cursor.getString(6)));
                Bitmap photoScaled = Bitmap
                        .createScaledBitmap(photo, 70, 70, true);
                photoIV.setImageBitmap(photoScaled);
                if (photo != photoScaled) {
                    photo.recycle();
                }
            } catch (Exception e) {
            }



        }

        @Override
        public View newView(Context context,
                            Cursor cursor, ViewGroup parent) {
            return  mLayoutInflater
                    .inflate(R.layout.item_book, parent, false);
        }

    }

}
