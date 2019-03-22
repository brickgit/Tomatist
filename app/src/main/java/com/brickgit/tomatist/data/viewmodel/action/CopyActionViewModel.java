package com.brickgit.tomatist.data.viewmodel.action;

import android.content.Intent;

import com.brickgit.tomatist.data.database.Action;
import com.brickgit.tomatist.data.database.Category;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

/** Created by Daniel Lin on 2019/3/21. */
public class CopyActionViewModel extends ActionViewModel {

  private MutableLiveData<String> mCopiedActionId = new MutableLiveData<>();
  private LiveData<Action> mCopyingAction =
      Transformations.map(
          Transformations.switchMap(
              mCopiedActionId, (selectedId) -> mDataRepository.getAction(selectedId)),
          (copiedAction) -> {
            Action action = getAction().getValue();
            if (action == null) {
              action = new Action();
              action.setTitle(copiedAction.getTitle());
              action.setTagList(copiedAction.getTagList());
              action.setFinished(copiedAction.isFinished());
              action.setStartTime(copiedAction.getStartTime());
              action.setEndTime(copiedAction.getEndTime());
              action.setCategoryId(copiedAction.getCategoryId());
              action.setNote(copiedAction.getNote());
              mStartCalendar.setTime(action.getStartTime());
              mEndCalendar.setTime(action.getEndTime());
            }
            return action;
          });

  public CopyActionViewModel() {
    super();
  }

  @Override
  public LiveData<Action> getAction() {
    return mCopyingAction;
  }

  @Override
  public void init(Intent intent) {
    String copiedActionId = intent.getStringExtra(ACTION_ID_KEY);
    mCopiedActionId.setValue(copiedActionId != null ? copiedActionId : "");
  }

  @Override
  public void saveAction(String title, String note, boolean isFinished, List<String> tagList) {
    Action action = mCopyingAction.getValue();
    if (action == null) return;

    Category category = mSelectedCategory.getValue();

    action.setTitle(title);
    action.setNote(note);
    action.setFinished(isFinished);
    action.setStartTime(mStartCalendar.getTime());
    action.setEndTime(mEndCalendar.getTime());
    action.setCategoryId(category != null ? category.getId() : null);
    action.setTagList(tagList);
    mDataRepository.insertAction(action);
  }
}
