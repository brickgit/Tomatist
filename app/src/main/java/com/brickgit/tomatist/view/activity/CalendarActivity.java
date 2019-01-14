package com.brickgit.tomatist.view.activity;

import android.os.Bundle;
import android.text.style.ForegroundColorSpan;

import com.brickgit.tomatist.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import androidx.appcompat.widget.Toolbar;

public class CalendarActivity extends BaseActivity {

  private MaterialCalendarView mCalendarView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_calendar);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    mCalendarView = findViewById(R.id.calendar_view);
    mCalendarView.addDecorator(new DayViewDecorator() {
      @Override
      public boolean shouldDecorate(CalendarDay day) {
        return day.equals(CalendarDay.today());
      }

      @Override
      public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark)));
      }
    });
    mCalendarView.setOnDateChangedListener((view, day, b) -> {
      mActivityViewModel.getActivities(day.getYear(), day.getMonth(), day.getDay());
    });
  }
}
