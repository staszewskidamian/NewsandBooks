package com.dmns.nanonewsbooks;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving book data from Google Book API.
 */
public final class BooksQueryUtils {

    private static final String LOG_TAG = BooksQueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link BooksQueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name BooksQueryUtils (and an object instance of BooksQueryUtils is not needed).
     */
    private BooksQueryUtils() {
    }

    public static List<BookItemValue> fetchBooksValuesData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonBooksResponse = null;
        try {
            jsonBooksResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of books
        List<BookItemValue> getBooksValues = extractBooksValuesFromJSON(jsonBooksResponse);

        // Return the list of books
        return getBooksValues;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }


    private static String readBooksFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    private static String makeHttpRequest(URL url) throws IOException {
        String jsonBooksResponse = "";
        final int READ_TIME = 10000;
        final int TIMEOUT = 10000;


        // If the URL is null, then return early.
        if (url == null) {
            return jsonBooksResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIME /* milliseconds */);
            urlConnection.setConnectTimeout(TIMEOUT /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonBooksResponse = readBooksFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonBooksResponse;
    }


    public static ArrayList<BookItemValue> extractBooksValuesFromJSON(String valueJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(valueJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding books to
        ArrayList<BookItemValue> getBooksValues = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {


            JSONObject jsonBooksResponse = new JSONObject(valueJSON);
            JSONArray resultsArray = jsonBooksResponse.getJSONArray("items");
            String author;
            String description;
            int pageCount;
            String category;
            String smallThumbnail;

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject resultValues = resultsArray.getJSONObject(i);
                JSONObject volumeInfo = resultValues.getJSONObject("volumeInfo");

                String url = volumeInfo.getString("infoLink");
                String title = volumeInfo.getString("title");

                if (volumeInfo.has("pageCount")) {
                    pageCount = volumeInfo.getInt("pageCount");
                } else {
                    pageCount = 0;
                }

                if (volumeInfo.has("description")) {
                    description = volumeInfo.getString("description");
                } else {
                    description = "";
                }

                if (volumeInfo.has("authors")) {
                    JSONArray authors = volumeInfo.getJSONArray("authors");
                    author = authors.getString(0);
                } else {
                    author = "";
                }
                if (volumeInfo.has("imageLinks")) {
                    JSONObject image = volumeInfo.getJSONObject("imageLinks");
                    smallThumbnail = image.getString("smallThumbnail");
                } else {
                    smallThumbnail = "";
                }
                if (volumeInfo.has("categories")) {
                    JSONArray categories = volumeInfo.getJSONArray("categories");
                    category = categories.getString(0);
                } else {
                    category = "";
                }


                getBooksValues.add(new BookItemValue(smallThumbnail, url, title, author, pageCount, description, category));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("BooksQueryUtils", "Problem parsing the JSON results", e);
        }

        // Return the list of books
        return getBooksValues;
    }


}
