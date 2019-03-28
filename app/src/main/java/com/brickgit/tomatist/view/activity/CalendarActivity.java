package com.brickgit.tomatist.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.brickgit.tomatist.Initializer;
import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Action;
import com.brickgit.tomatist.data.viewmodel.CalendarActivityViewModel;
import com.brickgit.tomatist.view.actionlist.ActionListAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.shrikanthravi.collapsiblecalendarview.data.Day;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarActivity extends BaseActivity {

  private CalendarActivityViewModel mCalendarActivityViewModel;

  private View mRootView;
  private DrawerLayout mDrawerLayout;
  private CollapsibleCalendar mCalendarView;
  private RecyclerView mActionList;
  private LinearLayoutManager mLayoutManager;
  private ActionListAdapter mActionListAdapter;

  private List<Action> mActions = new ArrayList<>();

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
              break;
            case R.id.nav_report:
              ReportActivity.start(this);
              break;
            default:
              break;
          }
          mDrawerLayout.closeDrawers();
          return true;
        });

    findViewById(R.id.add_action).setOnClickListener((view) -> gotoAddActionActivity(null, false));

    mCalendarView = findViewById(R.id.calendar_view);
    mCalendarView.setCalendarListener(
        new CollapsibleCalendar.CalendarListener() {
          @Override
          public void onDaySelect() {
            Day day = mCalendarView.getSelectedDay();
            mCalendarActivityViewModel.selectDate(day.getYear(), day.getMonth(), day.getDay());
          }

          @Override
          public void onItemClick(View view) {}

          @Override
          public void onDataUpdate() {}

          @Override
          public void onMonthChange() {}

          @Override
          public void onWeekChange(int i) {}
        });

    mActionList = findViewById(R.id.action_list);
    mActionList.setHasFixedSize(true);

    mLayoutManager = new LinearLayoutManager(this);
    mActionList.setLayoutManager(mLayoutManager);
    mActionList.addOnScrollListener(
        new RecyclerView.OnScrollListener() {
          private int oldState = RecyclerView.SCROLL_STATE_IDLE;

          @Override
          public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            if (oldState == RecyclerView.SCROLL_STATE_IDLE
                && newState == RecyclerView.SCROLL_STATE_DRAGGING) {
              ((ActionListAdapter) recyclerView.getAdapter()).collapse();
            }
            oldState = newState;
            super.onScrollStateChanged(recyclerView, newState);
          }
        });

    mActionListAdapter = new ActionListAdapter();
    mActionList.setAdapter(mActionListAdapter);
    mActionListAdapter.setOnActionClickListener(
        new ActionListAdapter.OnActionClickListener() {
          @Override
          public void onItemExpand(View view) {
            int position = mActionList.getChildAdapterPosition(view);
            if (position != RecyclerView.NO_POSITION) {
              mActionList.smoothScrollToPosition(position);
            }
          }

          @Override
          public void onEditClick(Action action) {
            gotoAddActionActivity(action.getId(), false);
          }

          @Override
          public void onCopyClick(Action action) {
            gotoAddActionActivity(action.getId(), true);
          }

          @Override
          public void onDeleteClick(Action action) {
            removeAction(action);
          }
        });

    mCalendarActivityViewModel = ViewModelProviders.of(this).get(CalendarActivityViewModel.class);
    mCalendarActivityViewModel
        .getTagMap()
        .observe(this, (tags) -> mActionListAdapter.updateTags(tags));
    mCalendarActivityViewModel
        .getFinishedActionList()
        .observe(
            this,
            (actions) -> {
              mActions.clear();
              mActions.addAll(actions);
              mActionListAdapter.updateActions(
                  mCalendarActivityViewModel.getSelectedDate(), mActions);
            });
    Calendar today = Calendar.getInstance();
    Day day =
        new Day(
            today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
    mCalendarView.select(day);

    Initializer.firstLaunchSetup(this);
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

  private void removeAction(Action action) {
    if (action != null) {
      mCalendarActivityViewModel.deleteAction(action);
      showActionDeletedConfirmation(action);
    }
  }

  private void showActionDeletedConfirmation(Action action) {
    Snackbar.make(mRootView, R.string.action_deleted, Snackbar.LENGTH_SHORT)
        .setAction(R.string.undo, (view) -> mCalendarActivityViewModel.insertAction(action))
        .show();
  }

  private void gotoAddActionActivity(String actionId, boolean isCopyingAction) {
    Intent intent = new Intent(this, AddActionActivity.class);
    if (actionId != null) {
      intent.putExtra(AddActionActivity.SELECTED_ACTION_ID_KEY, actionId);
      intent.putExtra(AddActionActivity.IS_COPYING_ACTION, isCopyingAction);
    } else {
      intent.putExtra(
          AddActionActivity.SELECTED_YEAR_KEY, mCalendarActivityViewModel.getSelectedYear());
      intent.putExtra(
          AddActionActivity.SELECTED_MONTH_KEY, mCalendarActivityViewModel.getSelectedMonth());
      intent.putExtra(
          AddActionActivity.SELECTED_DAY_KEY, mCalendarActivityViewModel.getSelectedDay());
    }
    startActivity(intent);
  }

  private void gotoUnfinishedActionListActivity() {
    Intent intent = new Intent(this, UnfinishedActionListActivity.class);
    startActivity(intent);
  }
}
