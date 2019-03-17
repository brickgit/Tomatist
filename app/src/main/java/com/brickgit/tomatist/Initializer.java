package com.brickgit.tomatist;

import com.brickgit.tomatist.data.database.Category;
import com.brickgit.tomatist.data.database.CategoryGroup;
import com.brickgit.tomatist.data.preferences.TomatistPreferences;
import com.brickgit.tomatist.data.viewmodel.InitializerViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

/** Created by Daniel Lin on 2019/3/16. */
public class Initializer {

  public static void firstLaunchSetup(FragmentActivity activity) {
    TomatistPreferences pref = TomatistPreferences.getInstance(activity);
    if (!pref.isFirstLaunched()) {
      return;
    }

    InitializerViewModel initializerViewModel =
        ViewModelProviders.of(activity).get(InitializerViewModel.class);

    pref.setIsFirstLaunched(false);
    Map<String, List<String>> categoryMap = new HashMap<>();
    String[] defaultCategories = activity.getResources().getStringArray(R.array.default_categories);
    for (String defaultCategory : defaultCategories) {
      String substrings[] = defaultCategory.split(" - ");
      if (substrings.length == 2) {
        String categoryGroup = substrings[0];
        String category = substrings[1];
        if (!categoryMap.containsKey(categoryGroup)) {
          categoryMap.put(categoryGroup, new ArrayList<>());
        }
        categoryMap.get(categoryGroup).add(category);
      }
    }
    for (String categoryGroupTitle : categoryMap.keySet()) {
      CategoryGroup newGroup = new CategoryGroup();
      if (pref.lastUsedCategoryGroupId().isEmpty()) {
        pref.setLastUsedCategoryGroupId(newGroup.getId());
      }
      newGroup.setTitle(categoryGroupTitle);
      initializerViewModel.insertCategoryGroup(newGroup);
      List<Category> newCategories = new ArrayList<>();
      List<String> categories = categoryMap.get(categoryGroupTitle);
      for (String categoryTitle : categories) {
        Category newCategory = new Category();
        if (pref.lastUsedCategoryId().isEmpty()) {
          pref.setLastUsedCategoryId(newCategory.getId());
        }
        newCategory.setGroupId(newGroup.getId());
        newCategory.setTitle(categoryTitle);
        newCategories.add(newCategory);
      }
      initializerViewModel.insertCategories(newCategories);
    }
  }
}
