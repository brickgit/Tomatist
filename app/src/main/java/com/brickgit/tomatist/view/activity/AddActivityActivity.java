package com.brickgit.tomatist.view.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Activity;
import com.brickgit.tomatist.data.database.Category;
import com.brickgit.tomatist.data.database.CategoryGroup;
import com.brickgit.tomatist.data.database.Task;
import com.brickgit.tomatist.data.preferences.TomatistPreferences;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class AddActivityActivity extends BaseActivity {

  public static final String SELECTED_ACTIVITY_KEY = "SELECTED_ACTIVITY_KEY";
  public static final long INVALID_SELECTED_ACTIVITY_ID = -1;

  public static final String SELECTED_YEAR_KEY = "SELECTED_YEAR_KEY";
  public static final String SELECTED_MONTH_KEY = "SELECTED_MONTH_KEY";
  public static final String SELECTED_DAY_KEY = "SELECTED_DAY_KEY";
  public static final int INVALID_SELECTED_DATE = -1;

  private static final int REQUEST_CODE_SELECT_CATEGORY = 0;
  private static final int REQUEST_CODE_SELECT_TASK = 1;

  private TextInputEditText mNewActivityName;
  private TextView mStartDate;
  private TextView mStartTime;
  private TextView mEndDate;
  private TextView mEndTime;
  private TextView mDurationMinutes;
  private TextView mCategoryView;
  private TextView mTaskView;
  private TextInputEditText mNewActivityNote;

  private Activity mActivity;

  private Calendar mStartCalendar = Calendar.getInstance();
  private Calendar mEndCalendar = Calendar.getInstance();

  private LiveData<CategoryGroup> mCategoryGroup;
  private Observer<CategoryGroup> mCategoryGroupObserver = (categoryGroup) -> updateCategoryView();

  private LiveData<Category> mCategory;
  private Observer<Category> mCategoryObserver =
      (category) -> {
        if (category != null) {
          if (mCategoryGroup != null) {
            mCategoryGroup.removeObserver(mCategoryGroupObserver);
          }
          Long categoryGroupId = category.getCategoryGroupId();
          if (categoryGroupId != null) {
            mCategoryGroup = mCategoryViewModel.getCategoryGroup(category.getCategoryGroupId());
            mCategoryGroup.observe(AddActivityActivity.this, mCategoryGroupObserver);
          } else {
            mCategoryGroup = null;
          }
        }
        updateCategoryView();
      };

  private LiveData<Task> mTask;
  private Observer<Task> mTaskObserver =
      (task) -> {
        if (task != null) {
          Long categoryId = task.getCategoryId();
          if (categoryId != null) {
            if (mCategory != null) mCategory.removeObserver(mCategoryObserver);
            mCategory = mCategoryViewModel.getCategory(categoryId);
            mCategory.observe(this, mCategoryObserver);
          }
        }
        updateTaskView();
      };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_activity);

    long selectedActivityId =
        getIntent().getLongExtra(SELECTED_ACTIVITY_KEY, INVALID_SELECTED_ACTIVITY_ID);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar()
        .setTitle(
            selectedActivityId != INVALID_SELECTED_ACTIVITY_ID
                ? R.string.edit_activity
                : R.string.add_activity);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    mNewActivityName = findViewById(R.id.new_activity_name);
    mNewActivityNote = findViewById(R.id.new_activity_note);
    mCategoryView = findViewById(R.id.category);
    mCategoryView.setOnClickListener(
        (v) -> CategoryActivity.startForResult(this, REQUEST_CODE_SELECT_CATEGORY));
    mTaskView = findViewById(R.id.task);
    mTaskView.setOnClickListener(
        (v) -> TaskListActivity.startAsTaskSelector(this, REQUEST_CODE_SELECT_TASK));

    mStartDate = findViewById(R.id.start_datetime_date);
    mStartTime = findViewById(R.id.start_datetime_time);
    mEndDate = findViewById(R.id.end_datetime_date);
    mEndTime = findViewById(R.id.end_datetime_time);
    mDurationMinutes = findViewById(R.id.duration_minutes);

    if (selectedActivityId != INVALID_SELECTED_ACTIVITY_ID) {
      mActivityViewModel
          .getActivity(selectedActivityId)
          .observe(
              this,
              (selectedActivity) -> {
                mActivity = selectedActivity;
                mStartCalendar.setTime(mActivity.getStartTime());
                mEndCalendar.setTime(mActivity.getEndTime());

                Long categoryId = selectedActivity.getCategoryId();
                if (categoryId != null) {
                  mCategory = mCategoryViewModel.getCategory(categoryId);
                  mCategory.observe(this, mCategoryObserver);
                } else {
                  updateCategoryView();
                }

                Long taskId = selectedActivity.getTaskId();
                if (taskId != null) {
                  mTask = mProjectViewModel.getTask(taskId);
                  mTask.observe(this, mTaskObserver);
                } else {
                  updateTaskView();
                }

                init();
              });
    } else {
      int year = getIntent().getIntExtra(SELECTED_YEAR_KEY, INVALID_SELECTED_DATE);
      int month = getIntent().getIntExtra(SELECTED_MONTH_KEY, INVALID_SELECTED_DATE);
      int day = getIntent().getIntExtra(SELECTED_DAY_KEY, INVALID_SELECTED_DATE);

      if (year != INVALID_SELECTED_DATE
          && month != INVALID_SELECTED_DATE
          && day != INVALID_SELECTED_DATE) {
        mStartCalendar.set(Calendar.YEAR, year);
        mStartCalendar.set(Calendar.MONTH, month);
        mStartCalendar.set(Calendar.DAY_OF_MONTH, day);
        mEndCalendar.set(Calendar.YEAR, year);
        mEndCalendar.set(Calendar.MONTH, month);
        mEndCalendar.set(Calendar.DAY_OF_MONTH, day);
      }

      mCategory =
          mCategoryViewModel.getCategory(
              TomatistPreferences.getInstance(this).lastUsedCategoryId());
      mCategory.observe(this, mCategoryObserver);

      init();
    }
  }

  private void init() {
    updateDateViews();
    mStartDate.setOnClickListener((v) -> showDatePicker(true));
    mEndDate.setOnClickListener((v) -> showDatePicker(false));
    updateTimeViews();
    mStartTime.setOnClickListener((v) -> showTimePicker(true));
    mEndTime.setOnClickListener((v) -> showTimePicker(false));
    updateDurationView();
    mDurationMinutes.setOnClickListener((v) -> showMinuteList());

    if (mActivity != null) {
      mNewActivityName.setText(mActivity.getTitle());
      mNewActivityNote.setText(mActivity.getNote());
    }
  }

  private void saveActivity() {
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
    Long categoryId = null;
    if (mCategory != null) {
      Category category = mCategory.getValue();
      if (category != null) {
        categoryId = category.getCategoryId();
      }
    }
    Long taskId = null;
    if (mTask != null) {
      Task task = mTask.getValue();
      if (task != null) {
        taskId = task.getTaskId();
      }
    }

    if (mActivity != null) {
      mActivity.setTitle(newActivityName);
      mActivity.setStartTime(mStartCalendar.getTime());
      mActivity.setEndTime(mEndCalendar.getTime());
      mActivity.setMinutes(minutes);
      mActivity.setCategoryId(categoryId);
      mActivity.setTaskId(taskId);
      mActivity.setNote(mNewActivityNote.getText().toString().trim());
      mActivityViewModel.updateActivity(mActivity);
    } else {
      Activity activity = new Activity();
      activity.setTitle(newActivityName);
      activity.setStartTime(mStartCalendar.getTime());
      activity.setEndTime(mEndCalendar.getTime());
      activity.setMinutes(minutes);
      activity.setCategoryId(categoryId);
      activity.setTaskId(taskId);
      activity.setNote(mNewActivityNote.getText().toString().trim());
      mActivityViewModel.insertActivity(activity);
    }

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
        saveActivity();
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
              checkCalendars(isStartDate);
              updateDateViews();
              updateDurationView();
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
              checkCalendars(isStartTime);
              updateTimeViews();
              updateDurationView();
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true)
        .show();
  }

  private void showMinuteList() {
    new AlertDialog.Builder(this)
        .setTitle(R.string.duration)
        .setItems(
            R.array.minutes_string,
            (dialog, which) -> {
              int selectedMinutes = getResources().getIntArray(R.array.minutes_int)[which];
              mEndCalendar = (Calendar) mStartCalendar.clone();
              mEndCalendar.add(Calendar.MINUTE, selectedMinutes);
              updateTimeViews();
              updateDurationView();
            })
        .create()
        .show();
  }

  private void checkCalendars(boolean isStartDateTime) {
    if (isStartDateTime) {
      if (mStartCalendar.after(mEndCalendar)) {
        mEndCalendar = (Calendar) mStartCalendar.clone();
      }
    } else {
      if (mEndCalendar.before(mStartCalendar)) {
        mStartCalendar = (Calendar) mEndCalendar.clone();
      }
    }
  }

  private void updateDateViews() {
    mStartDate.setText(
        String.format(
            Locale.getDefault(),
            "%04d-%02d-%02d",
            mStartCalendar.get(Calendar.YEAR),
            mStartCalendar.get(Calendar.MONTH) + 1,
            mStartCalendar.get(Calendar.DAY_OF_MONTH)));
    mEndDate.setText(
        String.format(
            Locale.getDefault(),
            "%04d-%02d-%02d",
            mEndCalendar.get(Calendar.YEAR),
            mEndCalendar.get(Calendar.MONTH) + 1,
            mEndCalendar.get(Calendar.DAY_OF_MONTH)));
  }

  private void updateTimeViews() {
    mStartTime.setText(
        String.format(
            Locale.getDefault(),
            "%02d:%02d",
            mStartCalendar.get(Calendar.HOUR_OF_DAY),
            mStartCalendar.get(Calendar.MINUTE)));
    mEndTime.setText(
        String.format(
            Locale.getDefault(),
            "%02d:%02d",
            mEndCalendar.get(Calendar.HOUR_OF_DAY),
            mEndCalendar.get(Calendar.MINUTE)));
  }

  private void updateDurationView() {
    long minute = (mEndCalendar.getTimeInMillis() - mStartCalendar.getTimeInMillis()) / (60 * 1000);
    mDurationMinutes.setText(String.format(Locale.getDefault(), "%d", minute));
  }

  private void updateCategoryView() {
    StringBuilder sb = new StringBuilder();
    if (mCategory != null) {
      Category category = mCategory.getValue();
      if (category != null) {
        sb.append(category.getTitle());

        if (mCategoryGroup != null) {
          CategoryGroup group = mCategoryGroup.getValue();
          if (group != null && category.getCategoryGroupId().equals(group.getCategoryGroupId())) {
            sb.insert(0, group.getTitle() + " - ");
          }
        }
      }
    }
    String strCategory = sb.toString().trim();
    mCategoryView.setText(strCategory.isEmpty() ? getString(R.string.uncategorized) : strCategory);
  }

  private void updateTaskView() {
    if (mTask != null) {
      Task task = mTask.getValue();
      if (task != null) {
        mTaskView.setText(task.getTitle());
        return;
      }
    }
    mTaskView.setText(R.string.no_task);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE_SELECT_CATEGORY) {
      if (resultCode == RESULT_OK) {
        long selectedCategoryId =
            data.getLongExtra(
                CategoryActivity.SELECTED_CATEGORY_ID,
                CategoryActivity.INVALID_SELECTED_CATEGORY_ID);
        if (selectedCategoryId != CategoryActivity.INVALID_SELECTED_CATEGORY_ID) {
          if (mCategory != null) mCategory.removeObserver(mCategoryObserver);
          mCategory = mCategoryViewModel.getCategory(selectedCategoryId);
          mCategory.observe(this, mCategoryObserver);

          if (mTask != null) {
            mTask.removeObserver(mTaskObserver);
            mTask = null;
            updateTaskView();
          }
        }
      }
    } else if (requestCode == REQUEST_CODE_SELECT_TASK) {
      if (resultCode == RESULT_OK) {
        long selectedTaskId =
            data.getLongExtra(
                TaskListActivity.SELECTED_TASK_ID, TaskListActivity.INVALID_SELECTED_TASK_ID);
        if (selectedTaskId != TaskListActivity.INVALID_SELECTED_TASK_ID) {
          if (mTask != null) mTask.removeObserver(mTaskObserver);
          mTask = mProjectViewModel.getTask(selectedTaskId);
          mTask.observe(this, mTaskObserver);
        }
      }
    }
  }
}
