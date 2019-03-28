package com.brickgit.tomatist.data.viewmodel;

import com.brickgit.tomatist.data.database.Action;
import com.brickgit.tomatist.data.database.Tag;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

/** Created by Daniel Lin on 2019/3/26. */
public class ReportViewModel extends BaseViewModel {

  private MutableLiveData<String> mSelectedDate = new MutableLiveData<>();
  private int mSelectedYear;
  private int mSelectedMonth;
  private int mSelectedDay;
  private List<String> mSelectedTagIdList = new ArrayList<>();

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

  private LiveData<List<GroupedActionsItem>> mGroupedActionList =
      Transformations.map(
          Transformations.switchMap(
              mSelectedDate,
              (selectedDate) ->
                  mDataRepository.getFinishedActions(mSelectedYear, mSelectedMonth, mSelectedDay)),
          (actions) -> {
            Map<String, GroupedActionsItem> map = new HashMap<>();
            for (Action action : actions) {
              for (String tagId : action.getTagList()) {
                if (!mSelectedTagIdList.isEmpty() && !mSelectedTagIdList.contains(tagId)) {
                  continue;
                }
                if (!map.containsKey(tagId)) {
                  map.put(tagId, new GroupedActionsItem(tagId));
                }
                GroupedActionsItem item = map.get(tagId);
                item.addAction(action);
              }
            }
            List<GroupedActionsItem> itemList = new ArrayList<>();
            for (String tagId : map.keySet()) {
              itemList.add(map.get(tagId));
            }

            Collections.sort(
                itemList, (o1, o2) -> (int) (o1.getTotalMinutes() - o2.getTotalMinutes()));

            int size = itemList.size();
            if (size > 10) {
              return itemList.subList(itemList.size() - 10, itemList.size());
            } else {
              return itemList;
            }
          });

  public LiveData<Map<String, Tag>> getTagMap() {
    return mTagMap;
  }

  public LiveData<List<GroupedActionsItem>> getGroupedActionList() {
    return mGroupedActionList;
  }

  public void selectDate(int year, int month, int day) {
    String newSelectedDate = String.format(Locale.getDefault(), "%d/%d/%d", year, month + 1, day);
    if (newSelectedDate.equals(mSelectedDate.getValue())) return;
    mSelectedYear = year;
    mSelectedMonth = month;
    mSelectedDay = day;
    mSelectedDate.setValue(newSelectedDate);
  }

  public void backward() {
    Calendar date = Calendar.getInstance();
    date.set(Calendar.YEAR, mSelectedYear);
    date.set(Calendar.MONTH, mSelectedMonth);
    date.set(Calendar.DAY_OF_MONTH, mSelectedDay);
    date.set(Calendar.HOUR_OF_DAY, 0);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.add(Calendar.DATE, -1);
    selectDate(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
  }

  public void forward() {
    Calendar date = Calendar.getInstance();
    date.set(Calendar.YEAR, mSelectedYear);
    date.set(Calendar.MONTH, mSelectedMonth);
    date.set(Calendar.DAY_OF_MONTH, mSelectedDay);
    date.set(Calendar.HOUR_OF_DAY, 0);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.add(Calendar.DATE, 1);
    selectDate(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
  }

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
