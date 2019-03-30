package com.brickgit.tomatist.view.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Tag;
import com.brickgit.tomatist.data.viewmodel.GroupedActionsItem;
import com.brickgit.tomatist.data.viewmodel.report.DailyReportViewModel;
import com.brickgit.tomatist.data.viewmodel.report.MonthlyReportViewModel;
import com.brickgit.tomatist.data.viewmodel.report.ReportViewModel;
import com.brickgit.tomatist.data.viewmodel.report.YearlyReportViewModel;
import com.brickgit.tomatist.view.activity.TagSelectorActivity;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

/** Created by Daniel Lin on 2019/3/25. */
public class ReportFragment extends Fragment {

  public static final int MODE_DAILY = 0;
  public static final int MODE_MONTHLY = 1;
  public static final int MODE_YEARLY = 2;

  private static final int REQUEST_CODE_SELECT_TAG = 0;

  private int mMode = MODE_DAILY;
  private ReportViewModel mReportViewModel;
  private Map<String, Tag> mTagMap = new HashMap<>();
  private List<GroupedActionsItem> mActionList = new ArrayList<>();
  private List<String> mSelectedTagIdList = new ArrayList<>();

  private HorizontalBarChart mChart;
  private TextView mDate;

  public void setMode(int mode) {
    mMode = mode;
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_report, container, false);

    mChart = view.findViewById(R.id.chart);
    mDate = view.findViewById(R.id.date);
    mDate.setOnClickListener(
        (v) -> {
          Calendar today = Calendar.getInstance();
          mReportViewModel.selectDate(
              today.get(Calendar.YEAR),
              today.get(Calendar.MONTH),
              today.get(Calendar.DAY_OF_MONTH));
        });
    view.findViewById(R.id.backward).setOnClickListener((v) -> mReportViewModel.backward());
    view.findViewById(R.id.forward).setOnClickListener((v) -> mReportViewModel.forward());
    view.findViewById(R.id.calendar).setOnClickListener((v) -> showDatePicker());
    view.findViewById(R.id.filter)
        .setOnClickListener(
            (v) ->
                TagSelectorActivity.startForResult(
                    this, Activity.RESULT_CANCELED, mSelectedTagIdList));

    mReportViewModel =
        mMode == MODE_DAILY
            ? ViewModelProviders.of(this).get(DailyReportViewModel.class)
            : mMode == MODE_MONTHLY
                ? ViewModelProviders.of(this).get(MonthlyReportViewModel.class)
                : ViewModelProviders.of(this).get(YearlyReportViewModel.class);
    mReportViewModel
        .getTagMap()
        .observe(
            this,
            (tags) -> {
              mTagMap.clear();
              mTagMap.putAll(tags);
              updateView();
            });
    mReportViewModel
        .getGroupedActionList()
        .observe(
            this,
            (actionList) -> {
              mActionList.clear();
              mActionList.addAll(actionList);
              updateView();
            });

    Calendar today = Calendar.getInstance();
    mReportViewModel.selectDate(
        today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));

    return view;
  }

  private void updateView() {
    mDate.setText(mReportViewModel.getSelectedDate());

    int size = mActionList.size();
    long axisMax = (long) ((size == 0 ? 0 : mActionList.get(size - 1).getTotalMinutes()) * 1.3f);
    ReportValueFormatter formatter = new ReportValueFormatter(mTagMap, mActionList);

    XAxis xAxis = mChart.getXAxis();
    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    xAxis.setLabelCount(size < 10 ? size : 10);
    xAxis.setDrawAxisLine(false);
    xAxis.setDrawGridLines(false);
    xAxis.setTextSize(12);
    xAxis.setValueFormatter(formatter);
    mChart.getAxisRight().setEnabled(false);
    mChart.getAxisLeft().setEnabled(false);
    mChart.getAxisLeft().setAxisMaximum(axisMax);
    mChart.getAxisLeft().setAxisMinimum(0);
    mChart.getDescription().setEnabled(false);
    mChart.setScaleEnabled(false);
    mChart.setTouchEnabled(false);
    mChart.setDrawBarShadow(true);

    List<BarEntry> entries = new ArrayList<>();
    for (int i = 0; i < mActionList.size(); i++) {
      GroupedActionsItem item = mActionList.get(i);
      entries.add(new BarEntry(i, item.getTotalMinutes()));
    }
    BarDataSet set = new BarDataSet(entries, getString(R.string.minutes_times));
    set.setColor(getResources().getColor(R.color.colorPrimary));
    set.setBarShadowColor(Color.LTGRAY);
    BarData data = new BarData(set);
    data.setBarWidth(0.5f * size / 10f);
    data.setDrawValues(true);
    data.setValueTextSize(12);
    data.setValueFormatter(formatter);
    mChart.setData(data);
    mChart.invalidate();
    mChart.animateY(500);
  }

  private void showDatePicker() {
    new DatePickerDialog(
            getActivity(),
            (view, year, month, dayOfMonth) -> mReportViewModel.selectDate(year, month, dayOfMonth),
            mReportViewModel.getSelectedYear(),
            mReportViewModel.getSelectedMonth(),
            mReportViewModel.getSelectedDay())
        .show();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      switch (requestCode) {
        case REQUEST_CODE_SELECT_TAG:
          mSelectedTagIdList.clear();
          String selectedTagNameList = data.getStringExtra(TagSelectorActivity.SELECTED_TAG_LIST);
          if (!selectedTagNameList.isEmpty()) {
            List<String> tagIdList =
                Splitter.on(",").trimResults().splitToList(selectedTagNameList);
            mSelectedTagIdList.addAll(tagIdList);
          }
          mReportViewModel.updateSelectedTagIdList(mSelectedTagIdList);
          break;
      }
    }
  }

  private class ReportValueFormatter extends ValueFormatter {

    private final Map<String, Tag> mTagMap;
    private final List<GroupedActionsItem> mActionList;

    public ReportValueFormatter(Map<String, Tag> tagMap, List<GroupedActionsItem> actionList) {
      mTagMap = tagMap;
      mActionList = actionList;
    }

    public String getBarLabel(BarEntry barEntry) {
      float x = barEntry.getX();
      if (!isValid(x)) {
        return "";
      }
      GroupedActionsItem item = mActionList.get((int) x);
      long minutes = item.getTotalMinutes();
      long times = item.getTotalTimes();
      return String.format(Locale.getDefault(), "%d (%d)", minutes, times);
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
      if (!isValid(value)) {
        return "";
      }
      String tagId = mActionList.get((int) value).getTagId();
      if (mTagMap.get(tagId) == null) {
        return "";
      }
      return mTagMap.get(tagId).getTitle();
    }

    private boolean isValid(float value) {
      if (value < 0 || value >= mActionList.size()) {
        return false;
      }
      if ((value * 10f) % 10 > 0) {
        return false;
      }
      return true;
    }
  }
}
