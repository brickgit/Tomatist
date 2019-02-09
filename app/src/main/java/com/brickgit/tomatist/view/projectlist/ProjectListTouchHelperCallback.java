package com.brickgit.tomatist.view.projectlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ProjectListTouchHelperCallback extends ItemTouchHelper.Callback {

  private final ProjectListTouchHelperListener mListener;

  public ProjectListTouchHelperCallback(ProjectListTouchHelperListener listener) {
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

  @Override
  public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
    super.onSelectedChanged(viewHolder, actionState);
    mListener.onItemSelect();
  }

  @Override
  public void clearView(
      @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
    super.clearView(recyclerView, viewHolder);
    mListener.onItemUnselect();
  }
}
