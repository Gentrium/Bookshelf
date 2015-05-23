package it.source.com.bookshelf;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;


public class BookDialog extends DialogFragment{

    private ImageView iv_cover;
    private Button btn_select_cover;
    private Button btn_accept;
    private Button btn_cancel;
    private EditText et_book_name;
    private EditText et_book_size;
    private EditText et_book_isbn;
    private Spinner sp_genre;
    private LinearLayout ll_authors;


    static BookDialog openMode(int position){
        BookDialog bookDialog = new BookDialog();
//        Cursor mCursor = myContDB.query(DATABASE_TABLE, null, COLUMN_ID
//                        + " = ?", new String[] { String.valueOf(id) }, null, null,
//                COLUMN_LASTNAME);
        return bookDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(R.string.str_title_genre_add);
        View v = inflater.inflate(R.layout.book_dialog,null);
        iv_cover = (ImageView) v.findViewById(R.id.iv_book_cover);
        btn_select_cover = (Button) v.findViewById(R.id.btn_select_cover);
        btn_accept = (Button) v.findViewById(R.id.btn_book_accept);
        btn_cancel = (Button) v.findViewById(R.id.btn_book_cancel);
        et_book_name = (EditText) v.findViewById(R.id.et_book_name);
        et_book_size = (EditText) v.findViewById(R.id.et_size);
        et_book_isbn = (EditText) v.findViewById(R.id.et_isbn);
        sp_genre = (Spinner) v.findViewById(R.id.sp_genre);
        ll_authors = (LinearLayout) v.findViewById(R.id.ll_authors);
        v.findViewById(R.id.btn_ok_genre);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
