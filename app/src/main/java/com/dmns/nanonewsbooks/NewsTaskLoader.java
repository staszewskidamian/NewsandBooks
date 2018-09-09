package com.dmns.nanonewsbooks;

/**
 * Created by developer on 14/02/2017.
 */

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Loads a list of  by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class NewsTaskLoader extends AsyncTaskLoader<List<NewsItemValue>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = NewsTaskLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    public NewsTaskLoader(Context context, String url) {
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
    public List<NewsItemValue> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of news.
        return NewsQueryUtils.fetchNewsData(mUrl);
    }

}