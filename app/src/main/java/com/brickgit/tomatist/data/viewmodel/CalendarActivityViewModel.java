package com.brickgit.tomatist.data.viewmodel;

import com.brickgit.tomatist.data.database.Action;
import com.brickgit.tomatist.data.database.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

/** Created by Daniel Lin on 2019/3/10. */
public class CalendarActivityViewModel extends BaseViewModel {

  private MutableLiveData<String> mSelectedDate = new MutableLiveData<>();
  private int mSelectedYear;
  private int mSelectedMonth;
  private int mSelectedDay;

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
  private LiveData<List<Action>> mActionList =
      Transformations.switchMap(
          mSelectedDate,
          (selectedDate) ->
              mDataRepository.getActions(mSelectedYear, mSelectedMonth, mSelectedDay));

  public CalendarActivityViewModel() {
    super();
  }

  public LiveData<Map<String, Tag>> getTagMap() {
    return mTagMap;
  }

  public void updateAction(Action action) {
    mDataRepository.updateAction(action);
  }

  public void insertAction(Action action) {
    mDataRepository.insertAction(action);
  }

  public void deleteAction(Action action) {
    mDataRepository.deleteAction(action);
  }

  public LiveData<List<Action>> getActionList() {
    return mActionList;
  }

  public void selectDate(int year, int month, int day) {
    String newSelectedDate = String.format(Locale.getDefault(), "%d/%d/%d", year, month, day);
    if (newSelectedDate.equals(mSelectedDate.getValue())) return;
    mSelectedYear = year;
    mSelectedMonth = month;
    mSelectedDay = day;
    mSelectedDate.setValue(newSelectedDate);
  }

  public String getSelectedDate() {
    return mSelectedDate.getValue();
  }

  public int getSelectedYear() {
    return mSelectedYear;
  }

  public int getSelectedMonth() {
    return mSelectedMonth;
  }

  public int getSelectedDay() {
    return mSelectedDay;
  }
}
