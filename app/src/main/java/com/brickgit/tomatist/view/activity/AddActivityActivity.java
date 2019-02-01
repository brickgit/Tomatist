package com.brickgit.tomatist.view.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Activity;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.widget.Toolbar;

public class AddActivityActivity extends BaseActivity {

  private TextInputEditText mNewActivityName;
  private TextView mStartDate;
  private TextView mStartTime;
  private TextView mEndDate;
  private TextView mEndTime;
  private TextView mDurationMinutes;
  private TextInputEditText mNewActivityNote;

  private Calendar mStartCalendar;
  private Calendar mEndCalendar;

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
    mNewActivityNote = findViewById(R.id.new_activity_note);

    mStartDate = findViewById(R.id.start_datetime_date);
    mStartTime = findViewById(R.id.start_datetime_time);
    mEndDate = findViewById(R.id.end_datetime_date);
    mEndTime = findViewById(R.id.end_datetime_time);
    mDurationMinutes = findViewById(R.id.duration_minutes);

    mStartCalendar = Calendar.getInstance();
    mEndCalendar = Calendar.getInstance();

    final Calendar today = Calendar.getInstance();
    mStartDate.setText(String.format(Locale.getDefault(), "%tF", today.getTime()));
    mStartDate.setOnClickListener((v) -> showDatePicker(true));
    mEndDate.setText(String.format(Locale.getDefault(), "%tF", today.getTime()));
    mEndDate.setOnClickListener((v) -> showDatePicker(false));
    mStartTime.setText(
        String.format(
            Locale.getDefault(),
            "%02d:%02d",
            today.get(Calendar.HOUR_OF_DAY),
            today.get(Calendar.MINUTE)));
    mStartTime.setOnClickListener((v) -> showTimePicker(true));
    mEndTime.setText(
        String.format(
            Locale.getDefault(),
            "%02d:%02d",
            today.get(Calendar.HOUR_OF_DAY),
            today.get(Calendar.MINUTE)));
    mEndTime.setOnClickListener((v) -> showTimePicker(false));
    mDurationMinutes.setText(String.format(Locale.getDefault(), "%d", 0));
  }

  private void addActivity() {
    String newActivityName = mNewActivityName.getText().toString().trim();
    if (newActivityName.isEmpty()) {
      mNewActivityName.setError(getString(R.string.error_name_is_required));
      return;
    }

    int minutes;
    try {
      minutes = Integer.valueOf(mDurationMinutes.getText().toString());
    } catch (NumberFormatException ex) {
      minutes = 0;
    }

    Activity activity = new Activity();
    activity.setTitle(newActivityName);
    activity.setStartTime(mStartCalendar.getTime());
    activity.setEndTime(mEndCalendar.getTime());
    activity.setMinutes(minutes);
    activity.setNote(mNewActivityNote.getText().toString().trim());
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

  private void showDatePicker(final boolean isStartDate) {
    final Calendar calendar = isStartDate ? mStartCalendar : mEndCalendar;
    new DatePickerDialog(
            this,
            (view, year, month, dayOfMonth) -> {
              calendar.set(Calendar.YEAR, year);
              calendar.set(Calendar.MONTH, month);
              calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
              Calendar adjustedCalendar = checkCalendars(isStartDate);
              String date =
                  String.format(
                      Locale.getDefault(),
                      "%04d-%02d-%02d",
                      adjustedCalendar.get(Calendar.YEAR),
                      adjustedCalendar.get(Calendar.MONTH) + 1,
                      adjustedCalendar.get(Calendar.DAY_OF_MONTH));
              if (isStartDate) {
                mStartDate.setText(date);
              } else {
                mEndDate.setText(date);
              }
              calculateMinutes();
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH))
        .show();
  }

  private void showTimePicker(final boolean isStartTime) {
    final Calendar calendar = isStartTime ? mStartCalendar : mEndCalendar;
    new TimePickerDialog(
            this,
            (view, hourOfDay, minute) -> {
              calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
              calendar.set(Calendar.MINUTE, minute);
              Calendar adjustedCalendar = checkCalendars(isStartTime);
              String time =
                  String.format(
                      Locale.getDefault(),
                      "%02d:%02d",
                      adjustedCalendar.get(Calendar.HOUR_OF_DAY),
                      adjustedCalendar.get(Calendar.MINUTE));
              if (isStartTime) {
                mStartTime.setText(time);
              } else {
                mEndTime.setText(time);
              }
              calculateMinutes();
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true)
        .show();
  }

  private Calendar checkCalendars(boolean isStartDateTime) {
    if (isStartDateTime) {
      if (mStartCalendar.after(mEndCalendar)) {
        mStartCalendar = (Calendar) mEndCalendar.clone();
      }
      return mStartCalendar;
    } else {
      if (mEndCalendar.before(mStartCalendar)) {
        mEndCalendar = (Calendar) mStartCalendar.clone();
      }
      return mEndCalendar;
    }
  }

  private void calculateMinutes() {
    long minute = (mEndCalendar.getTimeInMillis() - mStartCalendar.getTimeInMillis()) / (60 * 1000);
    mDurationMinutes.setText(String.format(Locale.getDefault(), "%d", minute));
  }
}
