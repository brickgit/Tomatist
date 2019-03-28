package com.brickgit.tomatist.data.viewmodel.report;

import com.brickgit.tomatist.data.database.Tag;
import com.brickgit.tomatist.data.viewmodel.BaseViewModel;
import com.brickgit.tomatist.data.viewmodel.GroupedActionsItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

/** Created by Daniel Lin on 2019/3/26. */
public abstract class ReportViewModel extends BaseViewModel {

  protected MutableLiveData<String> mSelectedDate = new MutableLiveData<>();
  protected int mSelectedYear;
  protected int mSelectedMonth;
  protected int mSelectedDay;
  protected List<String> mSelectedTagIdList = new ArrayList<>();

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

  public LiveData<Map<String, Tag>> getTagMap() {
    return mTagMap;
  }

  public abstract LiveData<List<GroupedActionsItem>> getGroupedActionList();

  public abstract void selectDate(int year, int month, int day);

  public abstract void backward();

  public abstract void forward();

  public void updateSelectedTagIdList(List<String> selectedTagIdList) {
    mSelectedTagIdList.clear();
    mSelectedTagIdList.addAll(selectedTagIdList);
    mSelectedDate.setValue(mSelectedDate.getValue());
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
