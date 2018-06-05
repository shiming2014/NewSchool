package com.example.newschool.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.newschool.R;
import com.example.newschool.adapter.FileListAdapter;
import com.example.newschool.adapter.FileListAdapter2;
import com.example.newschool.adapter.StuHomeworkListAdapter;
import com.example.newschool.bean.HomeworkInfo;
import com.example.newschool.bean.StuHomework;
import com.example.newschool.view.SongFab;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

import static cn.bmob.v3.BmobConstants.TAG;

public class CorrectHomeworkActivity extends AppCompatActivity {
    private SeekBar seekBar;
    private TextView textView;
    private String score="0";
    private String homeworkId;
    private Toolbar toolbar;
    private SwitchCompat switchCompat;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private List<String> urls;
    private String stuHomeworkId;
    private MaterialSheetFab materialSheetFab;
    private String stuName, stuNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correct_homework);
        initView();
        addData();
        addListener();
    }

    private void addListener() {
        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView textName = findViewById(R.id.fab_name);
                        textName.setText("姓名：" + stuName);
                        TextView textNo = findViewById(R.id.fab_stuNo);
                        textNo.setText("学号：" + stuNo);
                    }
                });
            }

        });
        findViewById(R.id.fab_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StuHomework stuHomework = new StuHomework();
                stuHomework.setStatus("2");
                stuHomework.setScore(-1);
                stuHomework.update(stuHomeworkId, new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            finish();
                            Log.i("bmob", "更新成功");
                        } else {
                            Log.i("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                        }
                    }
                });
            }
        });
        findViewById(R.id.fab_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StuHomework stuHomework = new StuHomework();
                stuHomework.setStatus("1");
                stuHomework.setScore(Integer.parseInt(score));
                stuHomework.update(stuHomeworkId, new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            finish();
                            Log.i("bmob", "更新成功");
                        } else {
                            Log.i("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                        }
                    }
                });
            }
        });
    }

    private void addData() {
        urls = new ArrayList<>();
        BmobQuery<StuHomework> query = new BmobQuery<>();
        query.include("studentInfo");
        query.getObject(homeworkId, new QueryListener<StuHomework>() {

            @Override
            public void done(StuHomework object, BmobException e) {
                if (e == null) {
                    if(null!=object.getScore()){
                       seekBar.setProgress(Integer.parseInt(String.valueOf(object.getScore())));
                    }
                    stuName = object.getStudentInfo().getStuName();
                    stuNo = object.getStudentInfo().getStuNo();
                    stuHomeworkId = object.getObjectId();
                    for (BmobFile b : object.getFiles()
                            ) {
                        urls.add(b.getUrl());
                    }
                    FileListAdapter2 adapter = new FileListAdapter2(urls);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                    refreshLayout.setRefreshing(false);
                } else {
                    refreshLayout.setRefreshing(false);
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
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
        return true;
    }

    private void initView() {
        urls = new ArrayList<>();
        refreshLayout = findViewById(R.id.CorrectHomeworkActivity_freshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addData();
            }
        });
        recyclerView = findViewById(R.id.CorrectHomeworkActivity_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        FileListAdapter2 adapter = new FileListAdapter2(urls);
        recyclerView.setAdapter(adapter);
        switchCompat = findViewById(R.id.CorrectHomeworkActivity_switchCompat);
        toolbar = findViewById(R.id.CorrectHomeworkActivity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        homeworkId = getIntent().getStringExtra("stuHomeworkId");
        seekBar = findViewById(R.id.progress);
        textView = findViewById(R.id.text1);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 当拖动条的滑块位置发生改变时触发该方法,在这里直接使用参数progress，即当前滑块代表的进度值
                textView.setText("得分:" + Integer.toString(progress));
                score = String.valueOf(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e("------------", "开始滑动！");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("------------", "停止滑动！");
            }
        });


        SongFab fab = findViewById(R.id.fab);
        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.overlay);
        Integer sheetColor = getResources().getColor(R.color.white, null);
        Integer fabColor = getResources().getColor(R.color.colorPrimary, null);

        // Initialize material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay,
                sheetColor, fabColor);


    }

    public void onSwitchClick(View view) {
        if (switchCompat.isChecked()) {
            seekBar.setMax(100);
        } else {
            seekBar.setMax(10);
        }
    }

}
