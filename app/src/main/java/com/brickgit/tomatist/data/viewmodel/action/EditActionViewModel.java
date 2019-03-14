package com.brickgit.tomatist.data.viewmodel.action;

import android.content.Intent;

import com.brickgit.tomatist.data.database.Action;
import com.brickgit.tomatist.data.database.Category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

/** Created by Daniel Lin on 2019/3/11. */
public class EditActionViewModel extends ActionViewModel {

  private MutableLiveData<String> mSelectedActionId = new MutableLiveData<>();
  private LiveData<Action> mSelectedAction =
      Transformations.map(
          Transformations.switchMap(
              mSelectedActionId, (selectedId) -> mDataRepository.getAction(selectedId)),
          (action) -> {
            mStartCalendar.setTime(action.getStartTime());
            mEndCalendar.setTime(action.getEndTime());
            return action;
          });

  public EditActionViewModel() {
    super();
  }

  public LiveData<Action> getAction() {
    return mSelectedAction;
  }

  public void init(Intent intent) {
    String selectedActionId = intent.getStringExtra(ACTION_ID_KEY);
    mSelectedActionId.setValue(selectedActionId != null ? selectedActionId : "");
  }

  public void saveAction(String title, String note, boolean isFinished) {
    Action action = mSelectedAction.getValue();
    if (action == null) return;

    Category category = mSelectedCategory.getValue();

    action.setTitle(title);
    action.setNote(note);
    action.setFinished(isFinished);
    action.setStartTime(mStartCalendar.getTime());
    action.setEndTime(mEndCalendar.getTime());
    action.setMinutes(
        (mEndCalendar.getTimeInMillis() - mStartCalendar.getTimeInMillis()) / (60 * 1000));
    action.setCategoryId(category != null ? category.getId() : null);
    mDataRepository.updateAction(action);
  }
}
