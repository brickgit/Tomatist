package com.brickgit.tomatist.view.categorylist;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/** Created by Daniel Lin on 2019/2/10. */
public class CategoryListTouchHelperCallback extends ItemTouchHelper.Callback {

  private final CategoryListTouchHelperListener mListener;

  public CategoryListTouchHelperCallback(CategoryListTouchHelperListener listener) {
    mListener = listener;
  }

  @Override
  public boolean isLongPressDragEnabled() {
    return false;
  }

  @Override
  public boolean isItemViewSwipeEnabled() {
    return true;
  }

  @Override
  public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
    return makeMovementFlags(0, ItemTouchHelper.START | ItemTouchHelper.END);
  }

  @Override
  public boolean onMove(
      RecyclerView recyclerView,
      RecyclerView.ViewHolder viewHolder,
      RecyclerView.ViewHolder target) {
    return false;
  }

  @Override
  public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    mListener.onItemDismiss(viewHolder.getAdapterPosition());
  }
}
