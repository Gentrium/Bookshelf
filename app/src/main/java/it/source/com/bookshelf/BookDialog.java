package it.source.com.bookshelf;

import android.app.DialogFragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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

    static BookDialog openMode(int position) {
        BookDialog bookDialog = new BookDialog();
        Cursor c = BooksDatabase.selectBook(position);
        Bundle args = new Bundle();
        args.putByte(Constants.MODE_TYPE, Constants.MODE_OPEN);
        c.moveToFirst();
        args.putInt(Constants.BOOK_ID, c.getInt(0));
        args.putString(Constants.BOOK_NAME, c.getString(1));
        args.putString(Constants.BOOK_COVER, c.getString(2));
        args.putInt(Constants.BOOK_SIZE, c.getInt(3));
        args.putLong(Constants.BOOK_ISBN, c.getLong(4));
        args.putString(Constants.GENRE_NAME, c.getString(5));
        args.putString(Constants.AUTHOR_NAME, c.getString(6));

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
        ctx = this.getActivity();
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

        return v;
    }

    private void addBook() {

    }

    private void openBook(BookInfo bi) {

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
//        sp_genre.setAdapter(new ArrayAdapter<>(ctx,
//                R.layout.support_simple_spinner_dropdown_item,
//                new String[]{bi.getBookCover()}));

    }


}
