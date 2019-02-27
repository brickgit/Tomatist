package com.brickgit.tomatist.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Activity;
import com.brickgit.tomatist.data.database.Category;
import com.brickgit.tomatist.data.database.CategoryGroup;
import com.brickgit.tomatist.data.preferences.TomatistPreferences;
import com.brickgit.tomatist.view.activitylist.ActivityListAdapter;
import com.brickgit.tomatist.view.activitylist.ActivityListTouchHelperCallback;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarActivity extends BaseActivity {

  private View mRootView;
  private DrawerLayout mDrawerLayout;
  private MaterialCalendarView mCalendarView;
  private RecyclerView mActivityList;
  private LinearLayoutManager mLayoutManager;
  private ActivityListAdapter mActivityListAdapter;

  private CalendarDay mSelectedDay = CalendarDay.today();

  private LiveData<List<Activity>> mActivities;
  private Observer<List<Activity>> mObserver =
      (activities) -> {
        String date =
            String.format(
                Locale.getDefault(),
                "%d/%d/%d",
                mSelectedDay.getYear(),
                mSelectedDay.getMonth(),
                mSelectedDay.getDay());
        mActivityListAdapter.updateActivities(date, activities);
      };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_calendar);

    mRootView = findViewById(R.id.root_view);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

    mDrawerLayout = findViewById(R.id.drawer_layout);
    NavigationView navigationView = findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(
        (menuItem) -> {
          switch (menuItem.getItemId()) {
            default:
              break;
          }
          mDrawerLayout.closeDrawers();
          return true;
        });

    findViewById(R.id.add_activity).setOnClickListener((view) -> gotoAddActivityActivity(null));

    mCalendarView = findViewById(R.id.calendar_view);
    mCalendarView.addDecorator(
        new DayViewDecorator() {
          @Override
          public boolean shouldDecorate(CalendarDay day) {
            return day.equals(CalendarDay.today());
          }

          @Override
          public void decorate(DayViewFacade view) {
            view.addSpan(
                new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark)));
          }
        });
    mCalendarView.setDateSelected(mSelectedDay, true);
    mCalendarView.setOnDateChangedListener(
        (view, day, b) -> {
          mSelectedDay = day;
          if (mActivities != null) mActivities.removeObserver(mObserver);
          mActivities =
              mActivityViewModel.getActivities(day.getYear(), day.getMonth(), day.getDay());
          mActivities.observe(CalendarActivity.this, mObserver);
        });

    mActivityList = findViewById(R.id.activity_list);
    mActivityList.setHasFixedSize(true);

    mLayoutManager = new LinearLayoutManager(this);
    mActivityList.setLayoutManager(mLayoutManager);

    ItemTouchHelper.Callback callback =
        new ActivityListTouchHelperCallback(
            (position) -> {
              removeActivity(position);
            });
    ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
    touchHelper.attachToRecyclerView(mActivityList);

    mActivityListAdapter = new ActivityListAdapter();
    mActivityList.setAdapter(mActivityListAdapter);
    mActivityListAdapter.setOnActivityClickListener(
        (activity) -> gotoAddActivityActivity(activity.getActivityId()));

    mActivities =
        mActivityViewModel.getActivities(
            mSelectedDay.getYear(), mSelectedDay.getMonth(), mSelectedDay.getDay());
    mActivities.observe(this, mObserver);

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

    firstLaunchSetup();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        mDrawerLayout.openDrawer(GravityCompat.START);
        return true;
    }
    return super.onOptionsItemSelected(item);
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

  private void gotoAddActivityActivity(Long activityId) {
    Intent intent = new Intent(this, AddActivityActivity.class);
    if (activityId != null) {
      intent.putExtra(AddActivityActivity.SELECTED_ACTIVITY_KEY, activityId);
    } else {
      intent.putExtra(AddActivityActivity.SELECTED_YEAR_KEY, mSelectedDay.getYear());
      intent.putExtra(AddActivityActivity.SELECTED_MONTH_KEY, mSelectedDay.getMonth() - 1);
      intent.putExtra(AddActivityActivity.SELECTED_DAY_KEY, mSelectedDay.getDay());
    }
    startActivity(intent);
  }

  private void firstLaunchSetup() {
    TomatistPreferences pref = TomatistPreferences.getInstance(this);
    if (pref.isFirstLaunched()) {
      pref.setIsFirstLaunched(false);
      initDefaultCategories();
    }
  }

  private void initDefaultCategories() {
    Map<String, List<String>> categoryMap = new HashMap<>();
    String[] defaultCategories = getResources().getStringArray(R.array.default_categories);
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
      newGroup.setTitle(categoryGroupTitle);
      long newGroupId = mCategoryViewModel.insertCategoryGroup(newGroup);

      List<Category> newCategories = new ArrayList<>();
      List<String> categories = categoryMap.get(categoryGroupTitle);
      for (String categoryTitle : categories) {
        Category newCategory = new Category();
        newCategory.setCategoryGroupId(newGroupId);
        newCategory.setTitle(categoryTitle);
        newCategories.add(newCategory);
      }
      mCategoryViewModel.insertCategories(newCategories);
    }
  }
}
