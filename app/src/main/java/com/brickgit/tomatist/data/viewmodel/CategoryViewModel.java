package com.brickgit.tomatist.data.viewmodel;

import com.brickgit.tomatist.data.DataRepository;
import com.brickgit.tomatist.data.database.Category;
import com.brickgit.tomatist.data.database.CategoryGroup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class CategoryViewModel extends ViewModel {

  private DataRepository mDataRepository;

  public CategoryViewModel() {
    mDataRepository = DataRepository.getInstance();
  }

  public LiveData<CategoryGroup> getCategoryGroup(long categoryGroupId) {
    return mDataRepository.getCategoryGroup(categoryGroupId);
  }

  public LiveData<Category> getCategory(long categoryId) {
    return mDataRepository.getCategory(categoryId);
  }
}
