package com.example.newschool.activity;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.newschool.R;
import com.example.newschool.bean.TeacherInfo;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {
    private ImageView imglogo;
    private TextView registerTextview;
    private TextInputLayout userNameLayout, pwdLayout;
    private TextInputEditText userName, pwd;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        doSomeListener();
    }

    private void doSomeListener() {
        registerTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCorrect()) {
                    doLogin();
                }
            }
        });

    }

    private void doLogin() {
        TeacherInfo teacherInfo = new TeacherInfo();
        teacherInfo.setUsername(userName.getText().toString());
        teacherInfo.setPassword(pwd.getText().toString());
        teacherInfo.login(new SaveListener<TeacherInfo>() {
            @Override
            public void done(TeacherInfo teacherInfo, BmobException e) {
                if (e == null) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "登录失败\n" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        Bmob.initialize(this, "9ff69afefff479096396f1d74541b023");
        TeacherInfo userInfo = BmobUser.getCurrentUser(TeacherInfo.class);
        if(userInfo!=null){
           Intent intent = new Intent(this, MainActivity.class);
          startActivity(intent);
        }
        imglogo = findViewById(R.id.loginActivity_logo);
        Glide.with(this).load(R.drawable.logo_white).into(imglogo);
        registerTextview = findViewById(R.id.loginActivity_register);
        userName = findViewById(R.id.loginActivity_username);
        userNameLayout = findViewById(R.id.loginActivity_usernameLayout);
        pwd = findViewById(R.id.loginActivity_pwd);
        pwdLayout = findViewById(R.id.loginActivity_pwdLayout);
        loginBtn = findViewById(R.id.loginActivity_loginBtn);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String username = data.getStringExtra("userName");
                    String password = data.getStringExtra("passWord");
                    userName.setText(username);
                    pwd.setText(password);
                }
                break;
            default:
        }
    }

    private Boolean isCorrect() {
        if (!userName.getText().toString().contains("@")) {
            userNameLayout.setError("请输入正确的邮箱号码");
            return false;
        }

        if (pwd.getText().toString().length() < 6 || pwd.getText().toString().length() >= 18) {
            pwdLayout.setError("密码控制在6-18位之间");
            return false;
        }
        return true;
    }


}
