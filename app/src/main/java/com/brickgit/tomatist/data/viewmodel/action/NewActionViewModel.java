package com.brickgit.tomatist.data.viewmodel.action;

import android.content.Intent;

import com.brickgit.tomatist.data.database.Action;
import com.brickgit.tomatist.data.database.Category;

import java.util.Calendar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/** Created by Daniel Lin on 2019/3/11. */
public class NewActionViewModel extends ActionViewModel {

  private MutableLiveData<Action> mAction = new MutableLiveData<>();

  public NewActionViewModel() {
    super();
  }

  public LiveData<Action> getAction() {
    return mAction;
  }

  public void init(Intent intent) {
    int year = intent.getIntExtra(ACTION_YEAR_KEY, INVALID_ACTION_DATE);
    int month = intent.getIntExtra(ACTION_MONTH_KEY, INVALID_ACTION_DATE);
    int day = intent.getIntExtra(ACTION_DAY_KEY, INVALID_ACTION_DATE);

    if (year != INVALID_ACTION_DATE && month != INVALID_ACTION_DATE && day != INVALID_ACTION_DATE) {
      mStartCalendar.set(Calendar.YEAR, year);
      mStartCalendar.set(Calendar.MONTH, month);
      mStartCalendar.set(Calendar.DAY_OF_MONTH, day);
      mEndCalendar.set(Calendar.YEAR, year);
      mEndCalendar.set(Calendar.MONTH, month);
      mEndCalendar.set(Calendar.DAY_OF_MONTH, day);
    }

    Long categoryId = intent.getLongExtra(ACTION_CATEGORY_KEY, INVALID_CATEGORY_ID);
    if (categoryId == INVALID_CATEGORY_ID) categoryId = null;

    Action action = new Action();
    action.setStartTime(mStartCalendar.getTime());
    action.setEndTime(mEndCalendar.getTime());
    action.setCategoryId(categoryId);

    mAction.setValue(action);
  }

  public void saveAction(String title, String note, boolean isFinished) {
    Action action = mAction.getValue();
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
    mDataRepository.insertAction(action);
  }
}
