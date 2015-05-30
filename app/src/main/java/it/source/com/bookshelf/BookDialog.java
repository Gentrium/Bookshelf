package it.source.com.bookshelf;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.source.com.bookshelf.Database.BooksDatabase;


public class BookDialog extends DialogFragment {

    Context ctx;
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

    static BookDialog openMode(int position) {
        BookDialog bookDialog = new BookDialog();
        Cursor c = BooksDatabase.selectBook(position);
        Bundle args = new Bundle();
        args.putByte(Constants.MODE_TYPE, Constants.MODE_OPEN);
        c.moveToFirst();

        BookInfo bi = new BookInfo(c.getString(1), c.getString(2), c.getString(6), c.getLong(4), c.getInt(3), c.getInt(0), c.getString(5));
        args.putParcelable(Constants.BOOK_KEY, bi);
        c.close();
        bookDialog.setArguments(args);
        return bookDialog;
    }

    static BookDialog addMode() {
        BookDialog f = new BookDialog();
        Bundle args = new Bundle();
        args.putByte(Constants.MODE_TYPE, Constants.MODE_ADDING);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        getDialog().setTitle(R.string.str_title_genre_add);

        View v = inflater.inflate(R.layout.book_dialog, null);
        ctx = this.getActivity().getBaseContext();
        iv_cover = (ImageView) v.findViewById(R.id.iv_book_cover);
        btn_select_cover = (Button) v.findViewById(R.id.btn_select_cover);
        btn_accept = (Button) v.findViewById(R.id.btn_book_accept);
        btn_cancel = (Button) v.findViewById(R.id.btn_book_cancel);
        et_book_name = (EditText) v.findViewById(R.id.et_book_name);
        et_book_size = (EditText) v.findViewById(R.id.et_size);
        et_book_isbn = (EditText) v.findViewById(R.id.et_isbn);
        sp_genre = (Spinner) v.findViewById(R.id.sp_genre);
        ll_authors = (LinearLayout) v.findViewById(R.id.ll_authors);
        if (getArguments().getByte(Constants.MODE_TYPE) == Constants.MODE_OPEN) {
            openBook((BookInfo) getArguments().getParcelable(Constants.BOOK_KEY));
        } else if (getArguments().getByte(Constants.MODE_TYPE) == Constants.MODE_ADDING) {
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

        return v;
    }

    private void addBook() {
        getDialog().setTitle(R.string.str_add_book);
        sp_genre.setAdapter(new ArrayAdapter<>(ctx,
                R.layout.support_simple_spinner_dropdown_item,
                getGenresList()));


    }
    private List<String> getGenresList(){
        List<String> genres = new ArrayList<>();
        Cursor c = BooksDatabase.getGenres();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            genres.add(c.getString(0));
        }
        return genres;
    }

    private void openBook(BookInfo bi) {
        getDialog().setTitle(bi.getBookName());
        try {
            Bitmap photo = MediaStore.Images.Media
                    .getBitmap(ctx.getContentResolver(), Uri.parse(bi.getBookCover()));
            Bitmap photoScaled = Bitmap
                    .createScaledBitmap(photo, 120, 120, true);
            iv_cover.setImageBitmap(photoScaled);
            if (photo != photoScaled) {
                photo.recycle();
            }
        } catch (Exception e) {
            iv_cover.setImageResource(R.drawable.book_image);
        }
        btn_select_cover.setVisibility(View.GONE);
        btn_cancel.setText(R.string.str_back);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        et_book_name.setText(bi.getBookName());
        et_book_size.setText(String.valueOf(bi.getSize()));
        et_book_isbn.setText(String.valueOf(bi.getIsbn()));
        TextView tv = new TextView(ctx);
        tv.setText(bi.getBookAuthors());
        ll_authors.addView(tv);
        sp_genre.setAdapter(new ArrayAdapter<>(ctx,
                R.layout.support_simple_spinner_dropdown_item,
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

//    public void onActivityResult(int requestCode,
//                                 int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case 1:
//                if (resultCode == RESULT_OK) {
//                    uriCover = data.getData();
//                }
//                break;
//            default:
//                break;
//        }
//        try{
//            Bitmap photo = MediaStore.Images.Media
//                    .getBitmap(getContentResolver(), contactImageUri);
//            Bitmap photoScaled = Bitmap.createScaledBitmap(photo, 70, 70, false);
//            iv_cover.setImageBitmap(photoScaled);
//        } catch (Exception e) {
//            Toast.makeText(ctx, "Îøèáêà ÷òåíèÿ èçîáðàæåíèÿ", Toast.LENGTH_LONG)
//                    .show();
//        } catch(OutOfMemoryError e) {
//            Toast.makeText(ctx, "Èçîáðàæåíèå ñëèøêîì áîëüøîå", Toast.LENGTH_LONG)
//                    .show();
//        }
//
//    }
//


}
