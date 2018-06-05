package com.example.newschool.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.newschool.bean.SignToCourse;
import com.example.newschool.bean.StudentInfo;
import com.example.newschool.fragment.ClassCourseFragment;
import com.example.newschool.fragment.ClassInfoFragment;
import com.example.newschool.fragment.ClassmateFragment;
import com.example.newschool.fragment.SignNoFragment;
import com.example.newschool.fragment.SignYesFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 世铭 on 2018/5/23.
 */

public class SignDetailFragmentAdapter extends FragmentPagerAdapter {
    private String[] titles = {"已签到列表", "未签到列表"};
    private List<Fragment> mList = new ArrayList<>();


    public SignDetailFragmentAdapter(FragmentManager fm) {
        super(fm);
        mList.add(new SignYesFragment());
        mList.add(new SignNoFragment());
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
