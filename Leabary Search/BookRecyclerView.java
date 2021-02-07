package com.example.android.books;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * RecyclerView subclass that supports providing an empty view which
 * is displayed when the adapter has no data and else hidden
 */
public class BookRecyclerView extends RecyclerView
{

    private View mEmptyView;

    private AdapterDataObserver mDataObserver = new AdapterDataObserver()
    {
        @Override
        public void onChanged()
        {
            super.onChanged();
            updateEmptyStatus();
        }
    };

    public BookRecyclerView(Context context) {
        super(context);
    }

    public BookRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BookRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * When the backing adapter has no data
     * this view will be made visible and the recycler view hidden
     */
    public void setEmptyView(View emptyView)
    {
        this.mEmptyView = emptyView;
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter)
    {
        if (getAdapter() != null)
        {
            getAdapter().unregisterAdapterDataObserver(mDataObserver);
        }

        if (adapter != null)
        {
            adapter.registerAdapterDataObserver(mDataObserver);
        }

        super.setAdapter(adapter);

        updateEmptyStatus();
    }

    private void updateEmptyStatus()
    {
        if (mEmptyView != null && getAdapter() != null)
        {

            // Determine whether empty view should be visible or not
            final boolean showEmptyView = getAdapter().getItemCount() == 0;
            mEmptyView.setVisibility(showEmptyView ? VISIBLE : GONE);

            // Hide or show recycler view based on showEmptyView
            setVisibility(showEmptyView ? GONE : VISIBLE);
        }
    }
}
