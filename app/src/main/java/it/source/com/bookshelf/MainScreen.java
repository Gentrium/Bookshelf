package it.source.com.bookshelf;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.source.com.bookshelf.Database.BooksDatabase;


public class MainScreen extends ActionBarActivity {

    BooksDatabase database;
    RecyclerView rvBooks;
    private RecyclerView.Adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        database = new BooksDatabase(this);
        database.open();

        rvBooks = (RecyclerView) findViewById(R.id.rv_books);
        rvBooks.setHasFixedSize(true);
        RecyclerView.LayoutManager lmRecycler = new LinearLayoutManager(this);
        rvBooks.setLayoutManager(lmRecycler);

        MyRecycleAdapter myRecycleAdapter =
                new MyRecycleAdapter(bookInfoList(database.getDataForListView()));
        rvBooks.setAdapter(myRecycleAdapter);

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

    private List<BookInfo> bookInfoList(Cursor c) {
        List<BookInfo> list = new ArrayList<>();
        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            BookInfo bi = new BookInfo();
            bi.bookName = c.getString(1);
            bi.bookAuthors = c.getString(3);
            bi.bookCover = c.getString(2);
            list.add(bi);
        }

        return list;
    }


    private class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.ViewHolder> {

        List<BookInfo> bookInfoList;

        private MyRecycleAdapter(List<BookInfo> bookInfoList) {
            this.bookInfoList = bookInfoList;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            BookInfo bi = bookInfoList.get(position);
            holder.name.setText(bi.bookName);
            holder.authors.setText(bi.bookAuthors);

        }

        @Override
        public int getItemCount() {
            return bookInfoList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            TextView authors;
            ImageView cover;

            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.tv_book_name);
                authors = (TextView) itemView.findViewById(R.id.tv_authors);
                cover = (ImageView) itemView.findViewById(R.id.iv_book_cover);
            }
        }
    }


}
