package com.brickgit.tomatist.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Activity;
import com.brickgit.tomatist.data.database.Category;
import com.brickgit.tomatist.data.database.CategoryGroup;
import com.brickgit.tomatist.view.activitylist.ActivityListAdapter;
import com.brickgit.tomatist.view.activitylist.ActivityListTouchHelperCallback;
import com.google.android.material.snackbar.Snackbar;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/** Created by Daniel Lin on 2019/2/26. */
public class UnfinishedActivityListActivity extends BaseActivity {

  private static final int REQUEST_CODE_SELECT_CATEGORY = 0;

  private View mRootView;
  private Button mCategoryButton;
  private RecyclerView mActivityList;
  private LinearLayoutManager mLayoutManager;
  private ActivityListAdapter mActivityListAdapter;

  private Map<Long, CategoryGroup> mCategoryGroups = new HashMap<>();
  private Map<Long, Category> mCategories = new HashMap<>();
  private LiveData<List<Activity>> mActivities;
  private Long mSelectedCategoryId = null;
  private List<Activity> mSelectedActivities = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_unfinished_activity_list);

    mRootView = findViewById(R.id.root_view);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle(R.string.unfinished_actions);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    findViewById(R.id.add_activity).setOnClickListener((view) -> gotoAddActivityActivity(null));

    mCategoryButton = findViewById(R.id.category_button);
    mCategoryButton.setOnClickListener(
        (view) -> CategoryActivity.startForResult(this, REQUEST_CODE_SELECT_CATEGORY));
    findViewById(R.id.clear)
        .setOnClickListener(
            (view) -> {
              mSelectedCategoryId = null;
              updateViews();
            });

    mActivityList = findViewById(R.id.unfinished_activity_list);
    mActivityList.setHasFixedSize(true);

    mLayoutManager = new LinearLayoutManager(this);
    mActivityList.setLayoutManager(mLayoutManager);

    ItemTouchHelper.Callback callback =
        new ActivityListTouchHelperCallback((position) -> removeActivity(position));
    ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
    touchHelper.attachToRecyclerView(mActivityList);

    mActivityListAdapter = new ActivityListAdapter();
    mActivityList.setAdapter(mActivityListAdapter);
    mActivityListAdapter.setOnActivityClickListener(
        (activity) -> gotoAddActivityActivity(activity.getActivityId()));

    mActivities = mActivityViewModel.getUnfinishedActivities();
    mActivities.observe(this, (activities) -> updateViews());

    mCategoryViewModel
        .getCategoryGroups()
        .observe(
            this,
            (categoryGroups) -> {
              Map<Long, CategoryGroup> map = new HashMap<>();
              for (CategoryGroup group : categoryGroups) {
                map.put(group.getCategoryGroupId(), group);
              }
              mCategoryGroups.clear();
              mCategoryGroups.putAll(map);
              mActivityListAdapter.updateCategoryGroups(map);
            });
    mCategoryViewModel
        .getCategories()
        .observe(
            this,
            (categories) -> {
              Map<Long, Category> map = new HashMap<>();
              for (Category category : categories) {
                map.put(category.getCategoryId(), category);
              }
              mCategories.clear();
              mCategories.putAll(map);
              mActivityListAdapter.updateCategories(map);
            });
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  private void gotoAddActivityActivity(Long activityId) {
    Intent intent = new Intent(this, AddActivityActivity.class);
    if (activityId != null) {
      intent.putExtra(AddActivityActivity.SELECTED_ACTIVITY_KEY, activityId);
    } else {
      CalendarDay today = CalendarDay.today();
      intent.putExtra(AddActivityActivity.SELECTED_YEAR_KEY, today.getYear());
      intent.putExtra(AddActivityActivity.SELECTED_MONTH_KEY, today.getMonth() - 1);
      intent.putExtra(AddActivityActivity.SELECTED_DAY_KEY, today.getDay());
    }
    startActivity(intent);
  }

  private void removeActivity(int position) {
    Activity activity = mSelectedActivities.get(position);
    if (activity != null) {
      mActivityViewModel.deleteActivity(activity);
      showActivityDeletedConfirmation(activity);
    }
  }

  private void showActivityDeletedConfirmation(Activity activity) {
    Snackbar.make(mRootView, R.string.action_deleted, Snackbar.LENGTH_SHORT)
        .setAction(R.string.undo, (view) -> mActivityViewModel.insertActivity(activity))
        .show();
  }

  private void updateViews() {
    String tag = getString(R.string.all);
    if (mSelectedCategoryId == null) {
      mCategoryButton.setText(getString(R.string.all));
      mSelectedActivities.clear();
      if (mActivities != null && mActivities.getValue() != null) {
        mSelectedActivities.addAll(mActivities.getValue());
      }
    } else {
      Category category = mCategories.get(mSelectedCategoryId);
      if (category != null) {
        tag = String.valueOf(category.getCategoryId());
        StringBuilder sb = new StringBuilder();
        sb.append(category.getTitle());
        CategoryGroup categoryGroup = mCategoryGroups.get(category.getCategoryGroupId());
        if (categoryGroup != null) {
          sb.insert(0, categoryGroup.getTitle() + " - ");
        }
        mCategoryButton.setText(sb.toString());

        mSelectedActivities.clear();
        if (mActivities != null && mActivities.getValue() != null) {
          for (Activity activity : mActivities.getValue()) {
            if (activity.getCategoryId().equals(category.getCategoryId())) {
              mSelectedActivities.add(activity);
            }
          }
        }
      } else {
        mCategoryButton.setText(getString(R.string.all));
        mSelectedActivities.clear();
        if (mActivities != null && mActivities.getValue() != null) {
          mSelectedActivities.addAll(mActivities.getValue());
        }
      }
    }
    mActivityListAdapter.updateActivities(tag, mSelectedActivities);
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
        mSelectedCategoryId =
            selectedCategoryId != CategoryActivity.INVALID_SELECTED_CATEGORY_ID
                ? selectedCategoryId
                : null;
        updateViews();
      }
    }
  }
}
