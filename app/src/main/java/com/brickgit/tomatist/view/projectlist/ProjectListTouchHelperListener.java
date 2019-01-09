package com.brickgit.tomatist.view.projectlist;

public interface ProjectListTouchHelperListener {
  void onItemSelect();

  void onItemUnselect();

  void onItemMove(int fromPosition, int toPosition);

  void onItemDismiss(int position);
}
