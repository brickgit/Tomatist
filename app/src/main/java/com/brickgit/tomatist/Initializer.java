package com.brickgit.tomatist;

import android.content.res.Resources;

import com.brickgit.tomatist.data.database.Category;
import com.brickgit.tomatist.data.database.CategoryGroup;
import com.brickgit.tomatist.data.preferences.TomatistPreferences;
import com.brickgit.tomatist.data.viewmodel.InitializerViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

/** Created by Daniel Lin on 2019/3/16. */
public class Initializer {

  public static void firstLaunchSetup(FragmentActivity activity) {
    TomatistPreferences pref = TomatistPreferences.getInstance(activity);
    if (!pref.isFirstLaunched()) {
      return;
    }
    pref.setIsFirstLaunched(false);

    InitializerViewModel initializerViewModel =
        ViewModelProviders.of(activity).get(InitializerViewModel.class);

    Resources res = activity.getResources();

    CategoryGroup categoryGroup = new CategoryGroup();
    categoryGroup.setId(res.getString(R.string.default_category_group_id_routine));
    categoryGroup.setTitle(res.getString(R.string.default_category_group_title_routine));
    initializerViewModel.insertCategoryGroup(categoryGroup);
    if (pref.lastUsedCategoryGroupId().isEmpty()) {
      pref.setLastUsedCategoryGroupId(categoryGroup.getId());
    }

    List<Category> categoryList = new ArrayList<>();
    String[] categoryIds = res.getStringArray(R.array.default_category_ids);
    String[] categoryTitles = res.getStringArray(R.array.default_category_titles);
    for (int i = 0; i < categoryIds.length; i++) {
      String categoryId = categoryIds[i];
      String categoryTitle = categoryTitles[i];
      Category category = new Category();
      category.setId(categoryId);
      category.setTitle(categoryTitle);
      category.setGroupId(categoryGroup.getId());
      categoryList.add(category);
      if (pref.lastUsedCategoryId().isEmpty()) {
        pref.setLastUsedCategoryId(category.getId());
      }
    }
    initializerViewModel.insertCategories(categoryList);
  }
}
