package com.brickgit.tomatist.data.viewmodel.action;

import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.brickgit.tomatist.data.database.Action;

import java.util.Calendar;
import java.util.List;

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
              action.setTagList(copiedAction.getTagList());
              action.setFinished(copiedAction.isFinished());
              action.setStartTime(copiedAction.getStartTime());
              action.setEndTime(copiedAction.getEndTime());
              action.setNote(copiedAction.getNote());
              if (action.getStartTime() != null) {
                mStartCalendar = Calendar.getInstance();
                mStartCalendar.setTime(action.getStartTime());
              } else {
                mStartCalendar = null;
              }
              if (action.getEndTime() != null) {
                mEndCalendar = Calendar.getInstance();
                mEndCalendar.setTime(action.getStartTime());
              } else {
                mEndCalendar = null;
              }
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
  public void saveAction(String note, boolean isFinished, List<String> tagList) {
    Action action = mCopyingAction.getValue();
    if (action == null) return;
    action.setNote(note);
    action.setFinished(isFinished);
    if (mStartCalendar != null) {
      action.setStartTime(mStartCalendar.getTime());
    } else {
      action.setStartTime(null);
    }
    if (mEndCalendar != null) {
      action.setEndTime(mEndCalendar.getTime());
    } else {
      action.setEndTime(null);
    }
    action.setTagList(tagList);
    mDataRepository.insertAction(action);
  }
}
