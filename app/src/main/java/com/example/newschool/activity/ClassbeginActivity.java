package com.example.newschool.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.example.newschool.R;
import com.example.newschool.adapter.ClassRoomFragmentAdapter;
import com.example.newschool.fragment.ClassCourseFragment;
import com.example.newschool.fragment.ClassInfoFragment;
import com.example.newschool.fragment.ClassmateFragment;

//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.Response;

public class ClassbeginActivity extends AppCompatActivity implements
        ClassCourseFragment.OnFragmentInteractionListener,
        ClassInfoFragment.OnFragmentInteractionListener,
        ClassmateFragment.OnFragmentInteractionListener {
    private String _objectId, _courseName, _backgroundColor;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private ImageView imageView;
    private AppBarLayout appbar;
    private ClassRoomFragmentAdapter mAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classbegin);
        initView();
        addListener();
    }

    private void addListener() {
        appbar.addOnOffsetChangedListener(
                new AppBarLayout.OnOffsetChangedListener() {
                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                            collapsingToolbarLayout.setTitleEnabled(false);
                        } else {
                            collapsingToolbarLayout.setTitleEnabled(true);
                        }

                    }
                }
        );
    }

    private void initView() {
        Intent intent = getIntent();
        _objectId = intent.getStringExtra("invitedCode");
        _courseName = intent.getStringExtra("courseName");
        _backgroundColor = intent.getStringExtra("backgroundColor");
        collapsingToolbarLayout = findViewById(R.id.ClassBeginActivity_collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle(_courseName);
        collapsingToolbarLayout.setBackgroundColor(Color.parseColor(_backgroundColor));
        toolbar = findViewById(R.id.ClassBeginActivity_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        imageView = findViewById(R.id.ClassBeginActivity_img);
        appbar = findViewById(R.id.ClassBeginActivity_appBarLayout);
        mAdapter = new ClassRoomFragmentAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.ClassBeginActivity_viewPage);
        viewPager.setAdapter(mAdapter);
        tabLayout = findViewById(R.id.ClassBeginActivity_tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
