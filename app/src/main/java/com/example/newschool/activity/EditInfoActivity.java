package com.example.newschool.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.newschool.R;
import com.example.newschool.bean.CourseInfo;

import cn.bmob.v3.b.V;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class EditInfoActivity extends AppCompatActivity {
    private TextInputEditText time, location;
    private TextView send;
    private ProgressBar progressBar;
    private String courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        initView();
        addListener();
    }

    private void addListener() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });
    }

    private void sendData() {
        final Intent intent = new Intent();


        progressBar.setVisibility(View.VISIBLE);
        if (time.getText().length() == 0 && location.getText().length() == 0) {
            progressBar.setVisibility(View.GONE);
            Snackbar.make(findViewById(R.id.EditInfoActivity_), "您没有提交任何更改", Snackbar.LENGTH_SHORT).show();
        } else {
            CourseInfo courseInfo = new CourseInfo();
            if (time.getText().length() > 0) {
                courseInfo.setTime(time.getText().toString());
                intent.putExtra("time", time.getText().toString());
            }

            if (location.getText().length() > 0) {
                courseInfo.setLocation(location.getText().toString());
                intent.putExtra("location", location.getText().toString());
            }

            courseInfo.update(courseId, new UpdateListener() {

                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        progressBar.setVisibility(View.GONE);

                        setResult(102, intent);
                        finish();

                        Log.i("bmob", "更新成功");
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Log.i("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                    }
                }
            });
        }
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
        courseId = getIntent().getStringExtra("courseId");
        Toolbar toolbar = findViewById(R.id.EditInfoActivity_toolbar);
        setSupportActionBar(toolbar);
        time = findViewById(R.id.EditInfoActivity_time);
        location = findViewById(R.id.EditInfoActivity_location);
        send = findViewById(R.id.EditInfoActivity_send);
        progressBar = findViewById(R.id.EditInfoActivity_progressBar);
    }
}
