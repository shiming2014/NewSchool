package com.example.newschool.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newschool.R;
import com.example.newschool.bean.SignToCourse;
import com.example.newschool.bean.StudentInfo;
import com.example.newschool.utils.ColorUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by 世铭 on 2018/5/30.
 */

public class ClassmateNotSignAdapter extends RecyclerView.Adapter<ClassmateNotSignAdapter.ViewHolder> {
    private List<StudentInfo> studentInfos;
    private Context context;

    public ClassmateNotSignAdapter(List<StudentInfo> studentInfos) {
        this.studentInfos = studentInfos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_classmate_notsign, parent, false);
        final ClassmateNotSignAdapter.ViewHolder holder = new ClassmateNotSignAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final StudentInfo studentInfo = studentInfos.get(position);
        String s = "姓名：" + studentInfo.getStuName() + "\n"
                + "学号：" + studentInfo.getStuNo();
        holder.content.setText(s);
        holder.headImg.setBackgroundColor(ColorUtil.nextColor());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.cardView2.getVisibility() == View.GONE) {
                    holder.imgRight.setVisibility(View.GONE);
                    holder.imgDown.setVisibility(View.VISIBLE);
                    holder.cardView2.setVisibility(View.VISIBLE);
                } else {
                    holder.imgRight.setVisibility(View.VISIBLE);
                    holder.imgDown.setVisibility(View.GONE);
                    holder.cardView2.setVisibility(View.GONE);

                }

            }
        });
        holder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23)
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    } else {
                        call(studentInfo.getMobilePhoneNumber());
                    }
                else {
                    call(studentInfo.getMobilePhoneNumber());
                }
            }
        });

        holder.sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23)
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.SEND_SMS}, 2);
                    } else {
                        sendSMS(studentInfo.getMobilePhoneNumber(), studentInfo.getStuName() + ",你好：\n请及时过来上课并签到，否则影响平时成绩");

                    }
                else {
                    sendSMS(studentInfo.getMobilePhoneNumber(), studentInfo.getStuName() + ",你好：\n请及时过来上课并签到，否则影响平时成绩");

                }
            }
        });

        holder.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmial(studentInfo.getEmail(), BmobUser.getCurrentUser().getEmail(), studentInfo.getStuName());
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
        context.startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
    }


    private void sendSMS(String phoneNumber, String message) {
        if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
            intent.putExtra("sms_body", message);
            context.startActivity(intent);
        }
    }


    private void call(String tel) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + tel));
            context.startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return studentInfos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView, cardView2;
        private TextView content, phone, sms, email;
        private ImageView imgRight, imgDown;
        private ImageView headImg;


        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.item_notSign_cardView);
            cardView2 = itemView.findViewById(R.id.item_notSign_cardView2);
            content = itemView.findViewById(R.id.item_notSign_name);
            imgRight = itemView.findViewById(R.id.item_notSign_arrow_right);
            imgDown = itemView.findViewById(R.id.item_notSign_arrow_down);
            phone = itemView.findViewById(R.id.item_notSign_phone);
            sms = itemView.findViewById(R.id.item_notSign_sms);
            email = itemView.findViewById(R.id.item_notSign_email);
            headImg = itemView.findViewById(R.id.item_notSign_headShot);

        }
    }

}
