package com.brickgit.tomatist.view;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

  private final ItemTouchHelperListener mListener;

  public ItemTouchHelperCallback(ItemTouchHelperListener listener) {
    mListener = listener;
  }

  @Override
  public boolean isLongPressDragEnabled() {
    return true;
  }

  @Override
  public boolean isItemViewSwipeEnabled() {
    return false;
  }

  @Override
  public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
    if (viewHolder.getAdapterPosition() == 0) return 0;
    return makeMovementFlags(
        ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.START | ItemTouchHelper.END);
  }

  @Override
  public boolean onMove(
      RecyclerView recyclerView,
      RecyclerView.ViewHolder viewHolder,
      RecyclerView.ViewHolder target) {
    int from = viewHolder.getAdapterPosition();
    int to = target.getAdapterPosition();
    if (from == 0 || to == 0) return false;

    mListener.onItemMove(from, to);
    return true;
  }

  @Override
  public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    mListener.onItemDismiss(viewHolder.getAdapterPosition());
  }
}
