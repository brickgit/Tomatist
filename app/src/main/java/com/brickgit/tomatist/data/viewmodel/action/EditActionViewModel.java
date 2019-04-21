package com.brickgit.tomatist.data.viewmodel.action;

import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.brickgit.tomatist.data.database.Action;

import java.util.Calendar;
import java.util.List;

/** Created by Daniel Lin on 2019/3/11. */
public class EditActionViewModel extends ActionViewModel {

  private MutableLiveData<String> mSelectedActionId = new MutableLiveData<>();
  private LiveData<Action> mSelectedAction =
      Transformations.map(
          Transformations.switchMap(
              mSelectedActionId, (selectedId) -> mDataRepository.getAction(selectedId)),
          (action) -> {
            if (action.getStartTime() != null) {
              mStartCalendar = Calendar.getInstance();
              mStartCalendar.setTime(action.getStartTime());
            } else {
              mStartCalendar = null;
            }
            if (action.getEndTime() != null) {
              mEndCalendar = Calendar.getInstance();
              mEndCalendar.setTime(action.getEndTime());
            } else {
              mEndCalendar = null;
            }
            return action;
          });

  public EditActionViewModel() {
    super();
  }

  @Override
  public LiveData<Action> getAction() {
    return mSelectedAction;
  }

  @Override
  public void init(Intent intent) {
    String selectedActionId = intent.getStringExtra(ACTION_ID_KEY);
    mSelectedActionId.setValue(selectedActionId != null ? selectedActionId : "");
  }

  @Override
  public void saveAction(String note, boolean isFinished, List<String> tagList) {
    Action action = mSelectedAction.getValue();
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
    mDataRepository.updateAction(action);
  }
}
