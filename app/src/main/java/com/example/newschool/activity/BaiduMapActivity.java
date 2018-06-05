package com.example.newschool.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.newschool.R;
import com.example.newschool.application.LocationApplication;
import com.example.newschool.bean.CourseInfo;
import com.example.newschool.bean.SignInfo;
import com.example.newschool.service.LocationService;
import com.example.newschool.utils.SignCode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class BaiduMapActivity extends AppCompatActivity {
    private LocationService locationService;
    private TextView signCode, mapLong, mapLat, locationInfo, deadline;
    private final int SDK_PERMISSION_REQUEST = 127;
    private String permissionInfo;
    private MapView mMapView;
    private BaiduMap baiduMap;
    private MapStatusUpdate update;
    private LatLng ll;
    private boolean isFirstLocate = true;
    private MyLocationData.Builder locationBuilder;
    private MyLocationData locationData;
    private Toolbar toolbar;
    private BDLocation mlocation;
    private Button button;
    private double longString;
    private double latString;
    private String courseID;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidumap);
        //    LocationResult = findViewById(R.id.textView1);
        // LocationResult.setMovementMethod(ScrollingMovementMethod.getInstance());
        // startLocation = findViewById(R.id.addfence);
        mMapView = findViewById(R.id.baiduMapActivity_bmapView);
        baiduMap = mMapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        //;
        initView();
        getPersimmions();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        Intent intent = getIntent();
        courseID = intent.getStringExtra("invitedCode");
        signCode = findViewById(R.id.baiduMapActivity_sign_code);
        signCode.setText(SignCode.getInvitedCode());
        mapLong = findViewById(R.id.baiduMapActivity_long);
        mapLat = findViewById(R.id.baiduMapActivity_lat);
        locationInfo = findViewById(R.id.baiduMapActivity_locationInfo);
        deadline = findViewById(R.id.baiduMapActivity_deadline);
        deadline.setText(Calendar.getInstance().get(Calendar.YEAR) + "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + " " + Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + (Calendar.getInstance().get(Calendar.MINUTE)+5));
        deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final StringBuffer time = new StringBuffer();
                //获取Calendar对象，用于获取当前时间
                final Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH) + 1;
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                //实例化TimePickerDialog对象
                final TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    //选择完时间后会调用该回调函数
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time.append(year + "-" + month + "-" + day);
                        time.append(" " + hourOfDay + ":" + minute);
                        //设置TextView显示最终选择的时间
                        deadline.setText(time);
                    }
                }, hour, minute+5, true);
                timePickerDialog.show();


            }
        });
        toolbar = findViewById(R.id.baiduMapActivity_toolbar);
        setSupportActionBar(toolbar);

        button = findViewById(R.id.baiduMapActivity_startSign);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSign();
            }
        });

        progressBar = findViewById(R.id.AddCourseActivity_progressBar);

    }

    @SuppressLint("SimpleDateFormat")
    private void doSign() {
        progressBar.setVisibility(View.VISIBLE);
        BmobGeoPoint bmobGeoPoint = new BmobGeoPoint(longString, latString);
        final CourseInfo courseInfo = new CourseInfo();
        courseInfo.setObjectId(courseID);

        final SignInfo signInfo = new SignInfo();
        signInfo.setBmobGeoPoint(bmobGeoPoint);
        signInfo.setCourseInfo(courseInfo);
        signInfo.setSignCode(signCode.getText().toString());
        String deadLineString = deadline.getText().toString();
        try {
            Log.i("doSignString", "" + deadLineString);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = sdf.parse(deadLineString);
            signInfo.setDeadline(new BmobDate(date));
            Log.i("doSign", "" + signInfo.getDeadline().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        signInfo.save(new SaveListener<String>() {

            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
//

                    BmobRelation relation = new BmobRelation();
                    SignInfo signInfo1 = new SignInfo();
                    signInfo1.setObjectId(objectId);
                    relation.add(signInfo1);

                    Log.i("doSign: ", signInfo1.toString());
                    courseInfo.setSign(relation);

                    courseInfo.update(new UpdateListener() {

                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Log.i("bmob", "更新成功");
                                progressBar.setVisibility(View.GONE);
                                finish();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Log.i("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                            }
                        }
                    });


                } else {
                    progressBar.setVisibility(View.GONE);
                    Log.i("done: ",e.toString());
                    Toast.makeText(getApplicationContext(), "发起签到失败，请重试", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 为toolbar创建Menu
        getMenuInflater().inflate(R.menu.menu_sign, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //TODO 菜单的逻辑实现
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_sign_refresh:
                locationService.stop();
                locationService.start();
                break;
            case R.id.menu_sign_history:
                Intent intent = new Intent(this, SignHistoryActivity.class);
                intent.putExtra("invitedCode", courseID);
                startActivity(intent);
                break;


        }
        return false;
    }


    private void navigateTo(BDLocation location) {
        if (isFirstLocate) {
            ll = new LatLng(location.getLatitude(), location.getLongitude());
            update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(18f);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }


        locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
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

            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
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
                    locationService.start();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }


    }

    /**
     * 显示请求字符串
     *
     * @param str
     */
//    public void logMsg(String str) {
//        final String s = str;
//        try {
//            if (LocationResult != null) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        LocationResult.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                Log.e("logMsg", "run: " + s);
//                                LocationResult.setText(s);
//                            }
//                        });
//
//                    }
//                }).start();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /***
     * Stop activity_mylocation service
     */
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        // -----------activity_mylocation config ------------
        locationService = ((LocationApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        locationService.start();

//        startLocation.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (startLocation.getText().toString().equals(getString(R.string.startlocation))) {
//                    locationService.start();// 定位SDK
//                    // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
//                    startLocation.setText(getString(R.string.stoplocation));
//                } else {
//                    locationService.stop();
//                    startLocation.setText(getString(R.string.startlocation));
//                }
//            }
//        });
    }


    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                mlocation = location;
                longString = location.getLongitude();
                latString = location.getLatitude();

                navigateTo(location);

//                StringBuffer sb = new StringBuffer(256);
//                sb.append("time : ");
//                /**
//                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
//                 * activity_mylocation.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
//                 */
//                sb.append(location.getTime());
//                sb.append("\nlocType : ");// 定位类型
//                sb.append(location.getLocType());
//                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
//                sb.append(location.getLocTypeDescription());
//                sb.append("\nlatitude : ");// 纬度
//                sb.append(location.getLatitude());
//                sb.append("\nlontitude : ");// 经度
//                sb.append(location.getLongitude());
//                sb.append("\nradius : ");// 半径
//                sb.append(location.getRadius());
//                sb.append("\nCountryCode : ");// 国家码
//                sb.append(location.getCountryCode());
//                sb.append("\nCountry : ");// 国家名称
//                sb.append(location.getCountry());
//                sb.append("\ncitycode : ");// 城市编码
//                sb.append(location.getCityCode());
//                sb.append("\ncity : ");// 城市
//                sb.append(location.getCity());
//                sb.append("\nDistrict : ");// 区
//                sb.append(location.getDistrict());
//                sb.append("\nStreet : ");// 街道
//                sb.append(location.getStreet());
//                sb.append("\naddr : ");// 地址信息
//                sb.append(location.getAddrStr());
//                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
//                sb.append(location.getUserIndoorState());
//                sb.append("\nDirection(not all devices have value): ");
//                sb.append(location.getDirection());// 方向
//                sb.append("\nlocationdescribe: ");
//                sb.append(location.getLocationDescribe());// 位置语义化信息
//                sb.append("\nPoi: ");// POI信息
//                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
//                    for (int i = 0; i < location.getPoiList().size(); i++) {
//                        Poi poi = location.getPoiList().get(i);
//                        sb.append(poi.getName() + ";");
//                    }
//                }
//                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
//                    sb.append("\nspeed : ");
//                    sb.append(location.getSpeed());// 速度 单位：km/h
//                    sb.append("\nsatellite : ");
//                    sb.append(location.getSatelliteNumber());// 卫星数目
//                    sb.append("\nheight : ");
//                    sb.append(location.getAltitude());// 海拔高度 单位：米
//                    sb.append("\ngps status : ");
//                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
//                    sb.append("\ndescribe : ");
//                    sb.append("gps定位成功");
//                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
//                    // 运营商信息
//                    if (location.hasAltitude()) {// *****如果有海拔高度*****
//                        sb.append("\nheight : ");
//                        sb.append(location.getAltitude());// 单位：米
//                    }
//                    sb.append("\noperationers : ");// 运营商信息
//                    sb.append(location.getOperators());
//                    sb.append("\ndescribe : ");
//                    sb.append("网络定位成功");
//                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//                    sb.append("\ndescribe : ");
//                    sb.append("离线定位成功，离线定位结果也是有效的");
//                } else if (location.getLocType() == BDLocation.TypeServerError) {
//                    sb.append("\ndescribe : ");
//                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
//                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//                    sb.append("\ndescribe : ");
//                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
//                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//                    sb.append("\ndescribe : ");
//                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
//                }
//              //  logMsg(sb.toString());
//                Log.e("错误信息", "onReceiveLocation: " + permissionInfo + "|||||" + sb.toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                    mapLong=findViewById(R.id.baiduMapActivity_long);
//                    mapLat=findViewById(R.id.baiduMapActivity_lat);
//                    locationInfo=findViewById(R.id.baiduMapActivity_locationInfo);

                        mapLong.setText("经度：" + location.getLongitude());
                        mapLat.setText("纬度：" + location.getLatitude());
                        locationInfo.setText(location.getAddrStr());
                    }
                });
            }

        }

    };

}
