package com.brickgit.tomatist.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Action;
import com.brickgit.tomatist.data.viewmodel.UnfinishedActionListViewModel;
import com.brickgit.tomatist.view.ListTouchHelperCallback;
import com.brickgit.tomatist.view.actionlist.ActionListAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/** Created by Daniel Lin on 2019/2/26. */
public class UnfinishedActionListActivity extends BaseActivity {

  private static final int REQUEST_CODE_SELECT_CATEGORY = 0;

  private UnfinishedActionListViewModel mUnfinishedActionListViewModel;

  private View mRootView;
  private RecyclerView mActionList;
  private LinearLayoutManager mLayoutManager;
  private ActionListAdapter mActionListAdapter;

  private List<Action> mUnfinishedActionList = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_unfinished_action_list);

    mRootView = findViewById(R.id.root_view);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle(R.string.unfinished_actions);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    findViewById(R.id.add_action).setOnClickListener((view) -> gotoAddActionActivity(null, false));

    mActionList = findViewById(R.id.unfinished_action_list);
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
        new ListTouchHelperCallback(
            (position) -> removeAction(mUnfinishedActionList.get(position)));
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

    mUnfinishedActionListViewModel =
        ViewModelProviders.of(this).get(UnfinishedActionListViewModel.class);
    mUnfinishedActionListViewModel
        .getUnfinishedActions()
        .observe(
            this,
            (actions) -> {
              mUnfinishedActionList.clear();
              mUnfinishedActionList.addAll(actions);
              updateViews();
            });
    mUnfinishedActionListViewModel
        .getTagMap()
        .observe(this, (tags) -> mActionListAdapter.updateTags(tags));
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  private void gotoAddActionActivity(String actionId, boolean isCopyingActin) {
    Intent intent = new Intent(this, AddActionActivity.class);
    if (actionId != null) {
      intent.putExtra(AddActionActivity.SELECTED_ACTION_ID_KEY, actionId);
      intent.putExtra(AddActionActivity.IS_COPYING_ACTION, isCopyingActin);
    } else {
      CalendarDay today = CalendarDay.today();
      intent.putExtra(AddActionActivity.SELECTED_YEAR_KEY, today.getYear());
      intent.putExtra(AddActionActivity.SELECTED_MONTH_KEY, today.getMonth() - 1);
      intent.putExtra(AddActionActivity.SELECTED_DAY_KEY, today.getDay());
    }
    startActivity(intent);
  }

  private void removeAction(Action action) {
    if (action != null) {
      mUnfinishedActionListViewModel.deleteAction(action);
      showActionDeletedConfirmation(action);
    }
  }

  private void showActionDeletedConfirmation(Action action) {
    Snackbar.make(mRootView, R.string.action_deleted, Snackbar.LENGTH_SHORT)
        .setAction(R.string.undo, (view) -> mUnfinishedActionListViewModel.insertAction(action))
        .show();
  }

  private void updateViews() {
    String tag = getString(R.string.all);
    mActionListAdapter.updateActions(tag, mUnfinishedActionList);
  }
}
