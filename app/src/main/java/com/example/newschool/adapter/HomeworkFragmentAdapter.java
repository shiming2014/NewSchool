package com.example.newschool.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.example.newschool.fragment.HomeworkFragment1;
import com.example.newschool.fragment.HomeworkFragment2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 世铭 on 2018/5/23.
 */

public class HomeworkFragmentAdapter extends FragmentPagerAdapter {
    private String[] titles = {"说明", "学生的作业"};
    private List<Fragment> mList = new ArrayList<>();

    public HomeworkFragmentAdapter(FragmentManager fm) {
        super(fm);
        mList.add(new HomeworkFragment1());
        mList.add(new HomeworkFragment2());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}
