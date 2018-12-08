package com.brickgit.tomatist.view;

public interface ItemTouchHelperListener {
  void onItemMove(int fromPosition, int toPosition);

  void onItemDismiss(int position);
}
