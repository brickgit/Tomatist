package com.brickgit.tomatist.data.viewmodel;

import com.brickgit.tomatist.data.database.Action;

import java.util.ArrayList;
import java.util.List;

/** Created by Daniel Lin on 2019/3/26. */
public class GroupedActionsItem {

  private String mTagId;
  private long mTotalMinutes;
  private List<Action> mActionList = new ArrayList<>();

  public GroupedActionsItem(String tagId) {
    mTagId = tagId;
  }

  public String getTagId() {
    return mTagId;
  }

  public long getTotalMinutes() {
    return mTotalMinutes;
  }

  public List<Action> getActionList() {
    return mActionList;
  }

  public void addAction(Action action) {
    mActionList.add(action);
    mTotalMinutes +=
        (action.getEndTime().getTime() - action.getStartTime().getTime()) / (60 * 1000);
  }
}
