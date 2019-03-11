package com.brickgit.tomatist.data.viewmodel;

import com.brickgit.tomatist.data.DataRepository;
import com.brickgit.tomatist.data.database.Action;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class ActionViewModel extends ViewModel {

  private DataRepository mDataRepository;

  public ActionViewModel() {
    mDataRepository = DataRepository.getInstance();
  }

  public void insertAction(Action action) {
    mDataRepository.insertAction(action);
  }

  public void updateAction(Action action) {
    mDataRepository.updateAction(action);
  }

  public LiveData<Action> getAction(long id) {
    return mDataRepository.getAction(id);
  }
}
