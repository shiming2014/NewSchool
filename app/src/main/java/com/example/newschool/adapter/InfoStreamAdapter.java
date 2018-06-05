package com.example.newschool.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.newschool.R;
import com.example.newschool.activity.PreViewActivity;
import com.example.newschool.activity.StuHomeworkActivity;
import com.example.newschool.bean.HomeworkInfo;
import com.example.newschool.bean.SignInfo;
import com.example.newschool.utils.ColorUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 世铭 on 2018/5/31.
 */

public class InfoStreamAdapter extends RecyclerView.Adapter<InfoStreamAdapter.ViewHolder> {
    private List<HomeworkInfo> homeworkInfos;
    private Context context;

    public InfoStreamAdapter(List<HomeworkInfo> homeworkInfos) {
        this.homeworkInfos = homeworkInfos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_infostream, parent, false);
        final InfoStreamAdapter.ViewHolder holder = new InfoStreamAdapter.ViewHolder(view);

        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final HomeworkInfo homeworkInfo = homeworkInfos.get(position);
        holder.headshot.setBackgroundColor(ColorUtil.nextColor());
        holder.name.setText(homeworkInfo.getTeacherInfo().getTeacherName());
        holder.date.setText(homeworkInfo.getCreatedAt());
        if ("" == homeworkInfo.getTheme())
            holder.theme.setVisibility(View.GONE);
        else
            holder.theme.setText(homeworkInfo.getTheme());
        if (null == homeworkInfo.getTitle())
            holder.title.setVisibility(View.GONE);
        else
            holder.title.setText(homeworkInfo.getTitle());
        if ("" == homeworkInfo.getDescribe())
            holder.subTitle.setVisibility(View.GONE);
        else
            holder.subTitle.setText(homeworkInfo.getDescribe());
        if (null == homeworkInfo.getDeadline())
            holder.deadline.setVisibility(View.GONE);
        else
            holder.deadline.setText("提交期限：" + homeworkInfo.getDeadline().getDate());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StuHomeworkActivity.class);
                intent.putExtra("homeworkId",homeworkInfo.getObjectId());
                intent.putExtra("deadline",homeworkInfo.getDeadline().getDate());
                intent.putExtra("courseId",homeworkInfo.getCourseInfo().getObjectId());
                context.startActivity(intent);
            }
        });
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPop(view,homeworkInfo);
            }
        });
        if (null == homeworkInfo.getFiles())
            holder.attach.setVisibility(View.GONE);
        else
            holder.attach.setText(homeworkInfo.getFiles().size() + " 个附件");
        if (null == homeworkInfo.getDone() || null == homeworkInfo.getNotDone())
            holder.linearLayout.setVisibility(View.GONE);
        else {
            holder.done.setText(homeworkInfo.getDone());
            holder.notDone.setText(homeworkInfo.getNotDone());
        }

    }

    private void showPop(View view,final HomeworkInfo homeworkInfo) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        //将指定的菜单布局进行加载
        popupMenu.getMenuInflater().inflate(R.menu.menu_homework, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_homework_remember:
                        RemenberIt(homeworkInfo);
                        break;
                    case R.id.menu_homework_delete:
                        deleteIt(homeworkInfo);
                        break;
                }
                return false;
            }
        });//给菜单绑定监听
        //展示菜单
        popupMenu.show();
    }

    private void deleteIt(final HomeworkInfo homeworkInfo) {

        HomeworkInfo gameScore = new HomeworkInfo();
        gameScore.setObjectId(homeworkInfo.getObjectId());
        gameScore.delete(new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    homeworkInfos.remove(homeworkInfo);
                    notifyDataSetChanged();
                    Log.i("bmob","成功");
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public static Date StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private void RemenberIt(HomeworkInfo homeworkInfo) {
        Calendar c = Calendar.getInstance();
        c.setTime(StrToDate(homeworkInfo.getCreatedAt()));

        Calendar d = Calendar.getInstance();
        d.setTime(StrToDate(homeworkInfo.getDeadline().getDate()));

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
        Calendar endTime = Calendar.getInstance();
        endTime.set(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH), d.get(Calendar.HOUR_OF_DAY), d.get(Calendar.MINUTE));
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, homeworkInfo.getCourseInfo().getCourseName() + "--" + homeworkInfo.getTitle())
                .putExtra(CalendarContract.Events.DESCRIPTION, homeworkInfo.getDescribe())
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        context.startActivity(intent);


    }

    @Override
    public int getItemCount() {
        return homeworkInfos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView name, date, deadline, title, subTitle, attach, done, notDone;
        private ImageView headshot, more;
        private Button theme;
        private LinearLayout linearLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.item_infoStream_cardView);
            title = itemView.findViewById(R.id.item_infoStream_title);
            headshot = itemView.findViewById(R.id.item_infoStream_img);
            more = itemView.findViewById(R.id.item_infoStream_more);
            name = itemView.findViewById(R.id.item_infoStream_Name);
            date = itemView.findViewById(R.id.item_infoStream_issueDate);
            theme = itemView.findViewById(R.id.item_infoStream_theme);
            deadline = itemView.findViewById(R.id.item_infoStream_deadline);
            subTitle = itemView.findViewById(R.id.item_infoStream_subTite);
            attach = itemView.findViewById(R.id.item_infoStream_attachment);
            done = itemView.findViewById(R.id.item_infoStream_textDone);
            notDone = itemView.findViewById(R.id.item_infoStream_textNot);
            linearLayout = itemView.findViewById(R.id.item_infoStream_DoneOrNot);


        }
    }
}
