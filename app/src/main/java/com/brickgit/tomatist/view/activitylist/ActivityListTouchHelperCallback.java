package com.brickgit.tomatist.view.activitylist;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ActivityListTouchHelperCallback extends ItemTouchHelper.Callback {

  private final ActivityListTouchHelperListener mListener;

  public ActivityListTouchHelperCallback(ActivityListTouchHelperListener listener) {
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
    if (viewHolder.getAdapterPosition() == 0) return 0;
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
