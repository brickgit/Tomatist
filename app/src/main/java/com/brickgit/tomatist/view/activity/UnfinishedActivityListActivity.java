package com.brickgit.tomatist.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Activity;
import com.brickgit.tomatist.data.database.Category;
import com.brickgit.tomatist.data.database.CategoryGroup;
import com.brickgit.tomatist.view.activitylist.ActivityListAdapter;
import com.brickgit.tomatist.view.activitylist.ActivityListTouchHelperCallback;
import com.google.android.material.snackbar.Snackbar;
import com.prolificinteractive.materialcalendarview.CalendarDay;

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

  private View mRootView;
  private RecyclerView mActivityList;
  private LinearLayoutManager mLayoutManager;
  private ActivityListAdapter mActivityListAdapter;

  private LiveData<List<Activity>> mActivities;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_unfinished_activity_list);

    mRootView = findViewById(R.id.root_view);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle(R.string.unfinished_activities);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    findViewById(R.id.add_activity).setOnClickListener((view) -> gotoAddActivityActivity(null));

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
    mActivities.observe(
        this, (activities) -> mActivityListAdapter.updateActivities("", activities));

    mCategoryViewModel
        .getCategoryGroups()
        .observe(
            this,
            (categoryGroups) -> {
              Map<Long, CategoryGroup> map = new HashMap<>();
              for (CategoryGroup group : categoryGroups) {
                map.put(group.getCategoryGroupId(), group);
              }
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
              mActivityListAdapter.updateCategories(map);
            });
  }

  @Override
  public boolean onSupportNavigateUp() {
    setResult(RESULT_CANCELED);
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
    if (mActivities != null) {
      List<Activity> activities = mActivities.getValue();
      if (activities != null) {
        Activity activity = activities.get(position);
        if (activity != null) {
          mActivityViewModel.deleteActivity(activity);
          showActivityDeletedConfirmation(activity);
        }
      }
    }
  }

  private void showActivityDeletedConfirmation(Activity activity) {
    Snackbar.make(mRootView, R.string.activity_deleted, Snackbar.LENGTH_SHORT)
        .setAction(R.string.undo, (view) -> mActivityViewModel.insertActivity(activity))
        .show();
  }
}
