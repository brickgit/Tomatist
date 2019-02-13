package com.brickgit.tomatist.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Category;
import com.brickgit.tomatist.data.database.CategoryGroup;
import com.brickgit.tomatist.data.database.Task;
import com.brickgit.tomatist.data.preferences.TomatistPreferences;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

/** Created by Daniel Lin on 2019/2/13. */
public class AddTaskActivity extends BaseActivity {

  private static final int REQUEST_CODE_SELECT_CATEGORY = 0;

  private TextInputEditText mNewTaskName;
  private TextView mCategoryView;

  private LiveData<CategoryGroup> mCategoryGroup;
  private Observer<CategoryGroup> mCategoryGroupObserver = (categoryGroup) -> updateCategoryView();

  private LiveData<Category> mCategory;
  private Observer<Category> mCategoryObserver =
      (category) -> {
        if (category != null) {
          if (mCategoryGroup != null) {
            mCategoryGroup.removeObserver(mCategoryGroupObserver);
          }
          Long categoryGroupId = category.getCategoryGroupId();
          if (categoryGroupId != null) {
            mCategoryGroup = mCategoryViewModel.getCategoryGroup(category.getCategoryGroupId());
            mCategoryGroup.observe(this, mCategoryGroupObserver);
          } else {
            mCategoryGroup = null;
          }
        }
        updateCategoryView();
      };

  public static void start(Activity activity) {
    Intent intent = new Intent(activity, AddTaskActivity.class);
    activity.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_task);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle(R.string.add_task);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    mNewTaskName = findViewById(R.id.new_task_name);

    mCategoryView = findViewById(R.id.category);
    mCategoryView.setOnClickListener(
        (v) -> CategoryActivity.startForResult(this, REQUEST_CODE_SELECT_CATEGORY));

    mCategory =
        mCategoryViewModel.getCategory(TomatistPreferences.getInstance(this).lastUsedCategoryId());
    mCategory.observe(this, mCategoryObserver);
  }

  private void addTask() {
    String newTaskName = mNewTaskName.getText().toString().trim();
    if (newTaskName.isEmpty()) {
      mNewTaskName.setError(getString(R.string.error_name_is_required));
      return;
    }

    Long categoryId = null;
    if (mCategory != null) {
      Category category = mCategory.getValue();
      if (category != null) {
        categoryId = category.getCategoryId();
      }
    }

    Task task = new Task();
    task.setTitle(newTaskName);
    task.setCategoryId(categoryId);
    mProjectViewModel.insertTask(task);

    finish();
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_add_task, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_add:
        addTask();
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void updateCategoryView() {
    StringBuilder sb = new StringBuilder();
    if (mCategory != null) {
      Category category = mCategory.getValue();
      if (category != null) {
        sb.append(category.getTitle());

        if (mCategoryGroup != null) {
          CategoryGroup group = mCategoryGroup.getValue();
          if (group != null && category.getCategoryGroupId().equals(group.getCategoryGroupId())) {
            sb.insert(0, group.getTitle() + " - ");
          }
        }
      }
    }
    String strCategory = sb.toString().trim();
    mCategoryView.setText(strCategory.isEmpty() ? getString(R.string.uncategorized) : strCategory);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE_SELECT_CATEGORY) {
      if (resultCode == RESULT_OK) {
        long selectedCategoryId =
            data.getLongExtra(
                CategoryActivity.SELECTED_CATEGORY_ID,
                CategoryActivity.INVALID_SELECTED_CATEGORY_ID);
        if (selectedCategoryId != CategoryActivity.INVALID_SELECTED_CATEGORY_ID) {
          if (mCategory != null) mCategory.removeObserver(mCategoryObserver);
          mCategory = mCategoryViewModel.getCategory(selectedCategoryId);
          mCategory.observe(this, mCategoryObserver);
        }
      }
    }
  }
}
