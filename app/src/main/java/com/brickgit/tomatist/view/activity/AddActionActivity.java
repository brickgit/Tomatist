package com.brickgit.tomatist.view.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Action;
import com.brickgit.tomatist.data.database.Category;
import com.brickgit.tomatist.data.database.CategoryGroup;
import com.brickgit.tomatist.data.preferences.TomatistPreferences;
import com.brickgit.tomatist.data.viewmodel.action.ActionViewModel;
import com.brickgit.tomatist.data.viewmodel.action.EditActionViewModel;
import com.brickgit.tomatist.data.viewmodel.action.NewActionViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

public class AddActionActivity extends BaseActivity {

  public static final String SELECTED_ACTION_ID_KEY = "SELECTED_ACTION_ID_KEY";

  public static final String SELECTED_YEAR_KEY = "SELECTED_YEAR_KEY";
  public static final String SELECTED_MONTH_KEY = "SELECTED_MONTH_KEY";
  public static final String SELECTED_DAY_KEY = "SELECTED_DAY_KEY";

  private static final int REQUEST_CODE_SELECT_CATEGORY = 0;

  private TextInputEditText mActionTitleView;
  private CheckBox mIsFinished;
  private View mDatetimeLayout;
  private TextView mStartDate;
  private TextView mStartTime;
  private TextView mEndDate;
  private TextView mEndTime;
  private TextView mDurationMinutes;
  private TextView mCategoryView;
  private TextInputEditText mActionNoteView;

