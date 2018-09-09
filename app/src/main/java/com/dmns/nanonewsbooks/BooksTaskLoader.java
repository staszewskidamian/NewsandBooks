package com.dmns.nanonewsbooks;


import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Loads a list of  by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class BooksTaskLoader extends AsyncTaskLoader<List<BookItemValue>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = BooksTaskLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new {@link BooksTaskLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public BooksTaskLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<BookItemValue> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of books.
        return BooksQueryUtils.fetchBooksValuesData(mUrl);
    }
}