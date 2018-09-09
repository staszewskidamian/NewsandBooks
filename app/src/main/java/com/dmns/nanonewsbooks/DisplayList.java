package com.dmns.nanonewsbooks;


import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


public class DisplayList extends AppCompatActivity {
    private static final int BOOKLOADER1 = 1;
    private static final int NEWSLOADER2 = 2;
    private static String BOOKS_REQUEST_URL = "";
    private static String NEWS_REQUEST_URL = "";
    private TextView mBooksEmptyStateTextView;
    private TextView mNewsEmptyStateTextView;
    private RecyclerView mRecyclerView;
    private NewsListRecycle adapter;
    private BooksListRecycle bAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista);

        ButterKnife.bind(this);
        String result2 = this.getIntent().getStringExtra("type");
        String result = this.getIntent().getStringExtra("httpRequest");

        if (result2.equals("books")) {
            BOOKS_REQUEST_URL = result;
            mRecyclerView = (RecyclerView) findViewById(R.id.toDisplay);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            bAdapter = new BooksListRecycle(new ArrayList<BookItemValue>(), this);
            mRecyclerView.setAdapter(bAdapter);
            getLoaderManager().restartLoader(BOOKLOADER1, null, new LoaderBooksCallback());
        } else {
            NEWS_REQUEST_URL = result;
            mRecyclerView = (RecyclerView) findViewById(R.id.toDisplay);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new NewsListRecycle(new ArrayList<NewsItemValue>(), this);
            mRecyclerView.setAdapter(adapter);
            getLoaderManager().restartLoader(NEWSLOADER2, null, new LoaderNewsCallback());
        }
    }

    private class LoaderNewsCallback implements LoaderManager.LoaderCallbacks<List<NewsItemValue>> {
        public Loader<List<NewsItemValue>> onCreateLoader(int i, Bundle bundle) {
            return new NewsTaskLoader(getApplicationContext(), NEWS_REQUEST_URL);
        }

        @Override
        public void onLoadFinished(Loader<List<NewsItemValue>> loader, List<NewsItemValue> getNewsValues) {
            if (getNewsValues != null && !getNewsValues.isEmpty()) {
                adapter.addAll(getNewsValues);
                View loadingIndicator = findViewById(R.id.loading_indicator);
                loadingIndicator.setVisibility(View.GONE);
            } else {
                mNewsEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
                mNewsEmptyStateTextView.setText(R.string.noFeedback);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<NewsItemValue>> loader) {
            mRecyclerView.getRecycledViewPool().clear();

        }
    }

    private class LoaderBooksCallback implements LoaderManager.LoaderCallbacks<List<BookItemValue>> {
        public Loader<List<BookItemValue>> onCreateLoader(int i, Bundle bundle) {
            return new BooksTaskLoader(getApplicationContext(), BOOKS_REQUEST_URL);
        }

        @Override
        public void onLoadFinished(Loader<List<BookItemValue>> loader, List<BookItemValue> getBookValues) {
            if (getBookValues != null && !getBookValues.isEmpty()) {
                bAdapter.addAll(getBookValues);
                View loadingIndicator = findViewById(R.id.loading_indicator);
                loadingIndicator.setVisibility(View.GONE);
            } else {
                mBooksEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
                mBooksEmptyStateTextView.setText(R.string.noFeedback);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<BookItemValue>> loader) {
            mRecyclerView.getRecycledViewPool().clear();

        }
    }


}
