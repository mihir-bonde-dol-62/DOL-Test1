package com.example.android.books;

class Book {

    private String mTitle;

    private String mAuthors;

    private float mRating;

    private float mPrice;

    /**
     * Create book object
     * @param title   title of the book
     * @param authors author of the book
     * @param rating  average rating for the book
     * @param price   retail price of the book
     */
    Book(String title, String authors, float rating, float price)
    {
        this.mTitle = title;
        this.mAuthors = authors;
        this.mRating = rating;
        this.mPrice = price;
    }

    String getTitle()
    {
        return mTitle;
    }

    String getAuthor()
    {
        return mAuthors;
    }

    float getRating()
    {
        return mRating;
    }

    float getPrice()
    {
        return mPrice;
    }
}