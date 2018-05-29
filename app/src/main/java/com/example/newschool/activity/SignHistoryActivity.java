package com.example.newschool.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.newschool.R;
import com.example.newschool.adapter.SignListAdapter;
import com.example.newschool.bean.CourseInfo;
import com.example.newschool.bean.SignInfo;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SignHistoryActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private String courseID;
    private List<SignInfo> signInfos;
    private SignListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signhistory);
        initView();
        addData();
    }

    private void addData() {
        CourseInfo courseInfo = new CourseInfo();
        courseInfo.setObjectId(courseID);
        BmobQuery<SignInfo> query = new BmobQuery<>();
        query.addWhereEqualTo("courseInfo", courseInfo);  // 查询当前用户的所有帖子
        query.order("-createdAt");
        query.include("courseInfo");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);   // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        query.setLimit(100);
        query.findObjects(new FindListener<SignInfo>() {

            @Override
            public void done(List<SignInfo> object, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "成功" + object.toString());
                    signInfos = object;
                    refreshLayout.setRefreshing(false);
                    listAdapter = new SignListAdapter(signInfos);
                    recyclerView.setAdapter(listAdapter);

                } else {
                    Log.i("bmob", "失败：" + e.getMessage());
                    refreshLayout.setRefreshing(false);
                }
            }

        });

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

    private void initView() {
        toolbar = findViewById(R.id.signListActivity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        refreshLayout = findViewById(R.id.signListActivity_RefreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addData();
            }
        });

        recyclerView = findViewById(R.id.signListActivity_recyclerView);
        Intent intent = getIntent();
        courseID = intent.getStringExtra("invitedCode");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        signInfos = new ArrayList<>();
    }
}
