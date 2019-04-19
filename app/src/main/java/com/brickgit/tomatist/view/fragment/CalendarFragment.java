package com.brickgit.tomatist.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Action;
import com.brickgit.tomatist.data.viewmodel.CalendarActivityViewModel;
import com.brickgit.tomatist.view.actionlist.ActionListAdapter;
import com.brickgit.tomatist.view.activity.AddActionActivity;
import com.google.android.material.snackbar.Snackbar;
import com.shrikanthravi.collapsiblecalendarview.data.Day;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalendarFragment extends Fragment {

  private CalendarActivityViewModel mCalendarActivityViewModel;

  @BindView(R.id.calendar_view)
  CollapsibleCalendar mCalendarView;

  @BindView(R.id.action_list)
  RecyclerView mActionList;

  private LinearLayoutManager mLayoutManager;
  private ActionListAdapter mActionListAdapter;

  private List<Action> mActions = new ArrayList<>();

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_calendar, container, false);

    ButterKnife.bind(this, view);

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

    mActionList.setHasFixedSize(true);

    mLayoutManager = new LinearLayoutManager(getActivity());
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
          public void onCheckClick(Action action) {
            action.setFinished(!action.isFinished());
            mCalendarActivityViewModel.updateAction(action);
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
        .getActionList()
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

    return view;
  }

  private void removeAction(Action action) {
    if (action != null) {
      mCalendarActivityViewModel.deleteAction(action);
      showActionDeletedConfirmation(action);
    }
  }

  private void showActionDeletedConfirmation(Action action) {
    Snackbar.make(getView(), R.string.action_deleted, Snackbar.LENGTH_SHORT)
        .setAction(R.string.undo, (view) -> mCalendarActivityViewModel.insertAction(action))
        .show();
  }

  private void gotoAddActionActivity(String actionId, boolean isCopyingAction) {
    Intent intent = new Intent(getActivity(), AddActionActivity.class);
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
}
