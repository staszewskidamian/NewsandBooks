package com.dmns.nanonewsbooks;

/**
 * adhere we can build and adjust desire data to display
 * that one will show a book{thumpnailUrl, title, author, etc}
 */

// represents single feed

public class BookItemValue {

    // global variables title, date etc:, m stands for member

    private String mBookTitle;
    private String mBookAuthor;
    private String mBookUrl;
    private String mCategory;
    private String mThumbnailURL;
    private int mPageCount;
    private String mDescription;

    // public constructor for the class

    /**
     * Create a new TourItem object for Sightseeing, Shopping
     *
     * @param thumbnailURL is the thumbnail shown as the book's image
     * @param url          is the link to the book's previewURL page
     * @param title        is the book's title
     * @param author       is the book's author
     * @param pageCount    is the book's number of pages
     * @param category     is the book's category
     * @param description  is a short description of the book
     */

    public BookItemValue(String thumbnailURL, String url, String title, String author, int
            pageCount, String description, String category) {

        this.mThumbnailURL = thumbnailURL;
        this.mBookUrl = url;
        this.mBookTitle = title;
        this.mBookAuthor = author;
        this.mPageCount = pageCount;
        this.mDescription = description;
        this.mCategory = category;
    }

    // public getter methods, so other classes can access to it

    public String getmThumbnailURL() {
        return mThumbnailURL;
    }

    public String getmCategory() {
        return mCategory;
    }

    public String getmBookTitle() {
        return mBookTitle;
    }

    public String getmBookAuthor() {
        return mBookAuthor;
    }

    public String getmPageCount() {
        return String.valueOf(mPageCount);
    }

    public String getmDescription() {
        return mDescription;
    }


    public String getBookUrl() {
        return mBookUrl;
    }


}
