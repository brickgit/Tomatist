package com.brickgit.tomatist.data.viewmodel;

import com.brickgit.tomatist.data.DataRepository;
import com.brickgit.tomatist.data.database.Activity;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class ActivityViewModel extends ViewModel {

  private DataRepository mDataRepository;
  private Map<String, LiveData<List<Activity>>> mFinishedActivities;
  private LiveData<List<Activity>> mUnfinishedActivities;

  public ActivityViewModel() {
    mDataRepository = DataRepository.getInstance();
    mFinishedActivities = new HashMap<>();
  }

  public void insertActivity(Activity activity) {
    mDataRepository.insertActivity(activity);
  }

  public void updateActivity(Activity activity) {
    mDataRepository.updateActivity(activity);
  }

  public void deleteActivity(Activity activity) {
    mDataRepository.deleteActivity(activity);
  }

  public LiveData<Activity> getActivity(long id) {
    return mDataRepository.getActivity(id);
  }

  public LiveData<List<Activity>> getFinishedActivities(int year, int month, int day) {
    String key = String.format(Locale.getDefault(), "%d/%d/%d", year, month, day);
    if (mFinishedActivities.containsKey(key)) {
      return mFinishedActivities.get(key);
    }

    LiveData<List<Activity>> activities = mDataRepository.getFinishedActivities(year, month, day);
    mFinishedActivities.put(key, activities);
    return activities;
  }

  public LiveData<List<Activity>> getUnfinishedActivities() {
    if (mUnfinishedActivities == null) {
      mUnfinishedActivities = mDataRepository.getUnfinishedActivities();
    }

    return mUnfinishedActivities;
  }
}
