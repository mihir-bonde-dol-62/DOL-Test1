package com.example.android.books;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

class BookLoader extends AsyncTaskLoader<List<Book>>
{

    /** The url to query the API */
    private String mSearchUrl;

    /** Data from the API */
    private List<Book> mData;

    BookLoader(Context context, String url)
    {
        super(context);
        mSearchUrl = url;
    }

    @Override
    protected void onStartLoading()
    {
        if (mData != null)
        {
            deliverResult(mData);
        } else {
            forceLoad();
        }
    }

    @Override
    public List<Book> loadInBackground() {
        // Check for valid string url
        if (mSearchUrl == null) {
            return null;
        }

        // Returns the list of books matching search criteria from Google books
        return QueryUtils.fetchBooks(mSearchUrl);
    }

    @Override
    public void deliverResult(List<Book> data)
    {
        mData = data; // Cache data
        super.deliverResult(data);
    }
}
