package com.brickgit.tomatist.view.tasklist;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/** Created by Daniel Lin on 2019/2/12. */
public class TaskListCategoryViewData extends ExpandableGroup<TaskListTaskViewData> {

  public TaskListCategoryViewData(String categoryTitle, List<TaskListTaskViewData> tasks) {
    super(categoryTitle, tasks);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) return false;
    if (!(obj instanceof TaskListCategoryViewData)) return false;
    return this.getTitle().equals(((TaskListCategoryViewData) obj).getTitle());
  }
}
