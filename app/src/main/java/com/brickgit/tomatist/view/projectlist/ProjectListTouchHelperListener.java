package com.brickgit.tomatist.view.projectlist;

public interface ProjectListTouchHelperListener {
  void onItemMove(int fromPosition, int toPosition);

  void onItemDismiss(int position);
}
