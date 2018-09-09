package com.dmns.nanonewsbooks;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NewsListRecycle extends RecyclerView.Adapter<NewsListRecycle.NewsHolder> {

    private ArrayList<NewsItemValue> mNewsItemValues;
    private LayoutInflater inflater;

    public NewsListRecycle(ArrayList<NewsItemValue> mNewsItemValues, Context context) {
        this.mNewsItemValues = mNewsItemValues;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public NewsHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, final int position) {
        final NewsItemValue currentNews = mNewsItemValues.get(position);
        holder.mNewsTitle.setText(currentNews.getNewsTitle());
        holder.mNewsSection.setText(currentNews.getNewsSection());
        holder.mNewsAuthor.setText(currentNews.getNewsAuthor());
        holder.mNewsDate.setText(currentNews.getNewsDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Context context = view.getContext();
                String url = currentNews.getNewsUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNewsItemValues.size();
    }

    public void addAll(List<NewsItemValue> data) {
        mNewsItemValues.clear();
        mNewsItemValues.addAll(data);
        notifyDataSetChanged();
    }

    public class NewsHolder extends RecyclerView.ViewHolder {
        public TextView mNewsSection;
        public TextView mNewsDate;
        public TextView mNewsTitle;
        public TextView mNewsAuthor;

        public NewsHolder(View itemView) {
            super(itemView);
            mNewsSection = (TextView) itemView.findViewById(R.id.section);
            mNewsDate = (TextView) itemView.findViewById(R.id.date);
            mNewsTitle = (TextView) itemView.findViewById(R.id.title);
            mNewsAuthor = (TextView) itemView.findViewById(R.id.author);
        }

    }

}






