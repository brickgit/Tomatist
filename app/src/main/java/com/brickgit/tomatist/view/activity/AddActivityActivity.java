package com.brickgit.tomatist.view.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Activity;
import com.google.android.material.textfield.TextInputEditText;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Calendar;

import androidx.appcompat.widget.Toolbar;

public class AddActivityActivity extends BaseActivity {

  private TextInputEditText mNewActivityName;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_activity);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle(R.string.add_project);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    mNewActivityName = findViewById(R.id.new_activity_name);
  }

  private void addActivity() {
    String newActivityName = mNewActivityName.getText().toString().trim();
    if (newActivityName.isEmpty()) {
      mNewActivityName.setError(getString(R.string.error_name_is_required));
      return;
    }

    Activity activity = new Activity();
    activity.setTitle(newActivityName);
    activity.setStartTime(Calendar.getInstance().getTime());
    activity.setNote("Note 1234567890");
    mActivityViewModel.insertActivity(activity);

    finish();
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_add_project, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_add:
        addActivity();
        return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
