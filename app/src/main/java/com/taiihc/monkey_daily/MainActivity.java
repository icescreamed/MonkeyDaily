package com.taiihc.monkey_daily;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

import com.taiihc.monkey_daily.Presenters.NewsPresenter;
import com.taiihc.monkey_daily.Presenters.WelfarePresenter;
import com.taiihc.monkey_daily.Skinload.SkinBaseActivity;
import com.taiihc.monkey_daily.view.BaseFragment;
import com.taiihc.monkey_daily.view.NewsFragment;
import com.taiihc.monkey_daily.view.WelfareFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends SkinBaseActivity {
    private static final String TAG="MainActivity";
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.main_vp)
    public ViewPager viewPager;
    @BindView(R.id.main_tab)
    public TabLayout tabLayout;
    @BindView(R.id.main_drawerlayout)
    public DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    public NavigationView navigationView;
    private WelfareFragment welfareFragment;
    private WelfarePresenter welfarePresenter;
    private NewsPresenter newsPresenter;
    private List<BaseFragment> fragmentList;
    private MyFragmentVPAdapter fragmentPagerAdapter;
    private NewsFragment newsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI(){
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        fragmentList = new ArrayList<>();
        welfareFragment = WelfareFragment.getInstance();
        newsFragment = NewsFragment.getInstance();
        fragmentList.add(newsFragment);
        fragmentList.add(welfareFragment);
        fragmentPagerAdapter = new MyFragmentVPAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        welfarePresenter = new WelfarePresenter(welfareFragment);
        newsPresenter = new NewsPresenter(newsFragment);
        final ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.drawer_theme:
                        Intent intent = new Intent(MainActivity.this,ThemeSetActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.drawer_main:
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.drawer_collection:
                        Intent intent1 = new Intent(MainActivity.this,CollectionActivity.class);
                        startActivity(intent1);
                        break;
                }
                return false;
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();

    }



    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
            drawerLayout.closeDrawer(Gravity.LEFT);
        }else {
            super.onBackPressed();
        }

    }

    private class MyFragmentVPAdapter extends FragmentPagerAdapter{
        private String[] title = {"新闻","福利"};
        public MyFragmentVPAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }
}
