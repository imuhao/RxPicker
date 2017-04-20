
package com.caimuhao.rxpicker.ui.view;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * @author Smile
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;
    private int mSpanCount;
    private int mRadixX;
    private int mItemCountInLastLine;
    private int mOldItemCount = -1;

    public SpacesItemDecoration(int space) {
        this(space, 1);
    }

    public SpacesItemDecoration(int space, int spanCount) {
        this.mSpace = space;
        this.mSpanCount = spanCount;
        this.mRadixX = space / spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, final RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        final int sumCount = state.getItemCount();
        final int position = params.getViewLayoutPosition();
        final int spanSize;
        final int index;

        if (params instanceof GridLayoutManager.LayoutParams) {
            GridLayoutManager.LayoutParams gridParams = (GridLayoutManager.LayoutParams) params;
            spanSize = gridParams.getSpanSize();
            index = gridParams.getSpanIndex();

            if ((position == 0 || mOldItemCount != sumCount) && mSpanCount > 1) {
                int countInLine = 0;
                int spanIndex;

                for (int tempPosition = sumCount - mSpanCount; tempPosition < sumCount; tempPosition++) {
                    spanIndex = ((GridLayoutManager) parent.getLayoutManager()).getSpanSizeLookup().getSpanIndex(tempPosition, mSpanCount);
                    countInLine = spanIndex == 0 ? 1 : countInLine + 1;
                }
                mItemCountInLastLine = countInLine;
                if (mOldItemCount != sumCount) {
                    mOldItemCount = sumCount;
                    if (position != 0) {
                        parent.post(new Runnable() {
                            @Override
                            public void run() {
                                parent.invalidateItemDecorations();
                            }
                        });
                    }
                }
            }
        } else if (params instanceof StaggeredGridLayoutManager.LayoutParams) {
            spanSize = ((StaggeredGridLayoutManager.LayoutParams) params).isFullSpan() ? mSpanCount : 1;
            index = ((StaggeredGridLayoutManager.LayoutParams) params).getSpanIndex();
        } else {
            spanSize = 1;
            index = 0;
        }

        if (spanSize < 1 || index < 0 || spanSize > mSpanCount) {
            return;
        }

        outRect.left = mSpace - mRadixX * index;
        outRect.right = mRadixX + mRadixX * (index + spanSize - 1);

        if (mSpanCount == 1 && position == sumCount - 1) {
            outRect.bottom = mSpace;
        } else if (position >= sumCount - mItemCountInLastLine && position < sumCount) {
            outRect.bottom = mSpace;
        }
        outRect.top = mSpace;
    }
}