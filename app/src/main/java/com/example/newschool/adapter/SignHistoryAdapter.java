package com.example.newschool.adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newschool.R;
import com.example.newschool.activity.ClassbeginActivity;
import com.example.newschool.activity.MainActivity;
import com.example.newschool.activity.ModifycourseActivity;
import com.example.newschool.bean.CourseInfo;
import com.example.newschool.bean.SignItemCard;
import com.example.newschool.utils.ColorUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static android.app.Activity.RESULT_OK;


/**
 * Created by 世铭 on 2018/5/20.
 */

public class SignHistoryAdapter extends RecyclerView.Adapter<SignHistoryAdapter.ViewHolder> {
    private List<SignItemCard> signItemCards;
    private Context context;


    public SignHistoryAdapter(List<SignItemCard> signItemCards) {
        this.signItemCards = signItemCards;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_sign, parent, false);

        final ViewHolder holder = new ViewHolder(view);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                //将指定的菜单布局进行加载
                popupMenu.getMenuInflater().inflate(R.menu.menu_sign_delete, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_sign_delete:


                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();

            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SignItemCard signItemCard = signItemCards.get(position);
        holder.courseName.setText(signItemCard.getCourseName());
        holder.issueTime.setText(signItemCard.getIssueTime());
        holder.deadline.setText(signItemCard.getDeadline());
        holder.signYes.setText(signItemCard.getSignYes());
        holder.signNo.setText(signItemCard.getSignNo());
        holder.background.setBackgroundColor(ColorUtil.nextColor());


    }

    @Override
    public int getItemCount() {
        return signItemCards.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView courseName, issueTime, deadline, signYes, signNo;
        private ImageView imageView, background;

        public ViewHolder(View itemView) {
            super(itemView);
            background = itemView.findViewById(R.id.item_sign_imgView);
            cardView = itemView.findViewById(R.id.item_sign_cardiew);
            courseName = itemView.findViewById(R.id.item_sign_course);
            issueTime = itemView.findViewById(R.id.item_sign_issueTime);
            deadline = itemView.findViewById(R.id.item_sign_deadline);
            signYes = itemView.findViewById(R.id.item_sign_yes);
            signNo = itemView.findViewById(R.id.item_sign_no);
            imageView = itemView.findViewById(R.id.item_sign_more);

        }
    }
}
