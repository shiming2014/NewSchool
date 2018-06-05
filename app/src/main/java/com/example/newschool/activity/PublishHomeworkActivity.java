package com.example.newschool.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.newschool.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

import com.example.newschool.adapter.FileListAdapter;
import com.example.newschool.bean.CourseInfo;
import com.example.newschool.bean.HomeworkInfo;
import com.example.newschool.bean.TeacherInfo;

public class PublishHomeworkActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextInputLayout titleLayout, subTitleLayout;
    private ProgressBar progressBar;
    private TextInputEditText title, subTitle;
    private List<String> photoPaths, docPaths;
    private BottomSheetDialog dialog;
    private List<String> list;
    private RecyclerView recyclerView;
    private EditText theme;
    private TextView deadDate, deadTime;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_homework);
        initView();
        doSomeListener();
    }

    private void doSomeListener() {
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                titleLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        deadDate.setOnClickListener(new View.OnClickListener() {
            Calendar calendar1 = Calendar.getInstance();
            int mYear = calendar1.get(Calendar.YEAR);
            int mMonth = calendar1.get(Calendar.MONTH);
            int mDay = calendar1.get(Calendar.DAY_OF_MONTH);

            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint({"SetTextI18n", "DefaultLocale"})
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        deadDate.setText(year + "-" + String.format("%02d", (month + 1)) + "-" + String.format("%02d", dayOfMonth));

                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });
        deadTime.setOnClickListener(new View.OnClickListener() {
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
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time.append(" " + String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
                        //设置TextView显示最终选择的时间
                        deadTime.setText(time);
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });
    }

    /*
    * deadline.setText(Calendar.getInstance().get(Calendar.YEAR) + "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + " " + Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + (Calendar.getInstance().get(Calendar.MINUTE)+5));
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
    *
    *
    *
    * */
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void initView() {
        recyclerView = findViewById(R.id.PublishHomeworkActivity_recyclerView);
        toolbar = findViewById(R.id.PublishHomeworkActivity_toolbar);
        setSupportActionBar(toolbar);
        title = findViewById(R.id.PublishHomeworkActivity_title);
        titleLayout = findViewById(R.id.PublishHomeworkActivity_titleLayout);
        subTitle = findViewById(R.id.PublishHomeworkActivity_subTitle);
        subTitleLayout = findViewById(R.id.PublishHomeworkActivity_subTitleLayout);
        theme = findViewById(R.id.PublishHomeworkActivity_theme);
        deadDate = findViewById(R.id.PublishHomeworkActivity_deadline_date);
        deadDate.setText(Calendar.getInstance().get(Calendar.YEAR) + "-" + String.format("%02d", (Calendar.getInstance().get(Calendar.MONTH) + 1)) + "-" + String.format("%02d", Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
        deadTime = findViewById(R.id.PublishHomeworkActivity_deadline_time);
        deadTime.setText(String.format("%02d", Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", Calendar.getInstance().get(Calendar.MINUTE)));
        progressBar = findViewById(R.id.PublishHomeworkActivity_progressBar);
        list = new ArrayList<>();

        dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.item_bottomsheet_share, null);
        dialog.setContentView(view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 为toolbar创建Menu
        getMenuInflater().inflate(R.menu.sharefile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //TODO 菜单的逻辑实现
            case android.R.id.home:
                finish();
                break;
            case R.id.share_attach:

                dialog.show();

                break;
            case R.id.share_send:
                if (isCorrect())
                    doSend();
                break;
            case R.id.share_callback:
                Toast.makeText(this, "日志已发送", Toast.LENGTH_SHORT).show();
                break;


        }
        return true;
    }


    /*
    * private CourseInfo courseInfo;
    private TeacherInfo teacherInfo;
    private BmobDate deadline;
    private String theme;
    private String title;
    private String describe;
    private List<BmobFile> files;
    private String done,notDone;
    *
    *
    * */
    private void doSend() {
        progressBar.setVisibility(View.VISIBLE);
        String tempDate = deadDate.getText() + " " + deadTime.getText();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = sdf.parse(tempDate);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("doSend: ", e.toString());
        }
        //Log.i( "IME: ",BmobInstallationManager.getInstallationId());
        final String[] temp = list.toArray(new String[list.size()]);
        final Date finalDate = date;
        if (temp.length > 0)
            BmobFile.uploadBatch(temp, new UploadBatchListener() {

                @Override
                public void onSuccess(List<BmobFile> files, List<String> urls) {
                    //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                    //2、urls-上传文件的完整url地址
                    if (urls.size() == temp.length) {//如果数量相等，则代表文件全部上传完成

                        Log.i("onSuccess: ", files.toString());
                        Log.i("onSuccess: ", urls.toString());
                        TeacherInfo teacherInfo = BmobUser.getCurrentUser(TeacherInfo.class);
                        HomeworkInfo homeworkInfo = new HomeworkInfo();
                        CourseInfo courseInfo = new CourseInfo();
                        courseInfo.setObjectId(getIntent().getStringExtra("invitedCode"));
                        homeworkInfo.setCourseInfo(courseInfo);
                        homeworkInfo.setTeacherInfo(teacherInfo);
                        if (temp.length != 0)
                            homeworkInfo.setFiles(files);
                        homeworkInfo.setDeadline(new BmobDate(finalDate));
                        if (null != theme.getText())
                            homeworkInfo.setTheme(theme.getText().toString());
                        homeworkInfo.setTitle(title.getText().toString());
                        if (null != subTitle.getText())
                            homeworkInfo.setDescribe(subTitle.getText().toString());
                        homeworkInfo.save(new SaveListener<String>() {

                            @Override
                            public void done(String objectId, BmobException e) {
                                if (e == null) {
                                    Log.i("创建数据成功：", objectId);

                                    progressBar.setVisibility(View.GONE);
                                    title.setFocusable(true);
                                    finish();


                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    title.setFocusable(true);
                                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                                }
                            }
                        });

                        title.setFocusable(true);
                        //do something
                    }

                }

                @Override
                public void onError(int statuscode, String errormsg) {
                    progressBar.setVisibility(View.GONE);
                    title.setFocusable(true);
                    Snackbar.make(findViewById(R.id.PublishHomeworkActivity_), "错误码" + statuscode + ",错误描述：" + errormsg, Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                    //1、curIndex--表示当前第几个文件正在上传
                    //2、curPercent--表示当前上传文件的进度值（百分比）
                    //3、total--表示总的上传文件数
                    //4、totalPercent--表示总的上传进度（百分比）
                }
            });
        else {
            TeacherInfo teacherInfo = BmobUser.getCurrentUser(TeacherInfo.class);
            HomeworkInfo homeworkInfo = new HomeworkInfo();
            CourseInfo courseInfo = new CourseInfo();
            courseInfo.setObjectId(getIntent().getStringExtra("invitedCode"));
            homeworkInfo.setCourseInfo(courseInfo);
            homeworkInfo.setTeacherInfo(teacherInfo);
            homeworkInfo.setDeadline(new BmobDate(finalDate));
            if (null != theme.getText())
                homeworkInfo.setTheme(theme.getText().toString());
            homeworkInfo.setTitle(title.getText().toString());
            if (null != subTitle.getText())
                homeworkInfo.setDescribe(subTitle.getText().toString());
            homeworkInfo.save(new SaveListener<String>() {

                @Override
                public void done(String objectId, BmobException e) {
                    if (e == null) {
                        Log.i("创建数据成功：", objectId);

                        progressBar.setVisibility(View.GONE);
                        title.setFocusable(true);
                        finish();


                    } else {
                        progressBar.setVisibility(View.GONE);
                        title.setFocusable(true);
                        Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    }
                }
            });
        }

    }

    private Boolean isCorrect() {
        if (title.getText().toString().length() == 0) {
            titleLayout.setError("请输入作业的主题");
            return false;
        }


        return true;
    }

    public void bottomsheetOnClick(View view) {
        switch (view.getId()) {
            case R.id.bottomsheet_cloud:
                dialog.dismiss();
                break;

            case R.id.bottomsheet_file:
                showTint(this);

                dialog.dismiss();
                break;
            case R.id.bottomsheet_link:
                dialog.dismiss();
                break;
            case R.id.bottomsheet_photo:
                FilePickerBuilder.getInstance().setMaxCount(9)
                        .setActivityTheme(R.style.LibAppTheme)
                        .enableCameraSupport(true)
                        .pickPhoto(this, FilePickerConst.REQUEST_CODE_PHOTO);
                dialog.dismiss();
                break;
            case R.id.bottomsheet_video:
                FilePickerBuilder.getInstance().setMaxCount(9)
                        .setActivityTheme(R.style.LibAppTheme)
                        .enableCameraSupport(true)
                        .enableVideoPicker(true)
                        .setActivityTitle("视频文件")
                        .pickPhoto(this, FilePickerConst.REQUEST_CODE_PHOTO);
                dialog.dismiss();
                break;

        }


    }

    private void showTint(final Activity activity) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("注意");
        dialog.setMessage("此选项将扫描您设备上的全部文件，然后进行归类显示，如果您的设备文件较多，将花费较多等待时间");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] zipTypes = new String[]{".zip", ".rar", ".7z"};
                FilePickerBuilder.getInstance().setMaxCount(9)
                        .setActivityTheme(R.style.LibAppTheme)
                        .enableDocSupport(true)
                        .enableSelectAll(true)
                        .addFileSupport("ZIP", zipTypes, R.drawable.ic_zip_icon)
                        .pickFile(activity, FilePickerConst.REQUEST_CODE_DOC);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();


    }

    private void showFile(List<String> photoPaths, List<String> docPaths) {


        if (null != photoPaths)
            list.addAll(photoPaths);
        if (null != docPaths)
            list.addAll(docPaths);
        Log.i("showFile: ", list.toString());

        FileListAdapter fileListAdapter = new FileListAdapter(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        fileListAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(fileListAdapter);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        photoPaths = new ArrayList<>();
        docPaths = new ArrayList<>();
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if (resultCode == Activity.RESULT_OK && data != null) {

                    photoPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                    Log.i("onActivityResult: ", photoPaths.toString());
                }
                break;
            case FilePickerConst.REQUEST_CODE_DOC:
                if (resultCode == Activity.RESULT_OK && data != null) {

                    docPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                    Log.i("onActivityResult: ", docPaths.toString());

                }
                break;
        }
        showFile(photoPaths, docPaths);
    }
}
