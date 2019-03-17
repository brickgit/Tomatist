package com.brickgit.tomatist.data.viewmodel;

import com.brickgit.tomatist.data.database.Category;
import com.brickgit.tomatist.data.database.CategoryGroup;

import java.util.List;

/** Created by Daniel Lin on 2019/3/16. */
public class InitializerViewModel extends BaseViewModel {

  public void insertCategoryGroup(CategoryGroup categoryGroup) {
    mDataRepository.insertCategoryGroup(categoryGroup);
  }

  public void insertCategories(List<Category> categories) {
    mDataRepository.insertCategories(categories);
  }
}
