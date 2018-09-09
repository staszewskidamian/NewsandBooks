package com.dmns.nanonewsbooks;

/**
 * @link NewsItemValue srip to a single values
 * adhere we can build and adjust desire data to display
 */

// represents single feed
public class NewsItemValue {

    // global variables title, date etc:, m stands for member
    private String mNewsSection;
    private String mNewsDate;
    private String mNewsTitle;
    private String mNewsAuthor;
    private String mNewsUrl;

    // public constructor for the class

    public NewsItemValue(String section, String date, String title, String author, String url) {

        // initializing member variables
        this.mNewsSection = section;
        this.mNewsDate = date;
        this.mNewsTitle = title;
        this.mNewsAuthor = author;
        this.mNewsUrl = url;
    }

    // public getter methods, so other classes can access to it

    /**
     * Methods for getting all the aforementioned variables
     */

    public String getNewsSection() {
        return mNewsSection;
    }

    public String getNewsDate() {
        return mNewsDate;
    }

    public String getNewsAuthor() {
        return mNewsAuthor;
    }

    public String getNewsTitle() {
        return mNewsTitle;
    }

    public String getNewsUrl() {
        return mNewsUrl;
    }


}
