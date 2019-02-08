package com.brickgit.tomatist.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Activity;
import com.brickgit.tomatist.data.database.Category;
import com.brickgit.tomatist.data.database.CategoryGroup;
import com.brickgit.tomatist.data.preferences.TomatistPreferences;
import com.brickgit.tomatist.view.activitylist.ActivityListAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarActivity extends BaseActivity {

  private MaterialCalendarView mCalendarView;
  private RecyclerView mActivityList;
  private LinearLayoutManager mLayoutManager;
  private ActivityListAdapter mActivityListAdapter;

  private LiveData<List<Activity>> mActivities;
  private Observer<List<Activity>> mObserver =
      (activities) -> mActivityListAdapter.updateActivities(activities);

  private CalendarDay mSelectedDay = CalendarDay.today();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_calendar);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

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

    mActivityListAdapter = new ActivityListAdapter();
    mActivityList.setAdapter(mActivityListAdapter);
    mActivityListAdapter.setOnActivityClickListener(
        new ActivityListAdapter.OnActivityClickListener() {
          @Override
          public void onAddActivityClick() {
            gotoAddActivityActivity(null);
          }

          @Override
          public void onActivityClick(Activity activity) {
            gotoAddActivityActivity(activity.getActivityId());
          }
        });

    CalendarDay today = CalendarDay.today();
    mActivities =
        mActivityViewModel.getActivities(today.getYear(), today.getMonth(), today.getDay());
    mActivities.observe(this, mObserver);

    firstLaunchSetup();
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
