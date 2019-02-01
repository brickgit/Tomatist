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
  private Map<String, LiveData<List<Activity>>> mActivities;

  public ActivityViewModel() {
    mDataRepository = DataRepository.getInstance();
    mActivities = new HashMap<>();
  }

  public void insertActivity(Activity activity) {
    mDataRepository.insertActivity(activity);
  }

  public void updateActivity(Activity activity) {
    mDataRepository.updateActivity(activity);
  }

  public LiveData<Activity> getActivity(long id) {
    return mDataRepository.getActivity(id);
  }

  public LiveData<List<Activity>> getActivities(int year, int month, int day) {
    String key = String.format(Locale.getDefault(), "%d/%d/%d", year, month, day);
    if (mActivities.containsKey(key)) {
      return mActivities.get(key);
    }

    LiveData<List<Activity>> activities = mDataRepository.getActivities(year, month, day);
    mActivities.put(key, activities);
    return activities;
  }
}
