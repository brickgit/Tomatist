package com.brickgit.tomatist.data.viewmodel.action;

import android.content.Intent;

import com.brickgit.tomatist.data.database.Action;
import com.brickgit.tomatist.data.database.Category;
import com.brickgit.tomatist.data.database.CategoryGroup;
import com.brickgit.tomatist.data.viewmodel.BaseViewModel;

import java.util.Calendar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

/** Created by Daniel Lin on 2019/3/10. */
public abstract class ActionViewModel extends BaseViewModel {

  public static final String ACTION_ID_KEY = "ACTION_ID_KEY";

  public static final String ACTION_YEAR_KEY = "ACTION_YEAR_KEY";
  public static final String ACTION_MONTH_KEY = "ACTION_MONTH_KEY";
  public static final String ACTION_DAY_KEY = "ACTION_DAY_KEY";
  public static final int INVALID_ACTION_DATE = -1;

  public static final String ACTION_CATEGORY_KEY = "ACTION_CATEGORY_KEY";

  protected MutableLiveData<String> mSelectedCategoryId = new MutableLiveData<>();
  protected LiveData<Category> mSelectedCategory =
      Transformations.switchMap(
          mSelectedCategoryId, (selectedId) -> mDataRepository.getCategory(selectedId));
  protected LiveData<CategoryGroup> mSelectedCategoryGroup =
      Transformations.switchMap(
          mSelectedCategory,
          (category) ->
              mDataRepository.getCategoryGroup(
                  (category != null && category.getGroupId() != null)
                      ? category.getGroupId()
                      : ""));

  protected Calendar mStartCalendar = Calendar.getInstance();
  protected Calendar mEndCalendar = Calendar.getInstance();

  ActionViewModel() {
    super();
  }

  public LiveData<Category> getSelectedCategory() {
    return mSelectedCategory;
  }

  public LiveData<CategoryGroup> getSelectedCategoryGroup() {
    return mSelectedCategoryGroup;
  }

  public void selectCategory(String selectedCategoryId) {
    mSelectedCategoryId.setValue(selectedCategoryId != null ? selectedCategoryId : "");
  }

  public Calendar getStartCalendar() {
    return mStartCalendar;
  }

  public void setStartCalendar(Calendar startCalendar) {
    mStartCalendar = startCalendar;
  }

  public Calendar getEndCalendar() {
    return mEndCalendar;
  }

  public void setEndCalendar(Calendar endCalendar) {
    mEndCalendar = endCalendar;
  }

  public abstract LiveData<Action> getAction();

  public abstract void init(Intent intent);

  public abstract void saveAction(String title, String note, boolean isFinished);
}
