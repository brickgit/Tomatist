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
  private Map<Long, LiveData<List<Category>>> mCategoriesMap;
  private LiveData<List<Category>> mCategoriesList;

  public CategoryViewModel() {
    mDataRepository = DataRepository.getInstance();
    mCategoriesMap = new HashMap<>();
  }

  public long insertCategoryGroup(CategoryGroup categoryGroup) {
    return mDataRepository.insertCategoryGroup(categoryGroup);
  }

  public LiveData<CategoryGroup> getCategoryGroup(long categoryGroupId) {
    return mDataRepository.getCategoryGroup(categoryGroupId);
  }

  public LiveData<List<CategoryGroup>> getCategoryGroups() {
    if (mCategoryGroups == null) {
      mCategoryGroups = mDataRepository.getCategoryGroups();
    }

    return mCategoryGroups;
  }

  public void deleteCategoryGroup(CategoryGroup categoryGroup) {
    mDataRepository.deleteCategoryGroup(categoryGroup);
  }

  public long insertCategory(Category category) {
    return mDataRepository.insertCategory(category);
  }

  public long[] insertCategories(List<Category> categories) {
    return mDataRepository.insertCategories(categories);
  }

  public LiveData<Category> getCategory(long categoryId) {
    return mDataRepository.getCategory(categoryId);
  }

  public LiveData<List<Category>> getCategories(long categoryGroupId) {
    if (mCategoriesMap.containsKey(categoryGroupId)) {
      return mCategoriesMap.get(categoryGroupId);
    }

    LiveData<List<Category>> categories = mDataRepository.getCategories(categoryGroupId);
    mCategoriesMap.put(categoryGroupId, categories);
    return categories;
  }

  public LiveData<List<Category>> getCategories() {
    if (mCategoriesList == null) {
      mCategoriesList = mDataRepository.getCategories();
    }

    return mCategoriesList;
  }

  public void deleteCategory(Category category) {
    mDataRepository.deleteCategory(category);
  }
}
