package com.example.newschool.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.newschool.R;
import com.example.newschool.adapter.HomeworkFragmentAdapter;
import com.example.newschool.fragment.HomeworkFragment1;
import com.example.newschool.fragment.HomeworkFragment2;

public class StuHomeworkActivity extends AppCompatActivity implements
        HomeworkFragment1.OnFragmentInteractionListener,
        HomeworkFragment2.OnFragmentInteractionListener{
    private Toolbar toolbar;
    private TabLayout tableLayout;
    private ViewPager viewPager;
    private HomeworkFragmentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_homework);
        initView();
    }
    private void initView() {
        toolbar=findViewById(R.id.DoHomeworkActivity_toolbar);
        tableLayout=findViewById(R.id.DoHomeworkActivity_tabLayout);
        viewPager=findViewById(R.id.DoHomeworkActivity_viewPage);
        setSupportActionBar(toolbar);
        //toolbar.setTitle("签到详情[" + intent.getStringExtra("signCode") + "]");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        adapter = new HomeworkFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPager);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //TODO 菜单的逻辑实现
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.getItem(1).onActivityResult(requestCode, resultCode, data);

    }
    @Override
    public void onFragmentInteraction(final String done,final String not) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView)findViewById(R.id.HomeworkFragment1_textDone)).setText(done);
                ((TextView)findViewById(R.id.HomeworkFragment1_textNot)).setText(not);
            }
        });
    }
}
