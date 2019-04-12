package com.brickgit.tomatist.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.view.fragment.ReportFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

/** Created by Daniel Lin on 2019/3/17. */
public class ReportActivity extends BaseActivity {

  @BindView(R.id.tab_layout)
  TabLayout mTabLayout;

  @BindView(R.id.view_pager)
  ViewPager mViewPager;

  public static void start(Activity activity) {
    Intent intent = new Intent(activity, ReportActivity.class);
    activity.startActivity(intent);
  }

  @Override
  protected int getLayoutId() {
    return R.layout.activity_report;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle(R.string.report);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
    ReportFragment dailyReportFragment = new ReportFragment();
    dailyReportFragment.setMode(ReportFragment.MODE_DAILY);
    adapter.addFragment(dailyReportFragment, getString(R.string.title_daily));
    ReportFragment monthlyReportFragment = new ReportFragment();
    monthlyReportFragment.setMode(ReportFragment.MODE_MONTHLY);
    adapter.addFragment(monthlyReportFragment, getString(R.string.title_monthly));
    ReportFragment yearlyReportFragment = new ReportFragment();
    yearlyReportFragment.setMode(ReportFragment.MODE_YEARLY);
    adapter.addFragment(yearlyReportFragment, getString(R.string.title_yearly));
    mViewPager.setAdapter(adapter);
    mTabLayout.setupWithViewPager(mViewPager);
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  private class MainPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> titles = new ArrayList<>();

    public MainPagerAdapter(FragmentManager manager) {
      super(manager);
    }

    @Override
    public Fragment getItem(int position) {
      return fragments.get(position);
    }

    @Override
    public int getCount() {
      return fragments.size();
    }

    public void addFragment(Fragment fragment, String title) {
      fragments.add(fragment);
      titles.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return titles.get(position);
    }
  }
}
