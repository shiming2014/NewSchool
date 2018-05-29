package com.example.newschool.utils;//package com.example.newschool.utils;
//
//
////import okhttp3.OkHttpClient;
////import okhttp3.Request;
//
//import cn.bmob.v3.Bmob;
//
///**
// * Created by 世铭 on 2018/5/22.
// * <p>
// * Error:Execution failed for task ':app:transformDexArchiveWithExternalLibsDexMergerForDebug'.
// * > com.android.builder.dexing.DexArchiveMergerException: Unable to merge dex
// */
//
//public class MainActivity extends AppCompatActivity {
//    public LocationClient mLocationClient;
//    private TextView positionText;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mLocationClient = new LocationClient(getApplicationContext());
//        mLocationClient.registerLocationListener(new MyLocationListener());
//        setContentView(R.layout.activity_main);
//        positionText = (TextView) findViewById(R.id.position_text_view);
//        List<String> permissionList = new ArrayList<>();
//        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
//        }
//        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.READ_PHONE_STATE);
//        }
//        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }
//        if (!permissionList.isEmpty()) {
//            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
//            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
//        } else {
//            requestLocation();
//        }
//    }
//
//    private void requestLocation() {
//        mLocationClient.start();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case 1:
//                if (grantResults.length > 0) {
//                    for (int result : grantResults) {
//                        if (result != PackageManager.PERMISSION_GRANTED) {
//                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
//                            finish();
//                            return;
//                        }
//                    }
//                    requestLocation();
//                } else {
//                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//                break;
//            default:
//        }
//    }
//
//    public class MyLocationListener implements BDLocationListener {
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    StringBuilder currentPosition = new StringBuilder();
//                    currentPosition.append("纬度：").append(location.getLatitude()).append("\n");
//                    currentPosition.append("经线：").append(location.getLongitude()).append("\n");
//                    currentPosition.append("定位方式：");
//                    if (location.getLocType() == BDLocation.TypeGpsLocation) {
//                        currentPosition.append("GPS");
//                    } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
//                        currentPosition.append("网络");
//                    }
//                    positionText.setText(currentPosition);
//                }
//            });
//        }
//
//        @Override
//        public void onConnectHotSpotMessage(String s, int i) {
//        }
//    }
//}


public class SignCode {
    private static String a = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";


    public static String getInvitedCode() {

        char[] rands = new char[4];
        for (int i = 0; i < rands.length; i++) {
            int rand = (int) (Math.random() * a.length());
            rands[i] = a.charAt(rand);

        }
        return String.valueOf(rands);

    }
}