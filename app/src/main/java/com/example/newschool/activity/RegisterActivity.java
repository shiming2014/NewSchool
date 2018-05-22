package com.example.newschool.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.newschool.R;
import com.example.newschool.bean.TeacherInfo;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextInputLayout usernameLayout, pwdLayout, rePwdLayout, phoneLayout, teacherNameLayout;
    private TextInputEditText username, pwd, rePwd, phone, teacherName;
    private Button button;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        addListener();
    }


    private void initView() {
        toolbar = findViewById(R.id.registerActivity_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        username = findViewById(R.id.registerActivity_username);
        usernameLayout = findViewById(R.id.registerActivity_usernameLayout);
        pwd = findViewById(R.id.registerActivity_pwd);
        pwdLayout = findViewById(R.id.registerActivity_pwdLayout);
        rePwd = findViewById(R.id.registerActivity_rePwd);
        rePwdLayout = findViewById(R.id.registerActivity_rePwdLayout);
        phone = findViewById(R.id.registerActivity_phone);
        phoneLayout = findViewById(R.id.registerActivity_phoneLayout);
        teacherName = findViewById(R.id.registerActivity_teacherName);
        teacherNameLayout = findViewById(R.id.registerActivity_teacherNameLayout);
        button = findViewById(R.id.registerActivity_loginBtn);

    }

    private void addListener() {
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                usernameLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pwdLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        rePwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                rePwdLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!rePwd.getText().toString().equals(pwd.getText().toString()))
                    rePwdLayout.setError("两次密码不一致");
            }
        });

        teacherName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                teacherNameLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                phoneLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCorrect()) {
                    doSomeRegister();
                }
            }
        });

    }

    private void doSomeRegister() {
        Toast.makeText(this, "注册中...", Toast.LENGTH_SHORT).show();
        TeacherInfo bmobUser = new TeacherInfo();
        bmobUser.setUsername(username.getText().toString());
        bmobUser.setPassword(pwd.getText().toString());
        bmobUser.setEmail(username.getText().toString());
        bmobUser.setMobilePhoneNumber(phone.getText().toString());
        bmobUser.setTeacherName(teacherName.getText().toString());
        bmobUser.signUp(new SaveListener<TeacherInfo>() {
            @Override
            public void done(TeacherInfo teacherInfo, BmobException e) {
                if (e == null) {
                    showInfo();

                } else {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showInfo() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(teacherName.getText().toString() + "," + "您好！");
        dialog.setMessage("您的\n用户名是：" + username.getText().toString() + "\n" + "密  码是：" + pwd.getText().toString() + "\n" + "是否前往登录？");
        dialog.setCancelable(false);
        dialog.setPositiveButton("现在就去", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.putExtra("userName", username.getText().toString());
                intent.putExtra("passWord", pwd.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        dialog.setNegativeButton("暂时不了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

    public Boolean isCorrect() {
        if (!username.getText().toString().contains("@")) {
            usernameLayout.setError("请输入正确的邮箱号码");
            return false;
        }

        if (pwd.getText().toString().length() < 6 || pwd.getText().toString().length() >= 18) {
            pwdLayout.setError("密码控制在6-18位之间");
            return false;
        }
        if (!rePwd.getText().toString().equals(pwd.getText().toString())) {
            rePwdLayout.setError("两次密码不一致");
            return false;
        }
        if (teacherName.getText().toString().length() == 0) {
            teacherNameLayout.setError("请输入您的称呼");
            return false;
        }
        if (phone.getText().toString().length() == 0) {
            phoneLayout.setError("请输入您的手机号");
            return false;
        }
        return true;

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
}
