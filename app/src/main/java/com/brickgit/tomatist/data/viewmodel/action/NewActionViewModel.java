package com.brickgit.tomatist.data.viewmodel.action;

import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.brickgit.tomatist.data.database.Action;

import java.util.List;

/** Created by Daniel Lin on 2019/3/11. */
public class NewActionViewModel extends ActionViewModel {

  private MutableLiveData<Action> mAction = new MutableLiveData<>();

  public NewActionViewModel() {
    super();
  }

  @Override
  public LiveData<Action> getAction() {
    return mAction;
  }

  @Override
  public void init(Intent intent) {
    Action action = new Action();
    mAction.setValue(action);
  }

  @Override
  public void saveAction(String note, boolean isFinished, List<String> tagList) {
    Action action = mAction.getValue();
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
