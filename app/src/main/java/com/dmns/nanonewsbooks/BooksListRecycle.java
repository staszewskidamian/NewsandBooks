package com.dmns.nanonewsbooks;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class BooksListRecycle extends RecyclerView.Adapter<BooksListRecycle.BooksHolder> {

    private ArrayList<BookItemValue> mBookItemValues;
    private LayoutInflater inflater;

    public BooksListRecycle(ArrayList<BookItemValue> mBookItemValues, Context context) {
        this.mBookItemValues = mBookItemValues;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public BooksHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        View view = inflater.inflate(R.layout.books_item, parent, false);

        return new BooksHolder(view);
    }

    @Override
    public void onBindViewHolder(BooksHolder holder, final int position) {

        final BookItemValue currentBook = mBookItemValues.get(position);
        holder.mBookTitle.setText(currentBook.getmBookTitle());
        holder.mBookAuthor.setText("Author: " + currentBook.getmBookAuthor());
        holder.mCategory.setText("Category: " + currentBook.getmCategory());
        holder.mPageCount.setText("Pages: " + currentBook.getmPageCount());
        holder.mDescription.setText(currentBook.getmDescription());
        Uri uri = Uri.parse(currentBook.getmThumbnailURL());
        Context context = holder.mThumbnailImageView.getContext();
        if (currentBook.getmThumbnailURL() != null && !currentBook.getmThumbnailURL().isEmpty()) {
            Picasso.with(context).load(uri).into(holder.mThumbnailImageView);
        } else {
            holder.mThumbnailImageView.setImageResource(R.drawable.thumbnail);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Context context = view.getContext();
                String url = currentBook.getBookUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mBookItemValues.size();
    }

    public void addAll(List<BookItemValue> data) {
        mBookItemValues.clear();
        mBookItemValues.addAll(data);
        notifyDataSetChanged();
    }

    public class BooksHolder extends RecyclerView.ViewHolder {
        public TextView mCategory;
        public TextView mPageCount;
        public TextView mBookTitle;
        public TextView mBookAuthor;
        public TextView mDescription;
        public ImageView mThumbnailImageView;

        public BooksHolder(View itemView) {
            super(itemView);
            mThumbnailImageView = (ImageView) itemView.findViewById(R.id.thumbnailUrl);
            mPageCount = (TextView) itemView.findViewById(R.id.pageCount);
            mDescription = (TextView) itemView.findViewById(R.id.description);
            mBookTitle = (TextView) itemView.findViewById(R.id.title);
            mBookAuthor = (TextView) itemView.findViewById(R.id.author);
            mCategory = (TextView) itemView.findViewById(R.id.category);

        }
    }
}



