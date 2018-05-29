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
import com.example.newschool.activity.MainActivity;
import com.example.newschool.activity.ModifycourseActivity;
import com.example.newschool.bean.CourseInfo;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static android.app.Activity.RESULT_OK;


/**
 * Created by 世铭 on 2018/5/20.
 */

public class ArchiveCourseAdapter extends RecyclerView.Adapter<ArchiveCourseAdapter.ViewHolder> {
    private List<CourseInfo> courseInfos;
    private Context context;


    public ArchiveCourseAdapter(List<CourseInfo> courseInfos) {
        this.courseInfos = courseInfos;

    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_coursecard, parent, false);

        final ViewHolder holder = new ViewHolder(view);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                //将指定的菜单布局进行加载
                popupMenu.getMenuInflater().inflate(R.menu.menu_card_archivecourse, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_archiveCard_delete:
                                showDialog_delete(holder.invitedCode.getText().toString());
                                break;
                            case R.id.menu_archiveCard_recover:
                                showDialog(holder.invitedCode.getText().toString());
                                break;
                        }
                        return false;
                    }
                });//给菜单绑定监听
                //展示菜单
                popupMenu.show();

            }
        });
        holder.invitedCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText("text", holder.invitedCode.getText().toString()));
                Toast.makeText(context, "邀请码已复制到粘贴板", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return holder;
    }

    private void showDialog_delete(final String code) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("要删除此课程吗？");
        dialog.setMessage("此课程及课程内资料将被永久删除");
        dialog.setCancelable(false);

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CourseInfo courseInfo = new CourseInfo();
                courseInfo.setObjectId(code);
                courseInfo.delete(new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if(e==null){

                            ((MainActivity)context).onActivityResult(2,RESULT_OK,null);
                        }else{
                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });

            }
        });

        dialog.show();
    }

    private void showDialog(final String code) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("要恢复课程吗？");
        dialog.setMessage("此课程将纳入主菜单的“课程”中，您可以前往查看");
        dialog.setCancelable(false);

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CourseInfo courseInfo = new CourseInfo();
                courseInfo.setStatus(1);
                courseInfo.update(code, new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            ((MainActivity)context).onActivityResult(2,RESULT_OK,null);
                        } else {
                            Log.i("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                        }
                    }

                });

            }
        });

        dialog.show();

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CourseInfo courseInfo = courseInfos.get(position);
        holder.backgroundColor = courseInfo.getColor();
        holder.courseName.setText(courseInfo.getCourseName());
        holder.className.setText(courseInfo.getClaName());
        if (null == courseInfo.getStudents()) {
            holder.stuCount.setText("0位学生");
        } else {
            holder.stuCount.setText(courseInfo.getStudents().getObjects().size() + "位学生");
        }

        holder.invitedCode.setText(courseInfo.getObjectId());

        holder.cardView.setBackgroundColor(Color.parseColor(holder.backgroundColor));

    }

    @Override
    public int getItemCount() {
        return courseInfos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView courseName, className, stuCount, invitedCode;
        private ImageView imageView;
        private String backgroundColor;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.item_courseCard_cardView);
            courseName = itemView.findViewById(R.id.item_courseCard_courseName);
            className = itemView.findViewById(R.id.item_courseCard_className);
            stuCount = itemView.findViewById(R.id.item_courseCard_stuCount);
            invitedCode = itemView.findViewById(R.id.item_courseCard_invitedNo);
            imageView = itemView.findViewById(R.id.item_courseCard_more);


        }
    }
}
