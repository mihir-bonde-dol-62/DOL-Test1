package com.example.android.books;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class QueryResultsActivity
        extends AppCompatActivity
        implements LoaderCallbacks<List<Book>>
{

    /** URL for books data from the Google books API */
    private String REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=";

    private static final String API_KEY = "AIzaSyCaNgg0GLoPlz75osYA3mDIYG0rWAZo01s";

    /** Adapter for the list of book titles */
    private BookAdapter mAdapter;

    private static final int EARTHQUAKE_LOADER_ID = 1;

    /** Indeterminate progress bar for loading books */
    private ProgressBar mProgressSpinner;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        // Inflate the activity UI
        setContentView(R.layout.list_of_books);

        BookRecyclerView recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);

        // Get the current orientation of the screen
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }

        // Initialize the adapter with the sample data
        mAdapter = new BookAdapter(new ArrayList<Book>());

        // Attach adapter to RecyclerView widget
        recyclerView.setAdapter(mAdapter);

        // Set empty view when there is no data on the recycler view
        mEmptyStateView = findViewById(R.id.empty_text_view);
        recyclerView.setEmptyView(mEmptyStateView);

        // Get reference to the Progress bar
        mProgressSpinner = findViewById(R.id.progress_spinner);
        // Indeterminate progress bar type
        mProgressSpinner.setIndeterminate(true);

        Intent queryIntent = getIntent();
        // Get the search text typed by the user
        String searchText = getIntent().getStringExtra("topic");
        String processedQuery = "";
        // Get the value for title key packaged in the intent
        String title = queryIntent.getStringExtra("title");
        // Get the value for author key
        String author = queryIntent.getStringExtra("author");
        // Get the value for isbn key
        String isbn = queryIntent.getStringExtra("isbn");

        // Determine which radio box was checked
        if (title != null) {
            // User is searching title of book
            processedQuery = searchText + "&" + title + searchText;
        } else if (author != null) {
            // User is searching author
            processedQuery = searchText + "&" + author + searchText;
        } else if (isbn != null) {
            // User is searching the isbn number
            processedQuery = searchText + "&" + isbn + searchText;
        } else {
            // No filters used
            processedQuery = searchText;
        }

        // Building url from user search
        REQUEST_URL += processedQuery + "&maxResults=40" + "&key=" + API_KEY;

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())
        {
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader manager
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, QueryResultsActivity.this);
        } else {
            // Otherwise, display error
            // First hide loading indicator so error message will be visible
            mProgressSpinner.setVisibility(View.GONE);

            mEmptyStateView.setText(R.string.no_internet_connection);
        }
    }

    /**
     * Create new loader object to load list of books after search
     */
    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args)
    {
        return new BookLoader(QueryResultsActivity.this, REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books)
    {
        // Hide progress bar
        mProgressSpinner.setVisibility(View.GONE);

        // Set empty state text to display "No books to display."
        mEmptyStateView.setText(R.string.no_books);

        mAdapter.clear();

        // Add valid list of books to the adapter
        if (books != null && !books.isEmpty())
        {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Clear existing data on adapter after loader is reset
        mAdapter.clear();
    }
}

