package com.brickgit.tomatist.view.actionlist;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ActionListTouchHelperCallback extends ItemTouchHelper.Callback {

  private final ActionListTouchHelperListener mListener;

  public ActionListTouchHelperCallback(ActionListTouchHelperListener listener) {
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
