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
import com.brickgit.tomatist.data.viewmodel.UnfinishedActionListViewModel;
import com.brickgit.tomatist.view.actionlist.ActionListAdapter;
import com.brickgit.tomatist.view.activity.AddActionActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlanFragment extends Fragment {

  private UnfinishedActionListViewModel mUnfinishedActionListViewModel;

  @BindView(R.id.unfinished_action_list)
  RecyclerView mActionList;

  private LinearLayoutManager mLayoutManager;
  private ActionListAdapter mActionListAdapter;

  private List<Action> mUnfinishedActionList = new ArrayList<>();

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_unfinished_action_list, container, false);

    ButterKnife.bind(this, view);

    mActionList.setHasFixedSize(true);

    mLayoutManager = new LinearLayoutManager(getContext());
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
          public void onCheckClick(Action action) {}

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

    return view;
  }

  private void gotoAddActionActivity(String actionId, boolean isCopyingAction) {
    Intent intent = new Intent(getContext(), AddActionActivity.class);
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
    Snackbar.make(getView(), R.string.action_deleted, Snackbar.LENGTH_SHORT)
        .setAction(R.string.undo, (view) -> mUnfinishedActionListViewModel.insertAction(action))
        .show();
  }

  private void updateViews() {
    String tag = getString(R.string.all);
    mActionListAdapter.updateActions(tag, mUnfinishedActionList);
  }
}
