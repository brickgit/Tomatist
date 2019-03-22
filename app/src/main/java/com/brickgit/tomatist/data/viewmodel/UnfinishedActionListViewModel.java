package com.brickgit.tomatist.data.viewmodel;

import com.brickgit.tomatist.data.database.Action;
import com.brickgit.tomatist.data.database.Category;
import com.brickgit.tomatist.data.database.CategoryGroup;
import com.brickgit.tomatist.data.database.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

/** Created by Daniel Lin on 2019/3/10. */
public class UnfinishedActionListViewModel extends BaseViewModel {

  private MutableLiveData<String> mSelectedCategoryId = new MutableLiveData<>();

  private LiveData<Map<String, CategoryGroup>> mCategoryGroupMap =
      Transformations.map(
          mDataRepository.getCategoryGroups(),
          (groups) -> {
            Map<String, CategoryGroup> map = new HashMap<>();
            for (CategoryGroup group : groups) {
              map.put(group.getId(), group);
            }
            return map;
          });
  private LiveData<Map<String, Category>> mCategoryMap =
      Transformations.map(
          mDataRepository.getCategories(),
          (categories) -> {
            Map<String, Category> map = new HashMap<>();
            for (Category category : categories) {
              map.put(category.getId(), category);
            }
            return map;
          });
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
  private LiveData<List<Action>> mSelectedUnfinishedActionList =
      Transformations.switchMap(
          mSelectedCategoryId,
          (selectedId) ->
              !selectedId.isEmpty()
                  ? mDataRepository.getUnfinishedActions(selectedId)
                  : mDataRepository.getUnfinishedActions());

  public UnfinishedActionListViewModel() {
    super();
  }

  public LiveData<Map<String, CategoryGroup>> getCategoryGroupMap() {
    return mCategoryGroupMap;
  }

  public LiveData<Map<String, Category>> getCategoryMap() {
    return mCategoryMap;
  }

  public LiveData<Map<String, Tag>> getTagMap() {
    return mTagMap;
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

  public void selectCategory(String selectedCategoryId) {
    mSelectedCategoryId.setValue(selectedCategoryId != null ? selectedCategoryId : "");
  }

  public String getSelectedCategoryId() {
    return mSelectedCategoryId.getValue();
  }
}
