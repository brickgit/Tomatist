package com.brickgit.tomatist.data.viewmodel;

import com.brickgit.tomatist.data.DataRepository;
import com.brickgit.tomatist.data.database.Action;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class ActionViewModel extends ViewModel {

  private DataRepository mDataRepository;
  private Map<String, LiveData<List<Action>>> mFinishedActions;
  private LiveData<List<Action>> mUnfinishedActions;

  public ActionViewModel() {
    mDataRepository = DataRepository.getInstance();
    mFinishedActions = new HashMap<>();
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

  public LiveData<Action> getaction(long id) {
    return mDataRepository.getAction(id);
  }

  public LiveData<List<Action>> getFinishedActions(int year, int month, int day) {
    String key = String.format(Locale.getDefault(), "%d/%d/%d", year, month, day);
    if (mFinishedActions.containsKey(key)) {
      return mFinishedActions.get(key);
    }

    LiveData<List<Action>> actions = mDataRepository.getFinishedActions(year, month, day);
    mFinishedActions.put(key, actions);
    return actions;
  }

  public LiveData<List<Action>> getUnfinishedActions() {
    if (mUnfinishedActions == null) {
      mUnfinishedActions = mDataRepository.getUnfinishedActions();
    }

    return mUnfinishedActions;
  }
}
