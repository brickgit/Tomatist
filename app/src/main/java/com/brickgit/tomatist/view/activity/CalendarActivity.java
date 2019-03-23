package com.brickgit.tomatist.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;

import com.brickgit.tomatist.Initializer;
import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Action;
import com.brickgit.tomatist.data.viewmodel.CalendarActivityViewModel;
import com.brickgit.tomatist.view.ListTouchHelperCallback;
import com.brickgit.tomatist.view.actionlist.ActionListAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarActivity extends BaseActivity {

  private CalendarActivityViewModel mCalendarActivityViewModel;

  private View mRootView;
  private DrawerLayout mDrawerLayout;
  private MaterialCalendarView mCalendarView;
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
            default:
              break;
          }
          mDrawerLayout.closeDrawers();
          return true;
        });

    findViewById(R.id.add_action).setOnClickListener((view) -> gotoAddActionActivity(null, false));

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
    CalendarDay today = CalendarDay.today();
    mCalendarView.setDateSelected(today, true);
    mCalendarView.setOnDateChangedListener(
        (view, day, b) ->
            mCalendarActivityViewModel.selectDate(day.getYear(), day.getMonth(), day.getDay()));

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

    ItemTouchHelper.Callback callback =
        new ListTouchHelperCallback((position) -> removeAction(mActions.get(position)));
    ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
    touchHelper.attachToRecyclerView(mActionList);

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
    mCalendarActivityViewModel.selectDate(today.getYear(), today.getMonth(), today.getDay());

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
          AddActionActivity.SELECTED_MONTH_KEY, mCalendarActivityViewModel.getSelectedMonth() - 1);
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
