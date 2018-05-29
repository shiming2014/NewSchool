package com.example.newschool.activity;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.newschool.R;
import com.example.newschool.bean.CourseInfo;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ModifycourseActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextInputLayout courseNameLayout, classNameLayout;
    private ProgressBar progressBar;
    private TextInputEditText courseName, className;
    private String _invitedCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifycourse);
        initView();
        doSomeListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.ModifyCourseActivity_toolbar);
        setSupportActionBar(toolbar);
        classNameLayout = findViewById(R.id.ModifyCourseActivity_classNameLayout);
        className = findViewById(R.id.ModifyCourseActivity_className);
        courseName = findViewById(R.id.ModifyCourseActivity_courseName);
        courseNameLayout = findViewById(R.id.ModifyCourseActivity_courseNameLayout);
        progressBar = findViewById(R.id.ModifyCourseActivity_progressBar);

        Intent intent = getIntent();
        String _courseName = intent.getStringExtra("courseName");
        String _className = intent.getStringExtra("className");

        _invitedCode = intent.getStringExtra("invitedCode");
        className.setText(_className);
        courseName.setText(_courseName);
    }

    private void doSomeListener() {
        className.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                classNameLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        courseName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                courseNameLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 为toolbar创建Menu
        getMenuInflater().inflate(R.menu.menu_modifycourse, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //TODO 菜单的逻辑实现
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_modify_callback:
                Toast.makeText(this, "日志已发送", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_modify_save:
                if (isCorrect())
                    doModifyCourse();
                break;

        }
        return true;
    }

    private void doModifyCourse() {
        progressBar.setVisibility(View.VISIBLE);
        CourseInfo courseInfo = new CourseInfo();
        if (isCorrect()) {
            courseInfo.setCourseName(courseName.getText().toString());
            courseInfo.setClaName(className.getText().toString());
            courseInfo.update(_invitedCode, new UpdateListener() {

                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        progressBar.setVisibility(View.GONE);
                        Intent intent = getIntent();
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Log.i("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                    }
                }
            });
        }

    }

    private Boolean isCorrect() {
        if (courseName.getText().toString().length() == 0) {
            courseNameLayout.setError("请输入课程名称");
            return false;
        }

        if (className.getText().toString().length() == 0) {
            classNameLayout.setError("请输入授课班级");
            return false;
        }

        return true;
    }

}
