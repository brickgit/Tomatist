package com.brickgit.tomatist.data.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.brickgit.tomatist.data.database.Action;
import com.brickgit.tomatist.data.database.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Created by Daniel Lin on 2019/3/10. */
public class UnfinishedActionListViewModel extends BaseViewModel {

  private LiveData<Map<String, Tag>> mTagMap =
      Transformations.map(
          mDataRepository.getTags(),
          (tags) -> {
            Map<String, Tag> map = new HashMap<>();
            for (Tag tag : tags) {
              map.put(tag.getId(), tag);
            }
            return map;
          });
  private LiveData<List<Action>> mSelectedUnfinishedActionList;

  public UnfinishedActionListViewModel() {
    super();
  }

  public LiveData<Map<String, Tag>> getTagMap() {
    return mTagMap;
  }

  public LiveData<List<Action>> getUnfinishedActions() {
    if (mSelectedUnfinishedActionList == null) {
      mSelectedUnfinishedActionList = mDataRepository.getUnfinishedActions();
    }
    return mSelectedUnfinishedActionList;
  }

  public void insertAction(Action action) {
    mDataRepository.insertAction(action);
  }

  public void updateAction(Action action) {
    mDataRepository.updateAction(action);
  }

  public void deleteAction(Action action) {
    mDataRepository.deleteAction(action);
  }
}
