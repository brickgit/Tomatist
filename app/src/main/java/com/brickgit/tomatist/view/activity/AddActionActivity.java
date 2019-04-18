package com.brickgit.tomatist.view.activity;

import android.animation.ValueAnimator;
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
import com.brickgit.tomatist.data.database.Tag;
import com.brickgit.tomatist.data.viewmodel.action.ActionViewModel;
import com.brickgit.tomatist.data.viewmodel.action.CopyActionViewModel;
import com.brickgit.tomatist.data.viewmodel.action.EditActionViewModel;
import com.brickgit.tomatist.data.viewmodel.action.NewActionViewModel;
import com.brickgit.tomatist.view.tagselector.SelectedTagListAdapter;
import com.brickgit.tomatist.view.tagselector.SelectedTagRecyclerView;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.textfield.TextInputEditText;
import com.google.common.base.Splitter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;

public class AddActionActivity extends BaseActivity {

  public static final String SELECTED_ACTION_ID_KEY = "SELECTED_ACTION_ID_KEY";
  public static final String IS_COPYING_ACTION = "IS_COPYING_ACTION";

  public static final String SELECTED_YEAR_KEY = "SELECTED_YEAR_KEY";
  public static final String SELECTED_MONTH_KEY = "SELECTED_MONTH_KEY";
  public static final String SELECTED_DAY_KEY = "SELECTED_DAY_KEY";

  private static final int REQUEST_CODE_SELECT_TAG = 0;

  @BindView(R.id.empty_tag_warning_view)
  View mEmptyTagWarningView;

  @BindView(R.id.is_finished)
  CheckBox mIsFinished;

  @BindView(R.id.end_datetime_layout)
  View mEndDatetimeLayout;

  @BindView(R.id.start_datetime_date)
  TextView mStartDate;

  @BindView(R.id.start_datetime_time)
  TextView mStartTime;

  @BindView(R.id.end_datetime_date)
  TextView mEndDate;

  @BindView(R.id.end_datetime_time)
  TextView mEndTime;

  @BindView(R.id.duration_minutes)
  TextView mDurationMinutes;

  @BindView(R.id.new_action_note)
  TextInputEditText mActionNoteView;

  private SelectedTagListAdapter mSelectedTagListAdapter;

  private ActionViewModel mActionViewModel;
  private Action mAction;
  private Map<String, Tag> mTagMap = new HashMap<>();
  private List<String> mSelectedTagIdList = new ArrayList<>();

  private final SimpleDateFormat mDateFormatter =
      new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
  private final SimpleDateFormat mTimeFormatter =
      new SimpleDateFormat("HH:mm", Locale.getDefault());

  @Override
  protected int getLayoutId() {
    return R.layout.activity_add_action;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    String selectedActionId = getIntent().getStringExtra(SELECTED_ACTION_ID_KEY);
    boolean isEditingAction = selectedActionId != null;
    boolean isCopyingAction = getIntent().getBooleanExtra(IS_COPYING_ACTION, false);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar()
        .setTitle(
            isCopyingAction
                ? R.string.copy_action
                : isEditingAction ? R.string.edit_action : R.string.add_acton);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    findViewById(R.id.tag_list_layout)
        .setOnClickListener(
            (v) ->
                TagSelectorActivity.startForResult(
                    this, REQUEST_CODE_SELECT_TAG, mSelectedTagIdList));
    SelectedTagRecyclerView selectedTagListView = findViewById(R.id.tag_list_view);
    FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
    layoutManager.setFlexDirection(FlexDirection.ROW);
    layoutManager.setJustifyContent(JustifyContent.FLEX_START);
    selectedTagListView.setLayoutManager(layoutManager);
    mSelectedTagListAdapter = new SelectedTagListAdapter();
    selectedTagListView.setAdapter(mSelectedTagListAdapter);
    mSelectedTagListAdapter.setOnTagClickListener(
        (tag) ->
            TagSelectorActivity.startForResult(this, REQUEST_CODE_SELECT_TAG, mSelectedTagIdList));

    mIsFinished.setOnCheckedChangeListener(
        (view, isChecked) -> {
          mAction.setFinished(isChecked);
          mEndDatetimeLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

    mStartDate.setOnClickListener((v) -> showDatePicker(true));
    mStartTime.setOnClickListener((v) -> showTimePicker(true));
    mEndDate.setOnClickListener((v) -> showDatePicker(false));
    mEndTime.setOnClickListener((v) -> showTimePicker(false));
    findViewById(R.id.duration_layout).setOnClickListener((v) -> showMinuteList());

    mActionViewModel =
        isCopyingAction
            ? ViewModelProviders.of(this).get(CopyActionViewModel.class)
            : isEditingAction
                ? ViewModelProviders.of(this).get(EditActionViewModel.class)
                : ViewModelProviders.of(this).get(NewActionViewModel.class);
    mActionViewModel
        .getAction()
        .observe(
            this,
            (action) -> {
              mAction = action;
              mActionNoteView.setText(mAction.getNote());
              mIsFinished.setChecked(mAction.isFinished());
              mSelectedTagIdList.clear();
              mSelectedTagIdList.addAll(mAction.getTagList());
              mSelectedTagListAdapter.updateTagIds(mSelectedTagIdList);
              updateViews();
            });
    mActionViewModel
        .getTagMap()
        .observe(
            this,
            (tags) -> {
              mTagMap.clear();
              mTagMap.putAll(tags);
              mSelectedTagListAdapter.updateTagMap(mTagMap);
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
    mActionViewModel.init(intent);
  }

  private void saveAction() {
    if (mSelectedTagIdList.isEmpty()) {
      ValueAnimator animator = ValueAnimator.ofFloat(1.0f, 0.0f);
      animator.setDuration(1000);
      animator.addUpdateListener(
          (animation) -> mEmptyTagWarningView.setAlpha((float) animation.getAnimatedValue()));
      animator.start();
      return;
    }
    mActionViewModel.saveAction(
        mActionNoteView.getText().toString().trim(), mIsFinished.isChecked(), mSelectedTagIdList);

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
      mEndDatetimeLayout.setVisibility(View.VISIBLE);
    } else {
      mEndDatetimeLayout.setVisibility(View.GONE);
    }

    Date startDate = mActionViewModel.getStartCalendar().getTime();
    Date endDate = mActionViewModel.getEndCalendar().getTime();
    mStartDate.setText(mDateFormatter.format(startDate));
    mStartTime.setText(mTimeFormatter.format(startDate));
    mEndDate.setText(mDateFormatter.format(endDate));
    mEndTime.setText(mTimeFormatter.format(endDate));

    long minute = (endDate.getTime() - startDate.getTime()) / (60 * 1000);
    mDurationMinutes.setText(String.format(Locale.getDefault(), "%d", minute));
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      switch (requestCode) {
        case REQUEST_CODE_SELECT_TAG:
          mSelectedTagIdList.clear();
          String selectedTagNameList = data.getStringExtra(TagSelectorActivity.SELECTED_TAG_LIST);
          if (!selectedTagNameList.isEmpty()) {
            List<String> tagIdList =
                Splitter.on(",").trimResults().splitToList(selectedTagNameList);
            mSelectedTagIdList.addAll(tagIdList);
          }
          mSelectedTagListAdapter.updateTagIds(mSelectedTagIdList);
          break;
      }
    }
  }
}
