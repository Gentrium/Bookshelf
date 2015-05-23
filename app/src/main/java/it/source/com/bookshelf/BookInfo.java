package it.source.com.bookshelf;


import android.os.Parcel;
import android.os.Parcelable;

public class BookInfo implements Parcelable {
    String bookName;
    String bookCover;
    String bookAuthors;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
