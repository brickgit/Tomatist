package com.brickgit.tomatist.data.viewmodel;

import com.brickgit.tomatist.data.database.Action;
import com.brickgit.tomatist.data.database.Category;
import com.brickgit.tomatist.data.database.CategoryGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

/** Created by Daniel Lin on 2019/3/10. */
public class UnfinishedActionListViewModel extends BaseViewModel {

  private MutableLiveData<Long> mSelectedCategoryId = new MutableLiveData<>();

  private LiveData<Map<Long, CategoryGroup>> mCategoryGroupMap =
      Transformations.map(
          mDataRepository.getCategoryGroups(),
          (groups) -> {
            Map<Long, CategoryGroup> map = new HashMap<>();
            for (CategoryGroup group : groups) {
              map.put(group.getId(), group);
            }
            return map;
          });
  private LiveData<Map<Long, Category>> mCategoryMap =
      Transformations.map(
          mDataRepository.getCategories(),
          (categories) -> {
            Map<Long, Category> map = new HashMap<>();
            for (Category category : categories) {
              map.put(category.getId(), category);
            }
            return map;
          });
  private LiveData<List<Action>> mSelectedUnfinishedActionList =
      Transformations.switchMap(
          mSelectedCategoryId,
          (selectedId) ->
              selectedId != null
                  ? mDataRepository.getUnfinishedActions(selectedId)
                  : mDataRepository.getUnfinishedActions());

  public UnfinishedActionListViewModel() {
    super();
  }

  public LiveData<Map<Long, CategoryGroup>> getCategoryGroupMap() {
    return mCategoryGroupMap;
  }

  public LiveData<Map<Long, Category>> getCategoryMap() {
    return mCategoryMap;
  }

  public LiveData<List<Action>> getUnfinishedActions() {
    return mSelectedUnfinishedActionList;
  }

  public void insertAction(Action action) {
    mDataRepository.insertAction(action);
  }

  public void deleteAction(Action action) {
    mDataRepository.deleteAction(action);
  }

  public void selectCategory(Long selectedCategoryId) {
    mSelectedCategoryId.setValue(selectedCategoryId);
  }

  public Long getSelectedCategoryId() {
    return mSelectedCategoryId.getValue();
  }
}
