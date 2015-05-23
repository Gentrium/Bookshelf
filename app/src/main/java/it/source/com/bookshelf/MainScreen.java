package it.source.com.bookshelf;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.source.com.bookshelf.Database.BooksDatabase;


public class MainScreen extends ActionBarActivity {

    BooksDatabase database;
    RecyclerView rvBooks;
    MyRecycleAdapter myRecycleAdapter;
    List<BookInfo> bookList = new ArrayList<>();


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

        fillBookList();
        myRecycleAdapter = new MyRecycleAdapter(bookList);
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
                addGenre();
                break;
            case R.id.add_author:
                addAuthor();
                break;
            case R.id.action_exit:
                break;
        }
        fillBookList();
        myRecycleAdapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        database.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        database.open();
    }

    private void fillBookList() {
        Cursor c = database.getDataForListView();
        bookList.clear();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            BookInfo bi = new BookInfo();
            bi.bookName = c.getString(1);
            bi.bookAuthors = c.getString(3);
            bi.bookCover = c.getString(2);
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
