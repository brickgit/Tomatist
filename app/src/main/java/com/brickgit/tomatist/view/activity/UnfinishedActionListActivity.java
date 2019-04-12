package com.brickgit.tomatist.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Action;
import com.brickgit.tomatist.data.viewmodel.UnfinishedActionListViewModel;
import com.brickgit.tomatist.view.actionlist.ActionListAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/** Created by Daniel Lin on 2019/2/26. */
public class UnfinishedActionListActivity extends BaseActivity {

  private UnfinishedActionListViewModel mUnfinishedActionListViewModel;

  @BindView(R.id.root_view)
  View mRootView;

  @BindView(R.id.unfinished_action_list)
  RecyclerView mActionList;

  private LinearLayoutManager mLayoutManager;
  private ActionListAdapter mActionListAdapter;

  private List<Action> mUnfinishedActionList = new ArrayList<>();

  @Override
  protected int getLayoutId() {
    return R.layout.activity_unfinished_action_list;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle(R.string.unfinished_actions);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    findViewById(R.id.add_action).setOnClickListener((view) -> gotoAddActionActivity(null, false));

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

  private void gotoAddActionActivity(String actionId, boolean isCopyingAction) {
    Intent intent = new Intent(this, AddActionActivity.class);
    if (actionId != null) {
      intent.putExtra(AddActionActivity.SELECTED_ACTION_ID_KEY, actionId);
      intent.putExtra(AddActionActivity.IS_COPYING_ACTION, isCopyingAction);
    } else {
      Calendar today = Calendar.getInstance();
      intent.putExtra(AddActionActivity.SELECTED_YEAR_KEY, today.get(Calendar.YEAR));
      intent.putExtra(AddActionActivity.SELECTED_MONTH_KEY, today.get(Calendar.MONTH));
      intent.putExtra(AddActionActivity.SELECTED_DAY_KEY, today.get(Calendar.DAY_OF_MONTH));
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
