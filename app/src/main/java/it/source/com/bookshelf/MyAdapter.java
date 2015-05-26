package it.source.com.bookshelf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.malinskiy.superrecyclerview.swipe.BaseSwipeAdapter;
import com.malinskiy.superrecyclerview.swipe.SwipeLayout;

import java.util.List;

import it.source.com.bookshelf.Database.BooksDatabase;

public class MyAdapter extends BaseSwipeAdapter<MyAdapter.ViewHolder> {

    List<BookInfo> bookInfoList;

    MyAdapter(List<BookInfo> bookInfoList) {
        this.bookInfoList = bookInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.item_swipeable, parent, false);

        final ViewHolder viewHolder = new ViewHolder(view);
        SwipeLayout swipeLayout = viewHolder.swipeLayout;
        swipeLayout.setDragEdge(SwipeLayout.DragEdge.Right);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(context, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(viewHolder.getLayoutPosition());
                Toast.makeText(v.getContext(), "Deleted " + viewHolder.getLayoutPosition(), Toast.LENGTH_SHORT).show();
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        BookInfo bi = bookInfoList.get(position);
        holder.name.setText(bi.getBookName());
        holder.authors.setText(bi.getBookAuthors());

//        holder.textView.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return bookInfoList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void remove(int position) {

        BooksDatabase.deleteBook(bookInfoList.get(position).getId() + 1);
//        MainScreen.onRefresh();

        closeItem(position);

        notifyItemRemoved(position);
    }

    public class ViewHolder extends BaseSwipeAdapter.BaseSwipeableViewHolder {
        public Button deleteButton;
        public ImageView cover;
        TextView name;
        TextView authors;

        public ViewHolder(View itemView) {
            super(itemView);
            deleteButton = (Button) itemView.findViewById(R.id.delete);
            name = (TextView) itemView.findViewById(R.id.tv_book_name);
            authors = (TextView) itemView.findViewById(R.id.tv_authors);
            cover = (ImageView) itemView.findViewById(R.id.iv_book_cover);

        }

    }
}
