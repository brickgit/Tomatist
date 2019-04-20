package com.brickgit.tomatist.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.view.fragment.CalendarFragment;
import com.brickgit.tomatist.view.fragment.PlanFragment;
import com.brickgit.tomatist.view.fragment.ReportFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

public class CalendarActivity extends BaseActivity {

  private static int SIZE_FRAGMENTS = 3;
  private static int INDEX_REPORT_FRAGMENT = 2;

  @BindView(R.id.root_view)
  View mRootView;

  @BindView(R.id.add_action)
  FloatingActionButton mAddButton;

  @BindView(R.id.drawer_layout)
  DrawerLayout mDrawerLayout;

  @BindView(R.id.tab_layout)
  TabLayout mTabLayout;

  @BindView(R.id.view_pager)
  ViewPager mViewPager;

  @Override
  protected int getLayoutId() {
    return R.layout.activity_calendar;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

    mAddButton.setOnClickListener((v) -> gotoAddActionActivity());

    MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
    PlanFragment plan = new PlanFragment();
    adapter.addFragment(plan, getString(R.string.title_plan));
    CalendarFragment sprint = new CalendarFragment();
    adapter.addFragment(sprint, getString(R.string.title_sprint));
    ReportFragment review = new ReportFragment();
    review.setMode(ReportFragment.MODE_MONTHLY);
    adapter.addFragment(review, getString(R.string.title_review));
    mViewPager.setAdapter(adapter);
    mViewPager.setOffscreenPageLimit(SIZE_FRAGMENTS);
    mViewPager.addOnPageChangeListener(
        new ViewPager.OnPageChangeListener() {
          @Override
          public void onPageScrolled(
              int position, float positionOffset, int positionOffsetPixels) {}

          @Override
          public void onPageSelected(int position) {
            if (position == INDEX_REPORT_FRAGMENT) {
              mAddButton.hide();
            } else {
              mAddButton.show();
            }
          }

          @Override
          public void onPageScrollStateChanged(int state) {}
        });
    mTabLayout.setupWithViewPager(mViewPager);

    NavigationView navigationView = findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(
        (menuItem) -> {
          switch (menuItem.getItemId()) {
            case R.id.nav_unfinished_actions:
              gotoUnfinishedActionListActivity();
              break;
            case R.id.nav_report:
              ReportActivity.start(this);
              break;
            default:
              break;
          }
          mDrawerLayout.closeDrawers();
          return true;
        });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        mDrawerLayout.openDrawer(GravityCompat.START);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void gotoUnfinishedActionListActivity() {
    Intent intent = new Intent(this, UnfinishedActionListActivity.class);
    startActivity(intent);
  }

  private void gotoAddActionActivity() {
    Intent intent = new Intent(this, AddActionActivity.class);
    Calendar today = Calendar.getInstance();
    intent.putExtra(AddActionActivity.SELECTED_YEAR_KEY, today.get(Calendar.YEAR));
    intent.putExtra(AddActionActivity.SELECTED_MONTH_KEY, today.get(Calendar.MONTH));
    intent.putExtra(AddActionActivity.SELECTED_DAY_KEY, today.get(Calendar.DAY_OF_MONTH));
    startActivity(intent);
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
