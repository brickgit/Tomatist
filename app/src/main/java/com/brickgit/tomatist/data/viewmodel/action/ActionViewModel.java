package com.brickgit.tomatist.data.viewmodel.action;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.brickgit.tomatist.data.database.Action;
import com.brickgit.tomatist.data.database.Tag;
import com.brickgit.tomatist.data.viewmodel.BaseViewModel;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Created by Daniel Lin on 2019/3/10. */
public abstract class ActionViewModel extends BaseViewModel {

  public static final String ACTION_ID_KEY = "ACTION_ID_KEY";

  public static final String ACTION_YEAR_KEY = "ACTION_YEAR_KEY";
  public static final String ACTION_MONTH_KEY = "ACTION_MONTH_KEY";
  public static final String ACTION_DAY_KEY = "ACTION_DAY_KEY";
  public static final int INVALID_ACTION_DATE = -1;

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

  @Nullable protected Calendar mStartCalendar;
  @Nullable protected Calendar mEndCalendar;

  ActionViewModel() {
    super();
  }

  public LiveData<Map<String, Tag>> getTagMap() {
    return mTagMap;
  }

  @Nullable
  public Calendar getStartCalendar() {
    return mStartCalendar;
  }

  public void setStartCalendar(@Nullable Calendar startCalendar) {
    mStartCalendar = startCalendar;
  }

  @Nullable
  public Calendar getEndCalendar() {
    return mEndCalendar;
  }

  public void setEndCalendar(@Nullable Calendar endCalendar) {
    mEndCalendar = endCalendar;
  }

  public abstract LiveData<Action> getAction();

  public abstract void init(Intent intent);

  public abstract void saveAction(String note, boolean isFinished, List<String> tagList);
}
