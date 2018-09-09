package com.dmns.nanonewsbooks;


import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String BOOKS_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=15";

    private static final String NEWS_REQUEST_URL =
            "https://content.guardianapis.com/search?q=All&order-by=newest&show-tags=contributor&api-key=test";

    private static final int BOOKLOADER1 = 1;
    private static final int NEWSLOADER2 = 2;
    private TextView mBooksEmptyStateTextView;
    private TextView mNewsEmptyStateTextView;
    private RecyclerView bRecyclerView;
    private RecyclerView nRecyclerView;
    private NewsListRecycle adapter;
    private BooksListRecycle bAdapter;

    @OnClick(R.id.search_button)
    protected void newIntent() {
        if(isNetworkAvailable(this)) {
            Intent newIntent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(newIntent);
        }else { Toast.makeText(MainActivity.this, "No internet Connection!", Toast.LENGTH_LONG).show();  }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ButterKnife.bind(this);

        nRecyclerView = (RecyclerView) findViewById(R.id.newsToDisplay);
        nRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bAdapter = new BooksListRecycle(new ArrayList<BookItemValue>(), this);
        nRecyclerView.setAdapter(bAdapter);
        getLoaderManager().restartLoader(BOOKLOADER1, null, new MainActivity.LoaderBooksCallback());

        bRecyclerView = (RecyclerView) findViewById(R.id.booksToDisplay);
        bRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsListRecycle(new ArrayList<NewsItemValue>(), this);
        bRecyclerView.setAdapter(adapter);
        getLoaderManager().restartLoader(NEWSLOADER2, null, new MainActivity.LoaderNewsCallback());
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    private class LoaderNewsCallback implements LoaderManager.LoaderCallbacks<List<NewsItemValue>> {
        public Loader<List<NewsItemValue>> onCreateLoader(int i, Bundle bundle) {
            return new NewsTaskLoader(getApplicationContext(), NEWS_REQUEST_URL);
        }



        @Override
        public void onLoadFinished(Loader<List<NewsItemValue>> loader, List<NewsItemValue> getNewsValues) {
            if (getNewsValues != null && !getNewsValues.isEmpty()) {
                adapter.addAll(getNewsValues);
                View loadingIndicator = findViewById(R.id.news_loading_indicator);
                loadingIndicator.setVisibility(View.GONE);
            } else {
                mNewsEmptyStateTextView = (TextView) findViewById(R.id.empty_view_for_news_handling);
                mNewsEmptyStateTextView.setVisibility(View.VISIBLE);
                mNewsEmptyStateTextView.setText(R.string.noFeedback);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<NewsItemValue>> loader) {

            nRecyclerView.getRecycledViewPool().clear();

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
                View loadingIndicator = findViewById(R.id.books_loading_indicator);
                loadingIndicator.setVisibility(View.GONE);
            } else {
                mBooksEmptyStateTextView = (TextView) findViewById(R.id.empty_view_for_books_handling);
                mBooksEmptyStateTextView.setVisibility(View.VISIBLE);
                mBooksEmptyStateTextView.setText(R.string.noFeedback);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<BookItemValue>> loader) {
            bRecyclerView.getRecycledViewPool().clear();

        }
    }
}





