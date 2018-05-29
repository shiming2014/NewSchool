package com.example.newschool.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.newschool.R;
import com.example.newschool.activity.BaiduMapActivity;
import com.example.newschool.bean.SignInfo;
import com.example.newschool.utils.ColorUtil;

import java.util.List;

/**
 * Created by 世铭 on 2018/5/29.
 */

public class SignListAdapter extends RecyclerView.Adapter<SignListAdapter.ViewHolder> {
    private List<SignInfo> shareFiles;
    private Context context;

    public SignListAdapter(List<SignInfo> shareFiles) {
        this.shareFiles = shareFiles;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_signlist, parent, false);
        final SignListAdapter.ViewHolder holder = new SignListAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SignInfo signInfo = shareFiles.get(position);
        holder.imageView.setBackgroundColor(ColorUtil.nextColor());
        String s = signInfo.getCourseInfo().getClaName() + "\n"
                + "签到开始于：" + signInfo.getCreatedAt().toString() + "\n"
                + "签到结束于：" + signInfo.getDeadline().getDate().toString();
        holder.title.setText(s);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, BaiduMapActivity.class);
                intent.putExtra("courseID",signInfo.getCourseInfo().getObjectId().toString());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return shareFiles.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView title;
        private ImageView imageView;


        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.item_signList_cardView);
            title = itemView.findViewById(R.id.item_signList_title);
            imageView = itemView.findViewById(R.id.item_signList_imgView);

        }
    }
}
