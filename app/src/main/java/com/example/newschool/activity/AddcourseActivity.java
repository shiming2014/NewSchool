package com.example.newschool.activity;

import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
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
import com.example.newschool.bean.TeacherInfo;
import com.example.newschool.utils.ColorUtil;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class AddcourseActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextInputLayout courseNameLayout, classNameLayout;
    private ProgressBar progressBar;
    private TextInputEditText courseName, className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcourse);
        initView();
        doSomeListener();
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

    private void initView() {
        toolbar = findViewById(R.id.AddCourseActivity_toolbar);
        setSupportActionBar(toolbar);
        classNameLayout = findViewById(R.id.AddCourseActivity_classNameLayout);
        className = findViewById(R.id.AddCourseActivity_className);
        courseName = findViewById(R.id.AddCourseActivity_courseName);
        courseNameLayout = findViewById(R.id.AddCourseActivity_courseNameLayout);
        progressBar = findViewById(R.id.AddCourseActivity_progressBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 为toolbar创建Menu
        getMenuInflater().inflate(R.menu.menu_addcourse, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //TODO 菜单的逻辑实现
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_addcourse_callback:
                Toast.makeText(this, "日志已发送", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_addcourse_add:
                if (isCorrect())
                    doAddCourse();
                break;

        }
        return true;
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

    private void doAddCourse() {
        progressBar.setVisibility(View.VISIBLE);
        CourseInfo courseInfo = new CourseInfo();
        courseInfo.setStatus(1);
        courseInfo.setCourseName(courseName.getText().toString());
        courseInfo.setClaName(className.getText().toString());
        courseInfo.setTeacherInfo(BmobUser.getCurrentUser(TeacherInfo.class));
        final String temp=ColorUtil.getRandomColor();
        courseInfo.setColor(temp);
        courseInfo.save(new SaveListener<String>() {

            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "课程添加成功");
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent();
                    intent.putExtra("color", temp);
                    intent.putExtra("courseName", courseName.getText().toString());
                    intent.putExtra("className", className.getText().toString());
                    intent.putExtra("count", "0");
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Log.i("bmob", "课程添加失败：" + e.getMessage());
                    Toast.makeText(getApplicationContext(), "请检查网络后重试", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

}
