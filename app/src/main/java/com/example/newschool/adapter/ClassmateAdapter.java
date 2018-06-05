package com.example.newschool.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.Resource;
import com.example.newschool.R;
import com.example.newschool.activity.LocationActivity;
import com.example.newschool.bean.SignInfo;
import com.example.newschool.bean.SignToCourse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 世铭 on 2018/5/30.
 */

public class ClassmateAdapter extends RecyclerView.Adapter<ClassmateAdapter.ViewHolder> {
    private List<SignToCourse> signToCourses;
    private Context context;

    public ClassmateAdapter(List<SignToCourse> signToCourses) {
        this.signToCourses = signToCourses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_classmate, parent, false);
        final ClassmateAdapter.ViewHolder holder = new ClassmateAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SignToCourse signToCourse = signToCourses.get(position);
        String s = "姓名：" + signToCourse.getStudentInfo().getStuName() + "\n"
                + "学号：" + signToCourse.getStudentInfo().getStuNo() + "\n"
                + "签到：" + signToCourse.getCreatedAt();
        holder.content.setText(s);
        String time1 = signToCourse.getCreatedAt();
        String time2 = signToCourse.getDeadline().getDate();
        Date date1 = null, date2 = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            date1 = sdf.parse(time1);
            date2 = sdf.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date1.after(date2)) {
            holder.headshot.setBackgroundColor(Color.parseColor("#c3292b"));
            holder.signNotOnTime.setVisibility(View.VISIBLE);
            holder.signOntime.setVisibility(View.GONE);
        } else {
            holder.signNotOnTime.setVisibility(View.GONE);
            holder.signOntime.setVisibility(View.VISIBLE);
        }
        holder.distance.setText(signToCourse.getDistenceToTeacher() + "m");

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, LocationActivity.class);
                intent.putExtra("stuName",signToCourse.getStudentInfo().getStuName());
                intent.putExtra("stuNo",signToCourse.getStudentInfo().getStuNo());
                intent.putExtra("time",signToCourse.getCreatedAt());
                intent.putExtra("phone",signToCourse.getStudentInfo().getMobilePhoneNumber());
                intent.putExtra("email",signToCourse.getStudentInfo().getEmail());
                intent.putExtra("latInfo",String.valueOf(signToCourse.getBmobGeoPoint().getLatitude()));
                intent.putExtra("longInfo",String.valueOf(signToCourse.getBmobGeoPoint().getLongitude()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return signToCourses.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView content, signOntime, signNotOnTime, distance;
        private ImageView headshot;


        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.item_classmate_cardView);
            content = itemView.findViewById(R.id.item_classmate_name);
            signOntime = itemView.findViewById(R.id.item_classmate_signOnTime);
            signNotOnTime = itemView.findViewById(R.id.item_classmate_signNotOnTime);
            headshot = itemView.findViewById(R.id.item_classmate_headshot);
            distance = itemView.findViewById(R.id.item_classmate_distence);

        }
    }

}
