package it.source.com.bookshelf.GUI;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.malinskiy.superrecyclerview.swipe.SwipeItemManagerInterface;

import java.util.ArrayList;
import java.util.List;

import it.source.com.bookshelf.Database.BookInfo;
import it.source.com.bookshelf.Database.BooksDatabase;
import it.source.com.bookshelf.Database.Constants;
import it.source.com.bookshelf.R;
import it.source.com.bookshelf.Utils.ItemListener;
import it.source.com.bookshelf.Utils.MyAdapter;


public class MainScreen extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener, OnMoreListener  {

    private BooksDatabase database;
    private MyAdapter myAdapter;
    private SuperRecyclerView mRecycler;
    private List<BookInfo> bookList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        mRecycler = (SuperRecyclerView) findViewById(R.id.list);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        database = new BooksDatabase(this);
        database.open();

        fillBookList();
        myAdapter = new MyAdapter(bookList);
        mRecycler.setAdapter(myAdapter);
        myAdapter.setMode(SwipeItemManagerInterface.Mode.Single);

        mRecycler.addOnItemTouchListener(new ItemListener.RecyclerItemClickListener(this, new ItemListener.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(MainScreen.this, BookActivity.class);
                intent.putExtra(Constants.BOOK_ID, position);
                startActivityForResult(intent, Constants.MODE_ADDING);

            }
        }));
        mRecycler.setRefreshListener(this);
        mRecycler.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        mRecycler.setupMoreListener(this, 1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add_book:
                database.fillSomeData();
                Intent intent = new Intent(this, BookActivity.class);
                startActivityForResult(intent, Constants.MODE_ADDING);
                break;
            case R.id.clear_all:
                database.deleteAllData();
                break;
            case R.id.add_genre:
                AlertDialog dialog = Dialogs.getDialog(this,this,Constants.ADD_GENRE);
                dialog.show();
                break;
            case R.id.add_author:
                AlertDialog dialogAuthor = Dialogs.getDialog(this,this,Constants.ADD_AUTHOR);
                dialogAuthor.show();
                break;
            case R.id.action_exit:
                finish();
                break;
        }
        fillBookList();
        myAdapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }

    private void fillBookList() {
        Cursor c = database.getDataForListView();
        bookList.clear();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            BookInfo bi = new BookInfo(c.getString(1),c.getString(2),c.getString(3));
            bookList.add(bi);
        }
    }

    public void deleteBook(int id){
        database.deleteBook(id);
    }

    @Override
    public void onRefresh() {
        bookList.clear();
        fillBookList();

        myAdapter.closeAllExcept(null);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.MODE_ADDING && resultCode == RESULT_OK){
            onRefresh();
        }
    }

    @Override
    public void onMoreAsked(int numberOfItems, int numberBeforeMore, int currentItemPos) {
        Toast.makeText(this, "No more books", Toast.LENGTH_LONG).show();
    }
}
