package it.source.com.bookshelf;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
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

import it.source.com.bookshelf.Database.BooksDatabase;


public class BookActivity extends Activity implements View.OnClickListener {

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
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_dialog);

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
        btn_plus_author = (Button) findViewById(R.id.btn_plus_author);

        if (getIntent().hasExtra(Constants.BOOK_ID)) {
            Cursor c = BooksDatabase.selectBook(getIntent().getIntExtra(Constants.BOOK_ID, 0));
            c.moveToFirst();
            BookInfo bi = new BookInfo(c.getString(1), c.getString(2), c.getString(6), c.getLong(4), c.getInt(3), c.getInt(0), c.getString(5));
            c.close();
            openMode(bi);
        } else {
            addBook();
        }

        btn_select_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });
    }

    private void openMode(BookInfo bi) {

        setTitle(bi.getBookName());
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
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void addBook() {
        setTitle(R.string.str_add_book);
        sp_genre.setAdapter(new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item,
                getGenresList()));
        btn_accept.setText(R.string.str_add_book);
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int [] authors = new int[0];
                et_book_isbn.getText();
                et_book_size.getText();

                if(authorCount == 1){
                    authors = new int[]{firstAuthor.getSelectedItemPosition()};
                }else if(authorCount == 2){
                    authors = new int[]{firstAuthor.getSelectedItemPosition(),
                            secondAuthor.getSelectedItemPosition()};
                }else if (authorCount == 3){
                    authors = new int[]{firstAuthor.getSelectedItemPosition(),
                            secondAuthor.getSelectedItemPosition(),
                            thirdAuthor.getSelectedItemPosition()};
                }else if (authorCount == 4){
                    authors = new int[]{firstAuthor.getSelectedItemPosition(),
                            secondAuthor.getSelectedItemPosition(),
                            thirdAuthor.getSelectedItemPosition(),
                            fourthAuthor.getSelectedItemPosition()};
                }

                try{
                    uriCover.toString();
                } catch (NullPointerException e) {
                    uriCover = Uri.parse("android.resource://it.source.com.bookshelf/drawable/person");
                }
                if(TextUtils.isEmpty(et_book_name.getText().toString()) ||
                        TextUtils.isEmpty(et_book_isbn.getText().toString()) ||
                        TextUtils.isEmpty(et_book_size.getText().toString())) {
                   Toast.makeText(BookActivity.this, R.string.empty_field,Toast.LENGTH_SHORT).show();
                } else {
                    BooksDatabase.addBook(et_book_name.getText().toString(),
                            uriCover.toString(),
                            sp_genre.getSelectedItemPosition(),
                            Integer.parseInt(et_book_size.getText().toString()),
                            Long.parseLong(et_book_isbn.getText().toString()),
                            authors);
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

//        ll_authors.addView(getLayoutInflater().inflate(R.layout.authors_one_spinner, ll_authors, false));

        btn_plus_author.setOnClickListener(this);
        adapter = new ArrayAdapter<>(this, R.layout.spinner_item, R.id.tv_spinner, getAuthorList());
        firstAuthor.setAdapter(adapter);
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_book_cancel:
                setResult(RESULT_CANCELED, new Intent());
                finish();
                break;
            case R.id.btn_plus_author:
                if (authorCount == 1) {
                    ll_authors.removeAllViews();
                    ll_authors.addView(getLayoutInflater().inflate(R.layout.authors_two_spinners, ll_authors));
                    secondAuthor = (Spinner) findViewById(R.id.secondAuthor);
                    secondAuthor.setAdapter(adapter);
                    authorCount++;
                } else if (authorCount == 2) {
                    ll_authors.addView(getLayoutInflater().inflate(R.layout.authors_three_spinners, ll_authors));
                    thirdAuthor = (Spinner) findViewById(R.id.thirdAuthor);
                    thirdAuthor.setAdapter(adapter);
                    authorCount++;
                } else if (authorCount == 3) {
                    ll_authors.addView(getLayoutInflater().inflate(R.layout.authors_four_spinners, ll_authors));
                    fourthAuthor = (Spinner) findViewById(R.id.fourthAuthor);
                    fourthAuthor.setAdapter(adapter);
                    authorCount++;
                } else {
                    Toast.makeText(BookActivity.this, R.string.err_too_many_authors, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
