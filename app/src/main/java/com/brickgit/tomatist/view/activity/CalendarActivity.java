package com.brickgit.tomatist.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Action;
import com.brickgit.tomatist.data.database.Category;
import com.brickgit.tomatist.data.database.CategoryGroup;
import com.brickgit.tomatist.data.preferences.TomatistPreferences;
import com.brickgit.tomatist.view.actionlist.ActionListAdapter;
import com.brickgit.tomatist.view.actionlist.ActionListTouchHelperCallback;
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
  private RecyclerView mActionList;
  private LinearLayoutManager mLayoutManager;
  private ActionListAdapter mActionListAdapter;

  private CalendarDay mSelectedDay = CalendarDay.today();

  private LiveData<List<Action>> mActions;
  private Observer<List<Action>> mObserver =
      (actions) -> {
        String date =
            String.format(
                Locale.getDefault(),
                "%d/%d/%d",
                mSelectedDay.getYear(),
                mSelectedDay.getMonth(),
                mSelectedDay.getDay());
        mActionListAdapter.updateActions(date, actions);
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
            case R.id.nav_unfinished_actions:
              gotoUnfinishedActionListActivity();
            default:
              break;
          }
          mDrawerLayout.closeDrawers();
          return true;
        });

    findViewById(R.id.add_action).setOnClickListener((view) -> gotoAddActionActivity(null));

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
          if (mActions != null) mActions.removeObserver(mObserver);
          mActions =
              mActionViewModel.getFinishedActions(day.getYear(), day.getMonth(), day.getDay());
          mActions.observe(CalendarActivity.this, mObserver);
        });

    mActionList = findViewById(R.id.action_list);
    mActionList.setHasFixedSize(true);

    mLayoutManager = new LinearLayoutManager(this);
    mActionList.setLayoutManager(mLayoutManager);

    ItemTouchHelper.Callback callback =
        new ActionListTouchHelperCallback((position) -> removeAction(position));
    ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
    touchHelper.attachToRecyclerView(mActionList);

    mActionListAdapter = new ActionListAdapter();
    mActionList.setAdapter(mActionListAdapter);
    mActionListAdapter.setOnActionClickListener(
        (action) -> gotoAddActionActivity(action.getId()));

    mActions =
        mActionViewModel.getFinishedActions(
            mSelectedDay.getYear(), mSelectedDay.getMonth(), mSelectedDay.getDay());
    mActions.observe(this, mObserver);

    mCategoryViewModel
        .getCategoryGroups()
        .observe(
            this,
            (categoryGroups) -> {
              Map<Long, CategoryGroup> map = new HashMap<>();
              for (CategoryGroup group : categoryGroups) {
                map.put(group.getId(), group);
              }
              mActionListAdapter.updateCategoryGroups(map);
            });
    mCategoryViewModel
        .getCategories()
        .observe(
            this,
            (categories) -> {
              Map<Long, Category> map = new HashMap<>();
              for (Category category : categories) {
                map.put(category.getId(), category);
              }
              mActionListAdapter.updateCategories(map);
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

  private void removeAction(int position) {
    if (mActions != null) {
      List<Action> actions = mActions.getValue();
      if (actions != null) {
        Action action = actions.get(position);
        if (action != null) {
          mActionViewModel.deleteAction(action);
          showActionDeletedConfirmation(action);
        }
      }
    }
  }

  private void showActionDeletedConfirmation(Action action) {
    Snackbar.make(mRootView, R.string.action_deleted, Snackbar.LENGTH_SHORT)
        .setAction(R.string.undo, (view) -> mActionViewModel.insertAction(action))
        .show();
  }

  private void gotoAddActionActivity(Long actionId) {
    Intent intent = new Intent(this, AddActionActivity.class);
    if (actionId != null) {
      intent.putExtra(AddActionActivity.SELECTED_ACTION_KEY, actionId);
    } else {
      intent.putExtra(AddActionActivity.SELECTED_YEAR_KEY, mSelectedDay.getYear());
      intent.putExtra(AddActionActivity.SELECTED_MONTH_KEY, mSelectedDay.getMonth() - 1);
      intent.putExtra(AddActionActivity.SELECTED_DAY_KEY, mSelectedDay.getDay());
    }
    startActivity(intent);
  }

  private void gotoUnfinishedActionListActivity() {
    Intent intent = new Intent(this, UnfinishedActionListActivity.class);
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
        newCategory.setGroupId(newGroupId);
        newCategory.setTitle(categoryTitle);
        newCategories.add(newCategory);
      }
      mCategoryViewModel.insertCategories(newCategories);
    }
  }
}
