package it.source.com.bookshelf.GUI;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.source.com.bookshelf.Database.BookInfo;
import it.source.com.bookshelf.Database.BooksDatabase;
import it.source.com.bookshelf.Database.Constants;
import it.source.com.bookshelf.R;


public class BookActivity extends ActionBarActivity implements View.OnClickListener {

    private ImageView iv_cover;
    private Button btn_select_cover;
    private Button btn_accept;
    private Button btn_cancel;
    private EditText et_book_name;
    private EditText et_book_size;
    private EditText et_book_isbn;
    private Spinner sp_genre;
    private LinearLayout ll_authors;
    private Uri uriCover;

    private Spinner firstAuthor;
    private Spinner secondAuthor;
    private Spinner thirdAuthor;
    private Spinner fourthAuthor;
    private Button btn_plus_author;
    private byte authorCount = 1;
    private ArrayAdapter<String> authorAdapter;
    private ArrayAdapter<String> genreAdapter;
    private byte mode;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_dialog);

        initUi();
        secondAuthor.setVisibility(View.GONE);
        thirdAuthor.setVisibility(View.GONE);
        fourthAuthor.setVisibility(View.GONE);

        if (getIntent().hasExtra(Constants.BOOK_ID)) {
            Cursor c = BooksDatabase.selectBook(getIntent().getIntExtra(Constants.BOOK_ID, 0));
            c.moveToFirst();
            BookInfo bi = new BookInfo(c.getString(1), c.getString(2), c.getString(6), c.getLong(4), c.getInt(3), c.getInt(0), c.getString(5));
            c.close();
            mode = Constants.MODE_OPEN;
            openMode(bi);
        } else {
            mode = Constants.MODE_ADDING;
            setTitle(R.string.str_add_book);
            sp_genre.setAdapter(genreAdapter);
            btn_accept.setText(R.string.str_add_book);
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        MainScreen ms = new MainScreen();
        switch (id) {
            case R.id.add_genre:
                AlertDialog dialog = Dialogs.getDialog(this,this,Constants.ADD_GENRE);
                dialog.show();
                break;
            case R.id.add_author:
                AlertDialog dialogAuthor = Dialogs.getDialog(this,this,Constants.ADD_AUTHOR);
                dialogAuthor.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void initUi(){
        iv_cover = (ImageView) findViewById(R.id.iv_book_cover);

        btn_select_cover = (Button) findViewById(R.id.btn_select_cover);
        btn_accept = (Button) findViewById(R.id.btn_book_accept);
        btn_cancel = (Button) findViewById(R.id.btn_book_cancel);

        et_book_name = (EditText) findViewById(R.id.et_book_name);
        et_book_size = (EditText) findViewById(R.id.et_size);
        et_book_isbn = (EditText) findViewById(R.id.et_isbn);

        sp_genre = (Spinner) findViewById(R.id.sp_genre);
        ll_authors = (LinearLayout) findViewById(R.id.ll_authors);

        firstAuthor = (Spinner) findViewById(R.id.firstAuthor);
        secondAuthor = (Spinner) findViewById(R.id.secondAuthor);
        thirdAuthor = (Spinner) findViewById(R.id.thirdAuthor);
        fourthAuthor = (Spinner) findViewById(R.id.fourthAuthor);

        btn_plus_author = (Button) findViewById(R.id.btn_plus_author);
        btn_select_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });
        btn_accept.setOnClickListener(this);
        btn_plus_author.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        authorAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, R.id.tv_spinner, getAuthorList());
        genreAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,getGenresList());
        firstAuthor.setAdapter(authorAdapter);
        secondAuthor.setAdapter(authorAdapter);
        thirdAuthor.setAdapter(authorAdapter);
        fourthAuthor.setAdapter(authorAdapter);
    }

    private void openMode(BookInfo bi) {

        setTitle(bi.getBookName());
        id = bi.getId();

        if (bi.getBookCover() != null)
            setCover(Uri.parse(bi.getBookCover()));

        btn_select_cover.setVisibility(View.GONE);
        btn_plus_author.setVisibility(View.GONE);

        firstAuthor.setVisibility(View.GONE);

        btn_cancel.setText(R.string.str_back);

        et_book_name.setText(bi.getBookName());
        et_book_size.setText(String.valueOf(bi.getSize()));
        et_book_isbn.setText(String.valueOf(bi.getIsbn()));

        TextView tv = new TextView(this);
        tv.setText(bi.getBookAuthors());
        ll_authors.addView(tv);
        sp_genre.setAdapter(new ArrayAdapter<>(this,
                R.layout.spinner_item,
                R.id.tv_spinner,
                new String[]{bi.getGenre()}));

        sp_genre.setEnabled(false);
        et_book_isbn.setEnabled(false);
        et_book_name.setEnabled(false);
        et_book_size.setEnabled(false);

        btn_accept.setText(R.string.str_edit);
    }

    private List<String> getAuthorList() {
        final List<String> authors = new ArrayList<>();
        Cursor c = BooksDatabase.getAuthorsList();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            authors.add(c.getString(0) + ' ' + c.getString(1));
        }
        return authors;
    }

    private List<String> getGenresList() {
        List<String> genres = new ArrayList<>();
        Cursor c = BooksDatabase.getGenres();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            genres.add(c.getString(0));
        }
        return genres;
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK)
            uriCover = data.getData();
        setCover(uriCover);


    }


    private void setCover(Uri uri) {
        try {
            Bitmap photo = MediaStore.Images.Media
                    .getBitmap(getContentResolver(), uri);
            Bitmap photoScaled = Bitmap
                    .createScaledBitmap(photo, 120, 120, true);
            iv_cover.setImageBitmap(photoScaled);
            if (photo != photoScaled) {
                photo.recycle();
            }
        } catch (Exception e) {
            iv_cover.setImageResource(R.drawable.book_image);
        }
    }

    private int[] getSelectedAuthors(){
        int[] authors = new int[0];

        if (authorCount == 1) {
            authors = new int[]{firstAuthor.getSelectedItemPosition()};
        } else if (authorCount == 2) {
            authors = new int[]{firstAuthor.getSelectedItemPosition(),
                    secondAuthor.getSelectedItemPosition()};
        } else if (authorCount == 3) {
            authors = new int[]{firstAuthor.getSelectedItemPosition(),
                    secondAuthor.getSelectedItemPosition(),
                    thirdAuthor.getSelectedItemPosition()};
        } else if (authorCount == 4) {
            authors = new int[]{firstAuthor.getSelectedItemPosition(),
                    secondAuthor.getSelectedItemPosition(),
                    thirdAuthor.getSelectedItemPosition(),
                    fourthAuthor.getSelectedItemPosition()};
        }
        return authors;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_book_cancel:
                setResult(RESULT_CANCELED, new Intent());
                finish();
                break;
            case R.id.btn_plus_author:
                if (authorCount == 1) {
                    secondAuthor.setVisibility(View.VISIBLE);
                    authorCount++;
                } else if (authorCount == 2) {
                    thirdAuthor.setVisibility(View.VISIBLE);
                    authorCount++;
                } else if (authorCount == 3) {
                    fourthAuthor.setVisibility(View.VISIBLE);
                    btn_plus_author.setVisibility(View.GONE);
                } else {
                    Toast.makeText(BookActivity.this, R.string.err_too_many_authors, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_book_accept:
                switch (mode){
                    case Constants.MODE_ADDING:

                        try {
                            uriCover.toString();
                        } catch (NullPointerException e) {
                            uriCover = Uri.parse("android.resource://it.source.com.bookshelf/drawable/person");
                        }
                        if (TextUtils.isEmpty(et_book_name.getText().toString()) ||
                                TextUtils.isEmpty(et_book_isbn.getText().toString()) ||
                                TextUtils.isEmpty(et_book_size.getText().toString())) {
                            Toast.makeText(BookActivity.this, R.string.empty_field, Toast.LENGTH_SHORT).show();
                        } else {
                            BooksDatabase.addBook(et_book_name.getText().toString(),
                                    uriCover.toString(),
                                    sp_genre.getSelectedItemPosition(),
                                    Integer.parseInt(et_book_size.getText().toString()),
                                    Long.parseLong(et_book_isbn.getText().toString()),
                                    getSelectedAuthors());
                            setResult(RESULT_OK);
                            finish();
                        }
                        break;
                    case Constants.MODE_OPEN:
                        mode = Constants.MODE_EDIT;

                        sp_genre.setEnabled(true);
                        sp_genre.setAdapter(genreAdapter);
                        et_book_isbn.setEnabled(true);
                        et_book_name.setEnabled(true);
                        et_book_size.setEnabled(true);
                        btn_select_cover.setVisibility(View.VISIBLE);
                        btn_plus_author.setVisibility(View.VISIBLE);
                        btn_accept.setText(R.string.str_ok);

                        firstAuthor.setVisibility(View.VISIBLE);
                        ll_authors.refreshDrawableState();
                        break;
                    case Constants.MODE_EDIT:
                        try {
                            uriCover.toString();
                        } catch (NullPointerException e) {
                            uriCover = Uri.parse("android.resource://it.source.com.bookshelf/drawable/person");
                        }
                        if (TextUtils.isEmpty(et_book_name.getText().toString()) ||
                                TextUtils.isEmpty(et_book_isbn.getText().toString()) ||
                                TextUtils.isEmpty(et_book_size.getText().toString())) {
                            Toast.makeText(BookActivity.this, R.string.empty_field, Toast.LENGTH_SHORT).show();
                        } else {
                            BooksDatabase.updateBook(id, et_book_name.getText().toString(),
                                    uriCover.toString(),
                                    sp_genre.getSelectedItemPosition(),
                                    Integer.parseInt(et_book_size.getText().toString()),
                                    Long.parseLong(et_book_isbn.getText().toString()),
                                    getSelectedAuthors());
                            setResult(RESULT_OK);
                            finish();
                        }
                        break;
                }
        }
    }
}
