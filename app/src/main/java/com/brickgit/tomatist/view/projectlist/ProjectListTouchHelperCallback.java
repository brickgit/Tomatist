package com.brickgit.tomatist.view.projectlist;

import android.util.Log;

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
    return true;
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
    Log.i("Daniel", "from " + from + " to " + to);
    if (from == 0 || to == 0) return false;

    mListener.onItemMove(from, to);
    return true;
  }

  @Override
  public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    mListener.onItemDismiss(viewHolder.getAdapterPosition());
  }
}
