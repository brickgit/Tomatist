package com.brickgit.tomatist.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Action;
import com.brickgit.tomatist.data.database.Category;
import com.brickgit.tomatist.data.database.CategoryGroup;
import com.brickgit.tomatist.view.actionlist.ActionListAdapter;
import com.brickgit.tomatist.view.actionlist.ActionListTouchHelperCallback;
import com.google.android.material.snackbar.Snackbar;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/** Created by Daniel Lin on 2019/2/26. */
public class UnfinishedActionListActivity extends BaseActivity {

  private static final int REQUEST_CODE_SELECT_CATEGORY = 0;

  private View mRootView;
  private Button mCategoryButton;
  private RecyclerView mActionList;
  private LinearLayoutManager mLayoutManager;
  private ActionListAdapter mActionListAdapter;

  private Map<Long, CategoryGroup> mCategoryGroups = new HashMap<>();
  private Map<Long, Category> mCategories = new HashMap<>();
  private LiveData<List<Action>> mActions;
  private Long mSelectedCategoryId = null;
  private List<Action> mSelectedAcions = new ArrayList<>();

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

    findViewById(R.id.add_action).setOnClickListener((view) -> gotoAddActionActivity(null));

    mCategoryButton = findViewById(R.id.category_button);
    mCategoryButton.setOnClickListener(
        (view) -> CategorySelectorActivity.startForResult(this, REQUEST_CODE_SELECT_CATEGORY));
    findViewById(R.id.clear)
        .setOnClickListener(
            (view) -> {
              mSelectedCategoryId = null;
              updateViews();
            });

    mActionList = findViewById(R.id.unfinished_action_list);
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

    mActions = mActionViewModel.getUnfinishedActions();
    mActions.observe(this, (actions) -> updateViews());

    mCategoryViewModel
        .getCategoryGroups()
        .observe(
            this,
            (categoryGroups) -> {
              Map<Long, CategoryGroup> map = new HashMap<>();
              for (CategoryGroup group : categoryGroups) {
                map.put(group.getId(), group);
              }
              mCategoryGroups.clear();
              mCategoryGroups.putAll(map);
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
              mCategories.clear();
              mCategories.putAll(map);
              mActionListAdapter.updateCategories(map);
            });
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  private void gotoAddActionActivity(Long actionId) {
    Intent intent = new Intent(this, AddActionActivity.class);
    if (actionId != null) {
      intent.putExtra(AddActionActivity.SELECTED_ACTION_KEY, actionId);
    } else {
      CalendarDay today = CalendarDay.today();
      intent.putExtra(AddActionActivity.SELECTED_YEAR_KEY, today.getYear());
      intent.putExtra(AddActionActivity.SELECTED_MONTH_KEY, today.getMonth() - 1);
      intent.putExtra(AddActionActivity.SELECTED_DAY_KEY, today.getDay());
    }
    startActivity(intent);
  }

  private void removeAction(int position) {
    Action action = mSelectedAcions.get(position);
    if (action != null) {
      mActionViewModel.deleteAction(action);
      showActionDeletedConfirmation(action);
    }
  }

  private void showActionDeletedConfirmation(Action action) {
    Snackbar.make(mRootView, R.string.action_deleted, Snackbar.LENGTH_SHORT)
        .setAction(R.string.undo, (view) -> mActionViewModel.insertAction(action))
        .show();
  }

  private void updateViews() {
    String tag = getString(R.string.all);
    if (mSelectedCategoryId == null) {
      mCategoryButton.setText(getString(R.string.all));
      mSelectedAcions.clear();
      if (mActions != null && mActions.getValue() != null) {
        mSelectedAcions.addAll(mActions.getValue());
      }
    } else {
      Category category = mCategories.get(mSelectedCategoryId);
      if (category != null) {
        tag = String.valueOf(category.getId());
        StringBuilder sb = new StringBuilder();
        sb.append(category.getTitle());
        CategoryGroup categoryGroup = mCategoryGroups.get(category.getGroupId());
        if (categoryGroup != null) {
          sb.insert(0, categoryGroup.getTitle() + " - ");
        }
        mCategoryButton.setText(sb.toString());

        mSelectedAcions.clear();
        if (mActions != null && mActions.getValue() != null) {
          for (Action action : mActions.getValue()) {
            if (action.getCategoryId().equals(category.getId())) {
              mSelectedAcions.add(action);
            }
          }
        }
      } else {
        mCategoryButton.setText(getString(R.string.all));
        mSelectedAcions.clear();
        if (mActions != null && mActions.getValue() != null) {
          mSelectedAcions.addAll(mActions.getValue());
        }
      }
    }
    mActionListAdapter.updateActions(tag, mSelectedAcions);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE_SELECT_CATEGORY) {
      if (resultCode == RESULT_OK) {
        long selectedCategoryId =
            data.getLongExtra(
                CategorySelectorActivity.SELECTED_CATEGORY_ID,
                CategorySelectorActivity.INVALID_SELECTED_CATEGORY_ID);
        mSelectedCategoryId =
            selectedCategoryId != CategorySelectorActivity.INVALID_SELECTED_CATEGORY_ID
                ? selectedCategoryId
                : null;
        updateViews();
      }
    }
  }
}
