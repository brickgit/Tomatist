package com.brickgit.tomatist.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.brickgit.tomatist.R;

import java.util.Calendar;

import butterknife.BindView;

/** Created by Daniel Lin on 2019/2/26. */
public class UnfinishedActionListActivity extends BaseActivity {

  @BindView(R.id.root_view)
  View mRootView;

  @BindView(R.id.unfinished_action_list)
  RecyclerView mActionList;

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
}
