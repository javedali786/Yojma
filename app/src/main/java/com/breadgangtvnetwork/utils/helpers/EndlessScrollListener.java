package com.breadgangtvnetwork.utils.helpers;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.breadgangtvnetwork.utils.Logger;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {
    final LinearLayoutManager layoutManager;
    // before loading more.
    private final int visibleThreshold = 5;
    // The current offset index of data you have loaded
    private final int currentPage = 0;
    // The total number of items in the dataset after the last load
    private final int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private final boolean loading = true;
    // Sets the starting page index
    private final int startingPageIndex = 0;


    public EndlessScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;

    }


    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        try {
            if (dy > 0) //check for scroll down
            {
                if (!isLoading() && !isLastPage()) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= getTotalPageCount()) {
                        loadMoreItems();
                    }
                }
            }
        } catch (Exception e) {
            Logger.e("EndlessScrollListner", "" + e);

        }

    }


    protected abstract void loadMoreItems();

    public abstract int getTotalPageCount();

    public abstract boolean isLastPage();

    public abstract boolean isLoading();

}