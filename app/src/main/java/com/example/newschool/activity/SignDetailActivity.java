package com.example.newschool.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newschool.R;
import com.example.newschool.adapter.SignDetailFragmentAdapter;
import com.example.newschool.bean.CourseInfo;
import com.example.newschool.bean.JoinedCourse;
import com.example.newschool.bean.SignInfo;
import com.example.newschool.bean.SignToCourse;
import com.example.newschool.bean.StudentInfo;
import com.example.newschool.fragment.SignNoFragment;
import com.example.newschool.fragment.SignYesFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SignDetailActivity extends AppCompatActivity implements
        SignNoFragment.OnFragmentInteractionListener,
        SignYesFragment.OnFragmentInteractionListener {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private SignDetailFragmentAdapter fragmentAdapter;
    private TextView claName, issueTime, deadline, signYes, signNo;
    private String courseId, signInfoID, signCode;
    private SwipeRefreshLayout refreshLayout;
    private List<String> haveSign, notSign;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_detail);
        initView();
        addData();
    }

    private void addData() {
        haveSign = new ArrayList<>();
        notSign = new ArrayList<>();
        BmobQuery<SignToCourse> query = new BmobQuery<>();
        final CourseInfo courseInfo = new CourseInfo();
        courseInfo.setObjectId(courseId);

        try {
            query.addWhereLessThanOrEqualTo("issueTime", new BmobDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(getIntent().getStringExtra("issueTime"))));
            query.addWhereGreaterThanOrEqualTo("issueTime", new BmobDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(getIntent().getStringExtra("issueTime"))));
            Log.i("addData: ", getIntent().getStringExtra("issueTime"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        query.addWhereEqualTo("courseInfo", courseInfo);
        query.include("studentInfo");
        query.findObjects(new FindListener<SignToCourse>() {

            @Override
            public void done(final List<SignToCourse> object1, BmobException e) {
                if (e == null) {
                    for (SignToCourse s : object1
                            ) {
                        haveSign.add(s.getStudentInfo().getObjectId());
                    }

                    BmobQuery<JoinedCourse> query2 = new BmobQuery<JoinedCourse>();
                    query2.addWhereEqualTo("courses", courseInfo);// 查询当前用户的所有帖子
                    query2.order("-updatedAt");
                    query2.include("studentInfo");// 希望在查询帖子信息的同时也把发布人的信息查询出来
                    query2.findObjects(new FindListener<JoinedCourse>() {

                        @Override
                        public void done(List<JoinedCourse> object, BmobException e) {
                            if (e == null) {
                                Log.i("bmob", "成功");
                                for (JoinedCourse p : object
                                        ) {

                                    notSign.add(p.getStudentInfo().getObjectId());

                                }
                                notSign.removeAll(haveSign);


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        signYes.setText(String.valueOf(haveSign.size()));
                                        signNo.setText(String.valueOf(notSign.size()));
                                        refreshLayout.setRefreshing(false);

                                    }
                                });


                            } else {
                                Log.i("bmob", "失败：" + e.getMessage());
                            }
                        }

                    });


                    Log.i("bmob", "查询个数：" + object1.size());
                } else {
                    Log.i("bmob", "失败：" + e.getMessage());
                }
            }

        });


    }

    private void initView() {
        Intent intent = getIntent();
        courseId = intent.getStringExtra("courseID");
        signInfoID = intent.getStringExtra("signInfoID");
        toolbar = findViewById(R.id.signDetailActivity_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("签到详情[" + intent.getStringExtra("signCode") + "]");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        claName = findViewById(R.id.signDetailActivity_claName);
        issueTime = findViewById(R.id.signDetailActivity_issueTime);
        deadline = findViewById(R.id.signDetailActivity_deadline);
        tabLayout = findViewById(R.id.signDetailActivity_tabLayout);
        signYes = findViewById(R.id.signDetailActivity_yes);
        signNo = findViewById(R.id.signDetailActivity_no);
        viewPager = findViewById(R.id.signDetailActivity_viewPage);
        fragmentAdapter = new SignDetailFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        claName.setText(intent.getStringExtra("claName"));
        issueTime.setText(intent.getStringExtra("issueTime"));
        deadline.setText(intent.getStringExtra("deadline"));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //TODO 菜单的逻辑实现
            case android.R.id.home:
                finish();
                break;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "未允许本应用打电话", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "未允许本应用发短信", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    @Override
    public void onFragmentInteraction(final String yes, final String no) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                signYes.setText(yes);
                signNo.setText(no);

            }
        });
    }
}
