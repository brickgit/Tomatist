package com.brickgit.tomatist.data.viewmodel;

import com.brickgit.tomatist.data.database.Action;
import com.brickgit.tomatist.data.database.Category;
import com.brickgit.tomatist.data.database.CategoryGroup;

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

  private LiveData<Map<Long, CategoryGroup>> mCategoryGroupMap;
  private LiveData<Map<Long, Category>> mCategoryMap;
  private LiveData<List<Action>> mFinishedActionList =
      Transformations.switchMap(
          mSelectedDate,
          (selectedDate) ->
              mDataRepository.getFinishedActions(mSelectedYear, mSelectedMonth, mSelectedDay));

  public CalendarActivityViewModel() {
    super();
  }

  public long insertCategoryGroup(CategoryGroup categoryGroup) {
    return mDataRepository.insertCategoryGroup(categoryGroup);
  }

  public long[] insertCategories(List<Category> categories) {
    return mDataRepository.insertCategories(categories);
  }

  public LiveData<Map<Long, CategoryGroup>> getCategoryGroupMap() {
    if (mCategoryGroupMap == null) {
      mCategoryGroupMap =
          Transformations.map(
              mDataRepository.getCategoryGroups(),
              (groups) -> {
                Map<Long, CategoryGroup> map = new HashMap<>();
                for (CategoryGroup group : groups) {
                  map.put(group.getId(), group);
                }
                return map;
              });
    }
    return mCategoryGroupMap;
  }

  public LiveData<Map<Long, Category>> getCategoryMap() {
    if (mCategoryMap == null) {
      mCategoryMap =
          Transformations.map(
              mDataRepository.getCategories(),
              (categories) -> {
                Map<Long, Category> map = new HashMap<>();
                for (Category category : categories) {
                  map.put(category.getId(), category);
                }
                return map;
              });
    }
    return mCategoryMap;
  }

  public void insertAction(Action action) {
    mDataRepository.insertAction(action);
  }

  public void deleteAction(Action action) {
    mDataRepository.deleteAction(action);
  }

  public LiveData<List<Action>> getFinishedActionList() {
    return mFinishedActionList;
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
