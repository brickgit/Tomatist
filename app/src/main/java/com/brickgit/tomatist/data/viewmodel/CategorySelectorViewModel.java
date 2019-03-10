package com.brickgit.tomatist.data.viewmodel;

import com.brickgit.tomatist.data.DataRepository;
import com.brickgit.tomatist.data.database.Category;
import com.brickgit.tomatist.data.database.CategoryGroup;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

/** Created by Daniel Lin on 2019/3/9. */
public class CategorySelectorViewModel extends ViewModel {

  private MutableLiveData<Long> mSelectedCategoryGroupId = new MutableLiveData<>();

  private DataRepository mDataRepository;
  private LiveData<List<CategoryGroup>> mCategoryGroupList;
  private LiveData<List<Category>> mSelectedCategoryList =
      Transformations.switchMap(
          mSelectedCategoryGroupId, (selectedId) -> mDataRepository.getCategories(selectedId));

  public CategorySelectorViewModel() {
    mDataRepository = DataRepository.getInstance();
  }

  public long insertCategoryGroup(CategoryGroup categoryGroup) {
    return mDataRepository.insertCategoryGroup(categoryGroup);
  }

  public void deleteCategoryGroup(CategoryGroup categoryGroup) {
    mDataRepository.deleteCategoryGroup(categoryGroup);
  }

  public long insertCategory(Category category) {
    return mDataRepository.insertCategory(category);
  }

  public void deleteCategory(Category category) {
    mDataRepository.deleteCategory(category);
  }

  public LiveData<List<CategoryGroup>> getCategoryGroupList() {
    if (mCategoryGroupList == null) {
      mCategoryGroupList = mDataRepository.getCategoryGroups();
    }
    return mCategoryGroupList;
  }

  public LiveData<List<Category>> getSelectedCategoryList() {
    return mSelectedCategoryList;
  }

  public void selectCategoryGroup(Long selectedCategoryGroupId) {
    mSelectedCategoryGroupId.setValue(selectedCategoryGroupId);
  }

  public Long getSelectedCategoryGroupId() {
    return mSelectedCategoryGroupId.getValue();
  }
}
