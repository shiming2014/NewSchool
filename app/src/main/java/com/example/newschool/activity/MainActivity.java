package com.example.newschool.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.newschool.R;
import com.example.newschool.bean.TeacherInfo;
import com.example.newschool.fragment.ArchiveCourseFragment;
import com.example.newschool.fragment.ClassroomFragment;

import cn.bmob.v3.BmobUser;

public class MainActivity extends AppCompatActivity implements
        ClassroomFragment.OnFragmentInteractionListener
        , ArchiveCourseFragment.OnFragmentInteractionListener {
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private TextView name, email;



    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.mainActivity_Fragment, fragment);
        transaction.commit();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case R.id.nav_home: // 在这里可以进行UI操作
                    toolbar.setTitle("课程");
                    break;
                case R.id.nav_archive:
                    toolbar.setTitle("归档");
                    break;

                case R.id.nav_location:
                    toolbar.setTitle("位置");
                    break;
                case R.id.nav_notification:
                    toolbar.setTitle("通知");
                    break;
            }
        }
    };

    private void setToolbarTitle(final int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = id;
                handler.sendMessage(message); // 将Message对象发送出去
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        addData();
        addListener();
    }

    private void addListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        setToolbarTitle(R.id.nav_home);
                        replaceFragment(new ClassroomFragment());
                        break;
                    case R.id.nav_calendar:
//                        setToolbarTitle(R.id.nav_calendar);
//                        replaceFragment(new ClassroomFragment());
                        Intent i = new Intent();
                        ComponentName cn = null;
                        if (Build.VERSION.SDK_INT >= 8) {
                            cn = new ComponentName("com.android.calendar", "com.android.calendar.LaunchActivity");
                        } else {
                            cn = new ComponentName("com.google.android.calendar", "com.android.calendar.LaunchActivity");
                        }
                        i.setComponent(cn);
                        startActivity(i);


                        break;
//                    case R.id.nav_location:
//                        setToolbarTitle(R.id.nav_location);
//                        replaceFragment(new ClassroomFragment());
//                        break;
//                    case R.id.nav_notification:
//                        setToolbarTitle(R.id.nav_home);
//                        replaceFragment(new ClassroomFragment());
//                        break;
                    case R.id.nav_archive:
                        setToolbarTitle(R.id.nav_archive);
                        replaceFragment(new ArchiveCourseFragment());
                        break;

//                    case R.id.nav_share:
//                        setToolbarTitle(R.id.nav_archive);
//                        replaceFragment(new ArchiveCourseFragment());
//                        break;
//
                    case R.id.nav_logout:
                        BmobUser.logOut();
                        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();

                        break;


                }


                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }

    private void addData() {
        TeacherInfo userInfo = BmobUser.getCurrentUser(TeacherInfo.class);
        name.setText(userInfo.getTeacherName());
        email.setText(userInfo.getEmail());
    }

    private void initView() {
        toolbar = findViewById(R.id.mainActivity_toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.mainActivity_drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.mainActivity_navigationView);

        View headerView = navigationView.getHeaderView(0);
        name = headerView.findViewById(R.id.username);
        email = headerView.findViewById(R.id.mail);




    }

    /*
    * 总结

Fragment中启动，Activity中接收结果：

使用getActivity().startActivityForResult()

Fragment中启动，Fragment中接收结果：

使用startActivityForResult()，且在Activity的onActivityResult()中调用super.onActivityResult()

    *
    *
    *
    *
    * */


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
            case 2:
                if (resultCode == RESULT_OK) {
                    getSupportFragmentManager()
                            .findFragmentById(R.id.mainActivity_Fragment).onActivityResult(requestCode, resultCode, data);

                }
                break;

        }

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
