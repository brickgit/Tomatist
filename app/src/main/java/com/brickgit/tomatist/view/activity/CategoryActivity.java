package com.brickgit.tomatist.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Category;
import com.brickgit.tomatist.data.database.CategoryGroup;
import com.brickgit.tomatist.view.categorylist.CategoryGroupListAdapter;
import com.brickgit.tomatist.view.categorylist.CategoryListAdapter;

import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryActivity extends BaseActivity {

  public static final int SELECT_CATEGORY = 1;
  public static final String SELECTED_CATEGORY_ID = "SELECTED_CATEGORY_ID";

  private RecyclerView mCategoryGroupsView;
  private CategoryGroupListAdapter mCategoryGroupAdapter;
  private RecyclerView mCategoriesView;
  private CategoryListAdapter mCategoryAdapter;

  private LiveData<List<CategoryGroup>> mCategoryGroups;
  private Observer<List<CategoryGroup>> mCategoryGroupsObserver =
      (categoryGroups) -> mCategoryGroupAdapter.updateCategoryGroups(categoryGroups);

  private LiveData<List<Category>> mCategories;
  private Observer<List<Category>> mCategoriesObserver =
      (categories) -> mCategoryAdapter.updateCategories(categories);

  public static void startForResult(Activity activity) {
    Intent intent = new Intent(activity, CategoryActivity.class);
    activity.startActivityForResult(intent, SELECT_CATEGORY);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_category);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle(R.string.category);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    mCategoryGroupsView = findViewById(R.id.category_groups);
    mCategoryGroupsView.setLayoutManager(new LinearLayoutManager(this));
    mCategoryGroupAdapter = new CategoryGroupListAdapter();
    mCategoryGroupAdapter.setOnCategoryGroupClickListener(
        (categoryGroup) -> {
          if (mCategories != null) {
            mCategories.removeObserver(mCategoriesObserver);
          }
          mCategories = mCategoryViewModel.getCategories(categoryGroup.getCategoryGroupId());
          mCategories.observe(CategoryActivity.this, mCategoriesObserver);
        });
    mCategoryGroupsView.setAdapter(mCategoryGroupAdapter);

    mCategoriesView = findViewById(R.id.categories);
    mCategoriesView.setLayoutManager(new LinearLayoutManager(this));
    mCategoryAdapter = new CategoryListAdapter();
    mCategoryAdapter.setOnCategoryClickListener(
        (category) -> {
          Intent intent = new Intent();
          intent.putExtra(SELECTED_CATEGORY_ID, category.getCategoryId());
          setResult(RESULT_OK, intent);
          finish();
        });
    mCategoriesView.setAdapter(mCategoryAdapter);

    mCategoryGroups = mCategoryViewModel.getCategoryGroups();
    mCategoryGroups.observe(this, mCategoryGroupsObserver);
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }
}
