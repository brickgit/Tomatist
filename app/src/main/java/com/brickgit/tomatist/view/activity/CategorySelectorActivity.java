package com.brickgit.tomatist.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Category;
import com.brickgit.tomatist.data.database.CategoryGroup;
import com.brickgit.tomatist.data.preferences.TomatistPreferences;
import com.brickgit.tomatist.data.viewmodel.CategorySelectorViewModel;
import com.brickgit.tomatist.view.categorylist.CategoryGroupListAdapter;
import com.brickgit.tomatist.view.categorylist.CategoryGroupListTouchHelperCallback;
import com.brickgit.tomatist.view.categorylist.CategoryListAdapter;
import com.brickgit.tomatist.view.categorylist.CategoryListTouchHelperCallback;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategorySelectorActivity extends BaseActivity {

  public static final String SELECTED_CATEGORY_ID = "SELECTED_CATEGORY_ID";

  protected CategorySelectorViewModel mCategorySelectorViewModel;

  private View mRootView;
  private RecyclerView mCategoryGroupsView;
  private CategoryGroupListAdapter mCategoryGroupAdapter;
  private RecyclerView mCategoriesView;
  private CategoryListAdapter mCategoryAdapter;

  private List<CategoryGroup> mCategoryGroupList = new ArrayList<>();
  private List<Category> mCategoryList = new ArrayList<>();

  public static void startForResult(Activity activity, int requestCoe) {
    Intent intent = new Intent(activity, CategorySelectorActivity.class);
    activity.startActivityForResult(intent, requestCoe);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_category);

    mRootView = findViewById(R.id.root_view);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle(R.string.category);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    findViewById(R.id.add_category_group)
        .setOnClickListener((view) -> showAddCategoryGroupDialog());
    findViewById(R.id.add_category).setOnClickListener((view) -> showAddCategoryDialog());

    mCategoryGroupsView = findViewById(R.id.category_groups);
    mCategoryGroupsView.setLayoutManager(new LinearLayoutManager(this));
    mCategoryGroupAdapter = new CategoryGroupListAdapter();
    mCategoryGroupAdapter.setOnCategoryGroupClickListener(
        (categoryGroup) -> {
          TomatistPreferences.getInstance(CategorySelectorActivity.this)
              .setLastUsedCategoryGroupId(categoryGroup.getId());
          selectCategoryGroup(categoryGroup.getId());
        });
    mCategoryGroupsView.setAdapter(mCategoryGroupAdapter);
    ItemTouchHelper.Callback categoryGroupsViewCallback =
        new CategoryGroupListTouchHelperCallback((position) -> removeCategoryGroup(position));
    ItemTouchHelper categoryGroupsViewTouchHelper = new ItemTouchHelper(categoryGroupsViewCallback);
    categoryGroupsViewTouchHelper.attachToRecyclerView(mCategoryGroupsView);

    mCategoriesView = findViewById(R.id.categories);
    mCategoriesView.setLayoutManager(new LinearLayoutManager(this));
    mCategoryAdapter = new CategoryListAdapter();
    mCategoryAdapter.setOnCategoryClickListener(
        (category) -> {
          TomatistPreferences.getInstance(CategorySelectorActivity.this)
              .setLastUsedCategoryId(category.getId());
          Intent intent = new Intent();
          intent.putExtra(SELECTED_CATEGORY_ID, category.getId());
          setResult(RESULT_OK, intent);
          finish();
        });
    mCategoriesView.setAdapter(mCategoryAdapter);
    ItemTouchHelper.Callback categoriesViewCallback =
        new CategoryListTouchHelperCallback((position) -> removeCategory(position));
    ItemTouchHelper categoriesViewTouchHelper = new ItemTouchHelper(categoriesViewCallback);
    categoriesViewTouchHelper.attachToRecyclerView(mCategoriesView);

    mCategorySelectorViewModel = ViewModelProviders.of(this).get(CategorySelectorViewModel.class);
    mCategorySelectorViewModel
        .getCategoryGroupList()
        .observe(
            this,
            (categoryGroups) -> {
              mCategoryGroupList.clear();
              mCategoryGroupList.addAll(categoryGroups);
              mCategoryGroupAdapter.updateCategoryGroups(mCategoryGroupList);
            });
    mCategorySelectorViewModel
        .getSelectedCategoryList()
        .observe(
            this,
            (categories) -> {
              mCategoryList.clear();
              mCategoryList.addAll(categories);
              mCategoryAdapter.updateCategories(
                  mCategorySelectorViewModel.getSelectedCategoryGroupId(), mCategoryList);
            });

    selectCategoryGroup(TomatistPreferences.getInstance(this).lastUsedCategoryGroupId());
  }

  @Override
  public boolean onSupportNavigateUp() {
    setResult(RESULT_CANCELED);
    onBackPressed();
    return true;
  }

  private void selectCategoryGroup(long categoryGroupId) {
    mCategorySelectorViewModel.selectCategoryGroup(categoryGroupId);
  }

  private void showAddCategoryGroupDialog() {
    final EditText newCategoryGroupTitleView = new EditText(this);

    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    dialog.setTitle(getString(R.string.add_category_group));
    dialog.setView(newCategoryGroupTitleView);

    dialog.setPositiveButton(
        getString(R.string.action_add),
        (view, which) -> {
          String newCategoryGroupTitle = newCategoryGroupTitleView.getText().toString().trim();
          if (!newCategoryGroupTitle.isEmpty()) {
            CategoryGroup newCategoryGroup = new CategoryGroup();
            newCategoryGroup.setTitle(newCategoryGroupTitle);
            long newCategoryGroupId =
                mCategorySelectorViewModel.insertCategoryGroup(newCategoryGroup);
            selectCategoryGroup(newCategoryGroupId);
          }
        });

    dialog.create().show();
  }

  private void removeCategoryGroup(int position) {
    CategoryGroup categoryGroup = mCategoryGroupList.get(position);
    if (categoryGroup != null) {
      mCategorySelectorViewModel.deleteCategoryGroup(categoryGroup);
      showCategoryGroupDeletedConfirmation(categoryGroup);
    }
  }

  private void showCategoryGroupDeletedConfirmation(CategoryGroup categoryGroup) {
    Snackbar.make(mRootView, R.string.category_group_deleted, Snackbar.LENGTH_SHORT)
        .setAction(
            R.string.undo, (view) -> mCategorySelectorViewModel.insertCategoryGroup(categoryGroup))
        .show();
  }

  private void showAddCategoryDialog() {
    final EditText newCategoryTitleView = new EditText(this);

    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    dialog.setTitle(getString(R.string.add_category));
    dialog.setView(newCategoryTitleView);

    dialog.setPositiveButton(
        getString(R.string.action_add),
        (view, which) -> {
          String newCategoryTitle = newCategoryTitleView.getText().toString().trim();
          if (!newCategoryTitle.isEmpty()) {
            Category newCategory = new Category();
            newCategory.setTitle(newCategoryTitle);
            newCategory.setGroupId(mCategorySelectorViewModel.getSelectedCategoryGroupId());
            mCategorySelectorViewModel.insertCategory(newCategory);
          }
        });

    dialog.create().show();
  }

  private void removeCategory(int position) {
    Category category = mCategoryList.get(position);
    if (category != null) {
      mCategorySelectorViewModel.deleteCategory(category);
      showCategoryDeletedConfirmation(category);
    }
  }

  private void showCategoryDeletedConfirmation(Category category) {
    Snackbar.make(mRootView, R.string.category_deleted, Snackbar.LENGTH_SHORT)
        .setAction(R.string.undo, (view) -> mCategorySelectorViewModel.insertCategory(category))
        .show();
  }
}
