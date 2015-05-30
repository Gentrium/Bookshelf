package it.source.com.bookshelf;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.malinskiy.superrecyclerview.swipe.SwipeItemManagerInterface;

import java.util.ArrayList;
import java.util.List;

import it.source.com.bookshelf.Database.BooksDatabase;


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
            Toast.makeText(MainScreen.this, "Clicked " + position, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainScreen.this, BookActivity.class);
                intent.putExtra(Constants.BOOK_ID,position);
                startActivityForResult(intent, Constants.MODE_ADDING);

//                final DialogFragment newFragment = BookDialog.openMode(position);
////                newFragment.setStyle(DialogFragment.STYLE_NO_TITLE,0);
//                newFragment.show(getFragmentManager(), "dialog");
            }
        }));
        mRecycler.setRefreshListener(this);
        mRecycler.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        mRecycler.setupMoreListener(this, 1);

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
                Intent intent = new Intent(this, BookActivity.class);
                startActivityForResult(intent, Constants.MODE_ADDING);
//                final DialogFragment newFragment = BookDialog.addMode();
////                newFragment.setStyle(DialogFragment.STYLE_NO_TITLE,0);
//                newFragment.show(getFragmentManager(), "dialog");
                break;
            case R.id.clear_all:
                database.deleteAllData();
                break;
            case R.id.add_genre:
                addGenre();
                break;
            case R.id.add_author:
                addAuthor();
                break;
            case R.id.action_exit:
                finish();
                break;
        }
        fillBookList();
        myAdapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }
//    void openBook(View v){
//        Toast.makeText( Toast.makeText(v.getContext(), "Clicked " , Toast.LENGTH_SHORT).show();
//    }

    @Override
    protected void onPause() {
        super.onPause();
//        database.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        database.open();
    }

    private void fillBookList() {
        Cursor c = database.getDataForListView();
        bookList.clear();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            BookInfo bi = new BookInfo(c.getString(1),c.getString(2),c.getString(3));
            bookList.add(bi);
        }
    }

    private void addGenre(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //you should edit this to fit your needs
        builder.setTitle(R.string.str_title_genre_add);

        final EditText genre = new EditText(this);
        genre.setHint(R.string.str_hint_genre);
        genre.setInputType(InputType.TYPE_CLASS_TEXT);

        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(genre);
        builder.setView(lay);

        // Set up the buttons
        builder.setPositiveButton(R.string.str_add_genre, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(TextUtils.isEmpty(genre.getText().toString())){
                    Toast.makeText(getBaseContext(),R.string.empty_genre_name_feild,Toast.LENGTH_SHORT).show();
                }else
                    database.addGenre(genre.getText().toString());
            }
        });

        builder.setNegativeButton(R.string.str_cancel_btn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    private void addAuthor(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.str_title_add_author);

        final EditText name = new EditText(this);
        name.setHint(R.string.str_hint_author_name);
        name.setInputType(InputType.TYPE_CLASS_TEXT);
        final EditText lastName = new EditText(this);
        lastName.setHint(R.string.str_hint_author_lastName);
        lastName.setInputType(InputType.TYPE_CLASS_TEXT);

        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(name);
        lay.addView(lastName);
        builder.setView(lay);

        // Set up the buttons
        builder.setPositiveButton(R.string.str_add_author, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(TextUtils.isEmpty(name.getText().toString())){
                    Toast.makeText(getBaseContext(),R.string.empty_author_name_feild ,Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(lastName.getText().toString()))
                    database.addAuthor(name.getText().toString());
                else
                    database.addAuthor(name.getText().toString(), lastName.getText().toString());
            }
        });

        builder.setNegativeButton(R.string.str_cancel_btn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        builder.show();
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
