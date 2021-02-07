package com.example.android.books;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class BookAdapter extends RecyclerView.Adapter<BookAdapter.CardViewHolder> {

    private final String LOG_TAG = BookAdapter.class.getSimpleName();
    /** str to hold book titles */
    private List<Book> mListOfBooks;

    /**
     * new link for RecyclerView
     * @param listOfBooks str of book titles
     */
    BookAdapter(List<Book> listOfBooks) {
        this.mListOfBooks = listOfBooks;
    }

    /**
     * Create new view
     * @param parent the activity that holds recycler
     * @param viewType used when there are more than one views to display
     */
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_card, parent, false);

        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        // Get current book
        Book currentBook = mListOfBooks.get(position);

        holder.bookTitle.setText(currentBook.getTitle());

        try
        {

            String authors = currentBook.getAuthor();

            if (!authors.isEmpty())
            {
                holder.bookAuthor.setText(authors);
            }

        } catch (NullPointerException e)
        {
            Log.v(LOG_TAG, "No information on authors");

            // Hide view from book
            holder.bookAuthor.setVisibility(View.INVISIBLE);
        }

        holder.bookRating.setRating(currentBook.getRating());

        String price = "";
        if (currentBook.getPrice() > 0)
        {
            price = "$" + currentBook.getPrice();
            holder.bookPrice.setText(price);
        }
    }

    /**
     * @return the int value for the total number of data items
     */
    @Override
    public int getItemCount() {
        return mListOfBooks.size();
    }

    /**
     * Clear all data from the adapter's data set
     */
    void clear() {
        mListOfBooks = new ArrayList<>();
    }

    void addAll(List<Book> data) {
        // Traverse the data list to add books to adapters data set
        for (int i = 0; i < data.size(); i++) {
            // Get book at current index
            Book book = data.get(i);
            // Add the book to the data set
            mListOfBooks.add(book);

            notifyDataSetChanged();
        }
    }

    /**
     * Static class with results of the findViewById() operations
     */
    static class CardViewHolder extends RecyclerView.ViewHolder {
        /** TextView for title of the book */
        TextView bookTitle;

        TextView bookAuthor;

        RatingBar bookRating;

        TextView bookPrice;

        CardViewHolder(View itemView) {
            super(itemView);

            // reference to set title of the book
            bookTitle = itemView.findViewById(R.id.book_title_text_view);

            bookAuthor = itemView.findViewById(R.id.author_text_view);

            bookRating = itemView.findViewById(R.id.rating_bar);

            bookRating.setMax(5);

            bookRating.setNumStars(5);

            Drawable progress = bookRating.getProgressDrawable();
            DrawableCompat.setTint(progress, Color.YELLOW);

            bookPrice = itemView.findViewById(R.id.retail_price_text_view);
        }
    }
}
