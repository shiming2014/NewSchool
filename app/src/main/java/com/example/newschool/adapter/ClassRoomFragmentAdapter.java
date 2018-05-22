package com.example.newschool.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.newschool.fragment.ClassCourseFragment;
import com.example.newschool.fragment.ClassInfoFragment;
import com.example.newschool.fragment.ClassmateFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 世铭 on 2018/5/23.
 */

public class ClassRoomFragmentAdapter extends FragmentPagerAdapter {
    private String[] titles = {"信息流", "同学", "关于"};
    private List<Fragment> mList = new ArrayList<>();

    public ClassRoomFragmentAdapter(FragmentManager fm) {
        super(fm);
        mList.add(new ClassInfoFragment());
        mList.add(new ClassmateFragment());
        mList.add(new ClassCourseFragment());
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
