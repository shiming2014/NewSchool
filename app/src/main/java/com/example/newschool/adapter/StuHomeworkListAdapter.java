package com.example.newschool.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.newschool.R;
import com.example.newschool.activity.CorrectHomeworkActivity;
import com.example.newschool.activity.StuHomeworkActivity;
import com.example.newschool.bean.StuHomework;
import com.example.newschool.fragment.HomeworkFragment2;
import com.example.newschool.utils.ColorUtil;

import java.util.List;

/**
 * Created by 世铭 on 2018/6/4.
 */

public class StuHomeworkListAdapter extends RecyclerView.Adapter<StuHomeworkListAdapter.ViewHolder> {
    private List<StuHomework> stuHomeworks;
    private Context context;

    public StuHomeworkListAdapter(List<StuHomework> stuHomeworks) {
        this.stuHomeworks = stuHomeworks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_homework, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final StuHomework stuHomework = stuHomeworks.get(position);
        holder.name.setText(stuHomework.getStudentInfo().getStuNo() + " " + stuHomework.getStudentInfo().getStuName());
        if (stuHomework.getStatus().equals("2")) {
            holder.score.setText("得分：(作业已打回)");
        } else if (null == stuHomework.getScore()) {
            holder.yes.setVisibility(View.GONE);
            holder.no.setVisibility(View.VISIBLE);
        } else {
            if("-1.0".equals(String.valueOf(stuHomework.getScore()))){
                holder.yes.setVisibility(View.GONE);
                holder.no.setVisibility(View.VISIBLE);
                holder.score.setText("得分：(作业已重新提交)");
            }else {
                holder.yes.setVisibility(View.VISIBLE);
                holder.no.setVisibility(View.GONE);
                holder.score.setText("得分：" + stuHomework.getScore());
            }

        }
        holder.time.setText(stuHomework.getUpdatedAt());
        holder.linearLayout.setBackgroundColor(Color.parseColor(ColorUtil.getRandomColor()));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CorrectHomeworkActivity.class);
                intent.putExtra("stuHomeworkId", stuHomework.getObjectId());
                ((StuHomeworkActivity) context).startActivityForResult(intent, 103);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stuHomeworks.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView name, yes, no, time, score;
        private LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.item_homework_card);
            name = itemView.findViewById(R.id.item_homework_name);
            yes = itemView.findViewById(R.id.item_homework_yes);
            no = itemView.findViewById(R.id.item_homework_no);
            name = itemView.findViewById(R.id.item_homework_name);
            time = itemView.findViewById(R.id.item_homework_time);
            score = itemView.findViewById(R.id.item_homework_score);
            linearLayout = itemView.findViewById(R.id.item_homework_layout);

        }
    }
}
