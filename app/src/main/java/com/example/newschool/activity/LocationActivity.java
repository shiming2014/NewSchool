package com.example.newschool.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.newschool.R;
import com.example.newschool.view.SongFab;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

import cn.bmob.v3.BmobUser;

public class LocationActivity extends AppCompatActivity {
    private MapView mMapView;
    private BaiduMap baiduMap;
    private MapStatusUpdate update;
    private boolean isFirstLocate = true;
    private LatLng ll;
    private MyLocationData.Builder locationBuilder;
    private MyLocationData locationData;
    private String stuName, stuNo, time, phone, email, latInfo, longInfo;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private MaterialSheetFab materialSheetFab;
    private SongFab fab;
    private View sheetView;
    private TextView textName, textNo, textTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        initView();
        addListener();
    }

    private void addListener() {
        findViewById(R.id.fab_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23)
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(LocationActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    } else {
                        call(phone);
                        materialSheetFab.hideSheet();
                    }
                else {
                    call(phone);
                    materialSheetFab.hideSheet();
                }
            }
        });
        findViewById(R.id.fab_sms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23)
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(LocationActivity.this, new String[]{Manifest.permission.SEND_SMS}, 2);
                        materialSheetFab.hideSheet();
                    } else {
                        sendSMS(phone, stuName + ",你好：\n请及时过来上课并签到，否则影响平时成绩");
                        materialSheetFab.hideSheet();
                    }
                else {
                    sendSMS(phone, stuName + ",你好：\n请及时过来上课并签到，否则影响平时成绩");
                    materialSheetFab.hideSheet();
                }
            }
        });
        findViewById(R.id.fab_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmial(email, BmobUser.getCurrentUser().getEmail(), stuName);
                materialSheetFab.hideSheet();
            }
        });

        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textName = findViewById(R.id.fab_name);
                        textName.setText("姓名：" + stuName);
                        textNo = findViewById(R.id.fab_stuNo);
                        textNo.setText("学号：" + stuNo);
                        textTime = findViewById(R.id.fab_sign);
                        textTime.setText("签到：" + time);
                    }
                });
            }

        });
    }


    private void sendEmial(String emailTo, String emailFrom, String stuName) {
        Uri uri = Uri.parse("mailto:" + emailTo);
        String[] email = {emailFrom};
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
        intent.putExtra(Intent.EXTRA_SUBJECT, stuName + "--上课签到提醒--"); // 主题
        intent.putExtra(Intent.EXTRA_TEXT, "当前正在上课，请及时到场并签到，否则影响平时成绩。"); // 正文
        startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
    }


    private void sendSMS(String phoneNumber, String message) {
        if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
            intent.putExtra("sms_body", message);
            startActivity(intent);
        }
    }


    private void call(String tel) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + tel));
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call(phone);
                } else {
                    Toast.makeText(this, "未允许本应用打电话", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMS(phone, stuName + ",你好：\n请及时过来上课并签到，否则影响平时成绩");
                } else {
                    Toast.makeText(this, "未允许本应用发短信", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    /*
    * intent.putExtra("stuName",signToCourse.getStudentInfo().getStuName());
                    intent.putExtra("stuNo",signToCourse.getStudentInfo().getStuNo());
                    intent.putExtra("time",signToCourse.getCreatedAt());
                    intent.putExtra("phone",signToCourse.getStudentInfo().getMobilePhoneNumber());
                    intent.putExtra("email",signToCourse.getStudentInfo().getEmail());
                    intent.putExtra("latInfo",String.valueOf(signToCourse.getBmobGeoPoint().getLatitude()));
                intent.putExtra("longInfo",String.valueOf(signToCourse.getBmobGeoPoint().getLongitude()));

    *
    * */
    private void initView() {
        Intent intent = getIntent();
        mMapView = findViewById(R.id.LocationActivity_mapView);
        baiduMap = mMapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        stuName = intent.getStringExtra("stuName");
        stuNo = intent.getStringExtra("stuNo");
        time = intent.getStringExtra("time");
        phone = intent.getStringExtra("phone");
        email = intent.getStringExtra("email");
        latInfo = intent.getStringExtra("latInfo");
        longInfo = intent.getStringExtra("longInfo");
        navigateTo(Double.parseDouble(latInfo), Double.parseDouble(longInfo));

        toolbar = findViewById(R.id.LocationActivity_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        fab = findViewById(R.id.fab);
        sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.overlay);
        Integer sheetColor = getResources().getColor(R.color.white, null);
        Integer fabColor = getResources().getColor(R.color.colorPrimary, null);
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay,
                sheetColor, fabColor);



    }

    private void navigateTo(double latInfo, double longInfo) {
        if (isFirstLocate) {
            ll = new LatLng(latInfo, longInfo);
            update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(18f);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }


        locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(latInfo);
        locationBuilder.longitude(longInfo);
        locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);

    }

    @Override
    public void onBackPressed() {
        if (materialSheetFab.isSheetVisible()) {
            materialSheetFab.hideSheet();
        } else {
            super.onBackPressed();
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
        return false;
    }


}
