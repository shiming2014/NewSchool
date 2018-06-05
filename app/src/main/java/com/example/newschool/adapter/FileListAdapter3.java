package com.example.newschool.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newschool.R;
import com.example.newschool.activity.PreOfficeActivity;
import com.example.newschool.activity.PrePicActivity;
import com.example.newschool.activity.PreViewActivity;


import java.util.List;

/**
 * Created by 世铭 on 2018/5/28.
 */

public class FileListAdapter3 extends RecyclerView.Adapter<FileListAdapter3.ViewHolder> {
    private List<String> shareFiles;
    private Context context;

    public FileListAdapter3(List<String> shareFiles) {
        this.shareFiles = shareFiles;
    }

    private void showTint(final Activity activity, final int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("注意");
        dialog.setMessage("点击确定，您将取消分享该文件");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shareFiles.remove(position);
                notifyDataSetChanged();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_file_, parent, false);
        final FileListAdapter3.ViewHolder holder = new FileListAdapter3.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String s = shareFiles.get(position);
        String[] temp = s.split("\\.");
        switch (temp[temp.length - 1]) {
            case "pdf":
            case "PDF":
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context, PreOfficeActivity.class);
                        intent.putExtra("url",s);
                        context.startActivity(intent);
                    }
                });
                Glide.with(context).load(R.drawable.icon_file_pdf).into(holder.imageView);
                break;
            case "DOC":
            case "doc":
            case "docx":
            case "DOCX":
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context, PreOfficeActivity.class);
                        intent.putExtra("url",s);
                        context.startActivity(intent);
                    }
                });
                Glide.with(context).load(R.drawable.icon_file_doc).into(holder.imageView);
                break;
            case "ppt":
            case "pptx":
            case "PPT":
            case "PPTX":
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context, PreOfficeActivity.class);
                        intent.putExtra("url",s);
                        context.startActivity(intent);
                    }
                });
                Glide.with(context).load(R.drawable.icon_file_ppt).into(holder.imageView);
                break;
            case "xls":
            case "xlsx":
            case "XLS":
            case "XLSX":
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context, PreOfficeActivity.class);
                        intent.putExtra("url",s);
                        context.startActivity(intent);
                    }
                });
                Glide.with(context).load(R.drawable.icon_file_xls).into(holder.imageView);
                break;
            case "png":
            case "jpg":
            case "jpeg":
            case "bmp":
            case "gif":
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context, PrePicActivity.class);
                        intent.putExtra("url",s);
                        context.startActivity(intent);
                    }
                });
                Glide.with(context).load(s).into(holder.imageView);
                break;
            case "mp4":
            case "wmv":
            case "asf":
            case "asx":
            case "rm":
            case "rmvb":
            case "3gp":
            case "mov":
            case "avi":
            case "flv":
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context, PreViewActivity.class);
                        intent.putExtra("url",s);
                        context.startActivity(intent);
                    }
                });
                Glide.with(context).load(R.drawable.video).into(holder.imageView);
                break;
            case "rar":
            case "zip":
            case "7z":
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(s);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);
                    }
                });
                Glide.with(context).load(R.drawable.ic_zip_icon).into(holder.imageView);
                break;
            case "txt":
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(s);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);
                    }
                });
                Glide.with(context).load(R.drawable.txt).into(holder.imageView);
                break;
            default:
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(s);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);
                    }
                });
                Glide.with(context).load(R.drawable.icon_file_unknown).into(holder.imageView);
        }
        String[] title = s.split("/");
        holder.title.setText(title[title.length - 1]);

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Uri uri = Uri.parse(s);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
                return false;
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
            cardView = itemView.findViewById(R.id.item_file_cardView);
            title = itemView.findViewById(R.id.item_file_title);
            imageView = itemView.findViewById(R.id.item_file_img);


        }
    }
}
