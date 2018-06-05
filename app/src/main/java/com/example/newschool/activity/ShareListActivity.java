package com.example.newschool.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;


import com.example.newschool.R;
import com.example.newschool.adapter.ShareListAdapter;
import com.example.newschool.bean.CourseInfo;
import com.example.newschool.bean.ShareFile;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ShareListActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private List<ShareFile> shareFiles;
    private String courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_list);
        initView();
        addData();
    }

    private void initView() {
        courseId = getIntent().getStringExtra("courseId");
        shareFiles = new ArrayList<>();
        toolbar = findViewById(R.id.ShareListActivity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.ShareListActivity_recyclerView);
        refreshLayout = findViewById(R.id.ShareListActivity_refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addData();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        ShareListAdapter shareListAdapter = new ShareListAdapter(shareFiles);
        recyclerView.setAdapter(shareListAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//点击back键finish当前activity
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void addData() {
        CourseInfo courseInfo = new CourseInfo();
        courseInfo.setObjectId(courseId);
        BmobQuery<ShareFile> query = new BmobQuery<>();
        query.order("-createdAt");
        query.addWhereEqualTo("courseInfo", courseInfo);
        query.include("studentInfo");
        query.setLimit(100);

        query.findObjects(new FindListener<ShareFile>() {
            @Override
            public void done(List<ShareFile> object, BmobException e) {
                if (e == null) {
                    Log.i("done: ", "查询成功：共" + object.size() + "条数据。");
                    ShareListAdapter shareListAdapter = new ShareListAdapter(object);
                    shareListAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(shareListAdapter);
                    refreshLayout.setRefreshing(false);
                } else {
                    refreshLayout.setRefreshing(false);
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }
}
