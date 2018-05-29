package com.example.newschool.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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

import java.util.ArrayList;

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
    private final int SDK_PERMISSION_REQUEST = 127;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        doSomeListener();
        getPersimmions();
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

    @SuppressLint("ObsoleteSdkInt")
    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }

            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {

            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }


        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SDK_PERMISSION_REQUEST:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }


    }
















    private void doLogin() {
        TeacherInfo teacherInfo = new TeacherInfo();
        teacherInfo.setUsername(userName.getText().toString());
        teacherInfo.setPassword(pwd.getText().toString());
        teacherInfo.login(new SaveListener<TeacherInfo>() {
            @Override
            public void done(TeacherInfo teacherInfo, BmobException e) {
                if (e == null) {
                    if(!"1".equals(teacherInfo.getStuOrTeacher())){
                        Toast.makeText(getApplicationContext(),"请用教师账号登录",Toast.LENGTH_LONG).show();
                    }else {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
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
