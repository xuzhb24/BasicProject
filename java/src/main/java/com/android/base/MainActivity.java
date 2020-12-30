package com.android.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.java.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;


/**
 * Created by xuzhb on 2019/10/19
 * Desc:
 */
public class MainActivity extends BaseActivity<ActivityMainBinding> implements DontSwipeBack {

    private int[] mImageButton = new int[]{
            R.drawable.selector_bottom_frame,
            R.drawable.selector_bottom_widget,
            R.drawable.selector_bottom_util
    };

    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private ArrayList<String> mTitleList = new ArrayList<>();

    @Override
    public void handleView(Bundle savedInstanceState) {
        mFragmentList.add(FrameFragment.newInstance());
        mFragmentList.add(WidgetFragment.newInstance());
        mFragmentList.add(UtilFragment.newInstance());
        mTitleList.add("框架");
        mTitleList.add("控件");
        mTitleList.add("工具");
        initBottomNavigationBar();
    }

    //初始化底部导航栏
    private void initBottomNavigationBar() {
        for (String title : mTitleList) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(title));
        }
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return mFragmentList.get(i);
            }

            @Override
            public int getCount() {
                return mTitleList.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitleList.get(position);
            }
        };
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOffscreenPageLimit(3);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        for (int i = 0; i < mFragmentList.size(); i++) {
            TabLayout.Tab tab = binding.tabLayout.getTabAt(i);
            //添加自定义布局
            tab.setCustomView(getTabView(i));
            //默认选中第一个导航栏
            if (i == 0) {
                ((ImageView) (tab.getCustomView().findViewById(R.id.iv))).setSelected(true);
                ((TextView) (tab.getCustomView().findViewById(R.id.tv))).setSelected(true);
            }
        }

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            //选择某一个Tab时触发，返回切换后的Tab
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //解决ViewPager + Fragment，点击tab切换时造成的闪屏问题
                // 默认切换的时候，会有一个过渡动画，设为false后，取消动画，直接显示
                binding.viewPager.setCurrentItem(tab.getPosition(), false);
            }

            //切换Tab时触发，返回切换前的Tab
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            //选中之后再次点击Tab时触发
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private View getTabView(int position) {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_tab_content, null);
        ImageView iv = view.findViewById(R.id.iv);
        TextView tv = view.findViewById(R.id.tv);
        iv.setImageResource(mImageButton[position]);
        tv.setText(mTitleList.get(position));
        return view;
    }

    @Override
    protected void initBar() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

}
