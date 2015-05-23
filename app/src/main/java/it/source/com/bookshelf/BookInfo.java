package it.source.com.bookshelf;


import android.os.Parcel;
import android.os.Parcelable;

public class BookInfo implements Parcelable {
    private String bookName;
    private String bookCover;
    private String bookAuthors;
    private long isbn;
    private int size;
    private int id;
    private int genre;

    public BookInfo(String bookName, String bookCover, String bookAuthors, long isbn, int size, int id, int genre) {
        this.bookName = bookName;
        this.bookCover = bookCover;
        this.bookAuthors = bookAuthors;
        this.isbn = isbn;
        this.size = size;
        this.id = id;
        this.genre = genre;
    }

    public BookInfo(String bookName, String bookCover, String bookAuthors) {
        this(bookName,bookCover,bookAuthors, 0,0,0,0);
//        this.bookName = bookName;
//        this.bookCover = bookCover;
//        this.bookAuthors = bookAuthors;
    }

    public BookInfo(Parcel source) {
        String[] strings = new String[3];
        int[] ints = new int[3];
        source.readStringArray(strings);
        source.readIntArray(ints);
        isbn = source.readLong();
        bookName = strings[0];
        bookCover = strings[1];
        bookAuthors = strings[2];
        id = ints[0];
        size = ints[1];
        genre = ints[2];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{bookName, bookCover, bookAuthors});
        dest.writeIntArray(new int[]{id, size, genre});
        dest.writeLong(isbn);
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookCover() {
        return bookCover;
    }

    public String getBookAuthors() {
        return bookAuthors;
    }

    public long getIsbn() {
        return isbn;
    }

    public int getSize() {
        return size;
    }

    public int getId() {
        return id;
    }

    public int getGenre() {
        return genre;
    }

    public static final Parcelable.Creator<BookInfo> CREATOR = new Parcelable.Creator<BookInfo>() {

        @Override
        public BookInfo createFromParcel(Parcel source) {
            return new BookInfo(source);
        }

        @Override
        public BookInfo[] newArray(int size) {
            return new BookInfo[size];
        }
    };
}