  private ActionViewModel mActionViewModel;
  private Action mAction;
  private CategoryGroup mCategoryGroup;
  private Category mCategory;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_action);

    String selectedActionId = getIntent().getStringExtra(SELECTED_ACTION_ID_KEY);
    boolean isEditingAction = selectedActionId != null;

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle(isEditingAction ? R.string.edit_action : R.string.add_acton);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    mActionTitleView = findViewById(R.id.new_action_name);
    mActionNoteView = findViewById(R.id.new_action_note);
    mIsFinished = findViewById(R.id.is_finished);
    mIsFinished.setOnCheckedChangeListener(
        (view, isChecked) -> {
          mAction.setFinished(isChecked);
          mDatetimeLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });
    mCategoryView = findViewById(R.id.category);
    findViewById(R.id.category_layout)
        .setOnClickListener(
            (v) -> CategorySelectorActivity.startForResult(this, REQUEST_CODE_SELECT_CATEGORY));

    mDatetimeLayout = findViewById(R.id.datetime_layout);
    mStartDate = findViewById(R.id.start_datetime_date);
    mStartDate.setOnClickListener((v) -> showDatePicker(true));
    mStartTime = findViewById(R.id.start_datetime_time);
    mStartTime.setOnClickListener((v) -> showTimePicker(true));
    mEndDate = findViewById(R.id.end_datetime_date);
    mEndDate.setOnClickListener((v) -> showDatePicker(false));
    mEndTime = findViewById(R.id.end_datetime_time);
    mEndTime.setOnClickListener((v) -> showTimePicker(false));
    mDurationMinutes = findViewById(R.id.duration_minutes);
    findViewById(R.id.duration_layout).setOnClickListener((v) -> showMinuteList());

    mActionViewModel =
        isEditingAction
            ? ViewModelProviders.of(this).get(EditActionViewModel.class)
            : ViewModelProviders.of(this).get(NewActionViewModel.class);
    mActionViewModel
        .getAction()
        .observe(
            this,
            (action) -> {
              mAction = action;
              mActionViewModel.selectCategory(mAction.getCategoryId());
              mActionTitleView.setText(mAction.getTitle());
              mActionNoteView.setText(mAction.getNote());
              mIsFinished.setChecked(mAction.isFinished());
              updateViews();
            });
    mActionViewModel
        .getSelectedCategory()
        .observe(
            this,
            (category) -> {
              mCategory = category;
              if (mAction != null) {
                mAction.setCategoryId(mCategory != null ? mCategory.getId() : null);
              }
              updateViews();
            });
    mActionViewModel
        .getSelectedCategoryGroup()
        .observe(
            this,
            (categoryGroup) -> {
              mCategoryGroup = categoryGroup;
              updateViews();
            });

    Intent intent = getIntent();
    intent.putExtra(ActionViewModel.ACTION_ID_KEY, intent.getStringExtra(SELECTED_ACTION_ID_KEY));
    intent.putExtra(
        ActionViewModel.ACTION_YEAR_KEY,
        intent.getIntExtra(SELECTED_YEAR_KEY, ActionViewModel.INVALID_ACTION_DATE));
    intent.putExtra(
        ActionViewModel.ACTION_MONTH_KEY,
        intent.getIntExtra(SELECTED_MONTH_KEY, ActionViewModel.INVALID_ACTION_DATE));
    intent.putExtra(
        ActionViewModel.ACTION_DAY_KEY,
        intent.getIntExtra(SELECTED_DAY_KEY, ActionViewModel.INVALID_ACTION_DATE));
    intent.putExtra(
        ActionViewModel.ACTION_CATEGORY_KEY,
        TomatistPreferences.getInstance(this).lastUsedCategoryId());
    mActionViewModel.init(intent);
  }

  private void saveAction() {
    String actionTitle = mActionTitleView.getText().toString().trim();
    if (actionTitle.isEmpty()) {
      mActionTitleView.setError(getString(R.string.error_name_is_required));
      return;
    }

    mActionViewModel.saveAction(
        actionTitle, mActionNoteView.getText().toString().trim(), mIsFinished.isChecked());

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
        saveAction();
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void showDatePicker(final boolean isStartDate) {
    final Calendar calendar =
        isStartDate ? mActionViewModel.getStartCalendar() : mActionViewModel.getEndCalendar();
    new DatePickerDialog(
            this,
            (view, year, month, dayOfMonth) -> {
              calendar.set(Calendar.YEAR, year);
              calendar.set(Calendar.MONTH, month);
              calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
              checkCalendars(isStartDate);
              if (isStartDate) {
                mAction.setStartTime(calendar.getTime());
              } else {
                mAction.setEndTime(calendar.getTime());
              }
              updateViews();
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH))
        .show();
  }

  private void showTimePicker(final boolean isStartTime) {
    final Calendar calendar =
        isStartTime ? mActionViewModel.getStartCalendar() : mActionViewModel.getEndCalendar();
    new TimePickerDialog(
            this,
            (view, hourOfDay, minute) -> {
              calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
              calendar.set(Calendar.MINUTE, minute);
              checkCalendars(isStartTime);
              updateViews();
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
              mActionViewModel.setEndCalendar(
                  (Calendar) mActionViewModel.getStartCalendar().clone());
              mActionViewModel.getEndCalendar().add(Calendar.MINUTE, selectedMinutes);
              updateViews();
            })
        .create()
        .show();
  }

  private void checkCalendars(boolean isStartDateTime) {
    Calendar startCalendar = mActionViewModel.getStartCalendar();
    Calendar endCalendar = mActionViewModel.getEndCalendar();

    if (isStartDateTime) {
      if (startCalendar.after(endCalendar)) {
        mActionViewModel.setEndCalendar((Calendar) startCalendar.clone());
      }
    } else {
      if (endCalendar.before(startCalendar)) {
        mActionViewModel.setStartCalendar((Calendar) endCalendar.clone());
      }
    }
  }

  private void updateViews() {

    if (mIsFinished.isChecked()) {
      mDatetimeLayout.setVisibility(View.VISIBLE);
    } else {
      mDatetimeLayout.setVisibility(View.GONE);
    }

    Calendar startCalendar = mActionViewModel.getStartCalendar();
    Calendar endCalendar = mActionViewModel.getEndCalendar();
    mStartDate.setText(
        String.format(
            Locale.getDefault(),
            "%04d-%02d-%02d",
            startCalendar.get(Calendar.YEAR),
            startCalendar.get(Calendar.MONTH) + 1,
            startCalendar.get(Calendar.DAY_OF_MONTH)));
    mStartTime.setText(
        String.format(
            Locale.getDefault(),
            "%02d:%02d",
            startCalendar.get(Calendar.HOUR_OF_DAY),
            startCalendar.get(Calendar.MINUTE)));
    mEndDate.setText(
        String.format(
            Locale.getDefault(),
            "%04d-%02d-%02d",
            endCalendar.get(Calendar.YEAR),
            endCalendar.get(Calendar.MONTH) + 1,
            endCalendar.get(Calendar.DAY_OF_MONTH)));
    mEndTime.setText(
        String.format(
            Locale.getDefault(),
            "%02d:%02d",
            endCalendar.get(Calendar.HOUR_OF_DAY),
            endCalendar.get(Calendar.MINUTE)));

    long minute = (endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis()) / (60 * 1000);
    mDurationMinutes.setText(String.format(Locale.getDefault(), "%d", minute));

    StringBuilder sb = new StringBuilder();
    if (mCategory != null) {
      sb.append(mCategory.getTitle());
      if (mCategoryGroup != null) {
        sb.insert(0, mCategoryGroup.getTitle() + " - ");
      }
    }
    String strCategory = sb.toString().trim();
    mCategoryView.setText(strCategory.isEmpty() ? getString(R.string.uncategorized) : strCategory);
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
        if (selectedCategoryId != CategorySelectorActivity.INVALID_SELECTED_CATEGORY_ID) {
          mActionViewModel.selectCategory(selectedCategoryId);
        }
      }
    }
  }
}
