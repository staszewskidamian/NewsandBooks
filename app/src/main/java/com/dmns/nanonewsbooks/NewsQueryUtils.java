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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Helper methods related to requesting and receiving earthquake data from Guardian API.
 */
public final class NewsQueryUtils {

    private static final String LOG_TAG = NewsQueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link NewsQueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name NewsQueryUtils (and an object instance of NewsQueryUtils is not needed).
     */
    private NewsQueryUtils() {
    }

    public static List<NewsItemValue> fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of books
        List<NewsItemValue> getNewsValues = extractNewsFromJSON(jsonResponse);
        // Return the list of books
        return getNewsValues;
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

    private static String readFromStream(InputStream inputStream) throws IOException {
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
        String jsonResponse = "";
        final int READ_TIME = 10000;
        final int TIMEOUT = 10000;


        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
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
                jsonResponse = readFromStream(inputStream);
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
        return jsonResponse;
    }

    private static String formatDate(String rawDate) {
        String jsonPushedDateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat jsonTimeFormatter = new SimpleDateFormat(jsonPushedDateFormat);
        try {
            Date parsedJsonDate = jsonTimeFormatter.parse(rawDate);
            String finalDatePattern = "dd MMM, yyyy";
            SimpleDateFormat finalDateFormatter = new SimpleDateFormat(finalDatePattern, Locale.ENGLISH);
            return finalDateFormatter.format(parsedJsonDate);
        } catch (ParseException e) {
            Log.e("NewsQueryUtils", "Error parsing JSON date: ", e);
            return "";
        }
    }

    public static ArrayList<NewsItemValue> extractNewsFromJSON(String valueJSON) {

        if (TextUtils.isEmpty(valueJSON)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding news to
        ArrayList<NewsItemValue> getNewsValues = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // build up a list of news objects with the corresponding data.

            JSONObject jsonResponse = new JSONObject(valueJSON);
            JSONObject jsonResults = jsonResponse.getJSONObject("response");
            JSONArray resultsArray = jsonResults.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject resultValues = resultsArray.getJSONObject(i);
                String webTitle = resultValues.getString("webTitle");
                String url = resultValues.getString("webUrl");
                String date = resultValues.getString("webPublicationDate");
                date = formatDate(date);
                String section = resultValues.getString("sectionName");
                //for author we going deeper into tags
                JSONArray tagsArray = resultValues.getJSONArray("tags");
                String author = "";
                if (tagsArray.length() == 0) {
                    author = "Unknown author";
                } else {
                    for (int n = 0; n < tagsArray.length(); n++) {
                        JSONObject authorObject = tagsArray.getJSONObject(n);
                        author += authorObject.getString("webTitle") + ". ";
                    }
                }

                getNewsValues.add(new NewsItemValue(section, date, webTitle, author, url));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("NewsQueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of news
        return getNewsValues;
    }
}
