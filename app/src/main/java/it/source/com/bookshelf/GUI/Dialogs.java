package it.source.com.bookshelf.GUI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.LinearLayout;

import it.source.com.bookshelf.Database.BooksDatabase;
import it.source.com.bookshelf.Database.Constants;
import it.source.com.bookshelf.R;

public class Dialogs {


    public static AlertDialog getDialog(Activity activity, Context context,int ID) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        switch(ID) {
            case Constants.ADD_AUTHOR:
                builder.setTitle(R.string.str_title_add_author);

                final EditText name = new EditText(context);
                name.setHint(R.string.str_hint_author_name);
                name.setInputType(InputType.TYPE_CLASS_TEXT);
                final EditText lastName = new EditText(context);
                lastName.setHint(R.string.str_hint_author_lastName);
                lastName.setInputType(InputType.TYPE_CLASS_TEXT);

                LinearLayout lay = new LinearLayout(context);
                lay.setOrientation(LinearLayout.VERTICAL);
                lay.addView(name);
                lay.addView(lastName);
                builder.setView(lay);

                // Set up the buttons
                builder.setPositiveButton(R.string.str_add_author, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(TextUtils.isEmpty(name.getText().toString())){
                            dialog.dismiss();
                        }else if(TextUtils.isEmpty(lastName.getText().toString()))
                            BooksDatabase.addAuthor(name.getText().toString());
                        else
                           BooksDatabase.addAuthor(name.getText().toString(), lastName.getText().toString());
                    }
                });

                builder.setNegativeButton(R.string.str_cancel_btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                return builder.create();
            case Constants.ADD_GENRE:
                builder.setTitle(R.string.str_title_genre_add);

                final EditText genre = new EditText(context);
                genre.setHint(R.string.str_hint_genre);
                genre.setInputType(InputType.TYPE_CLASS_TEXT);

                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(genre);
                builder.setView(layout);

                // Set up the buttons
                builder.setPositiveButton(R.string.str_add_genre, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(TextUtils.isEmpty(genre.getText().toString())){
                            dialog.dismiss();
                        }else
                            BooksDatabase.addGenre(genre.getText().toString());
                    }
                });

                builder.setNegativeButton(R.string.str_cancel_btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                return builder.create();
            default:
                return null;
        }
    }
}