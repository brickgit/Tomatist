package com.brickgit.tomatist.data.viewmodel;

import com.brickgit.tomatist.data.DataRepository;
import com.brickgit.tomatist.data.database.Category;
import com.brickgit.tomatist.data.database.CategoryGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class CategoryViewModel extends ViewModel {

  private DataRepository mDataRepository;
  private LiveData<List<CategoryGroup>> mCategoryGroups;
  private Map<Long, LiveData<List<Category>>> mCategories;

  public CategoryViewModel() {
    mDataRepository = DataRepository.getInstance();
    mCategories = new HashMap<>();
  }

  public long insertCategoryGroup(CategoryGroup categoryGroup) {
    return mDataRepository.insertCategoryGroup(categoryGroup);
  }

  public LiveData<List<CategoryGroup>> getCategoryGroups() {
    if (mCategoryGroups == null) {
      mCategoryGroups = mDataRepository.getCategoryGroups();
    }

    return mCategoryGroups;
  }

  public long[] insertCategories(List<Category> categories) {
    return mDataRepository.insertCategories(categories);
  }

  public LiveData<List<Category>> getCategories(long categoryGroupId) {
    if (mCategories.containsKey(categoryGroupId)) {
      return mCategories.get(categoryGroupId);
    }

    LiveData<List<Category>> categories = mDataRepository.getCategories(categoryGroupId);
    mCategories.put(categoryGroupId, categories);
    return categories;
  }
}
