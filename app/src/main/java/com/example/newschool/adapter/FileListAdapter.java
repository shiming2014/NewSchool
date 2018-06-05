package com.example.newschool.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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

import java.io.File;
import java.util.List;

/**
 * Created by 世铭 on 2018/5/28.
 */

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {
    private List<String> shareFiles;
    private Context context;

    public FileListAdapter(List<String> shareFiles) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_file_name, parent, false);
        final FileListAdapter.ViewHolder holder = new FileListAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String s = shareFiles.get(position);
        String[] temp = s.split("\\.");
        switch (temp[temp.length - 1]) {
            case "pdf":
            case "PDF":
                Glide.with(context).load(R.drawable.icon_file_pdf).into(holder.imageView);
                break;
            case "DOC":
            case "doc":
            case "docx":
            case "DOCX":
                Glide.with(context).load(R.drawable.icon_file_doc).into(holder.imageView);
                break;
            case "ppt":
            case "pptx":
            case "PPT":
            case "PPTX":
                Glide.with(context).load(R.drawable.icon_file_ppt).into(holder.imageView);
                break;
            case "xls":
            case "xlsx":
            case "XLS":
            case "XLSX":
                Glide.with(context).load(R.drawable.icon_file_xls).into(holder.imageView);
                break;
            case "png":
            case "jpg":
            case "jpeg":
            case "bmp":
            case "gif":
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
                Glide.with(context).load(Uri.fromFile(new File(s))).into(holder.imageView);
                break;

            case "rar":
            case "zip":
            case "7z":
                Glide.with(context).load(R.drawable.ic_zip_icon).into(holder.imageView);
                break;
            case "txt":
                Glide.with(context).load(R.drawable.txt).into(holder.imageView);
                break;
            default:
                Glide.with(context).load(R.drawable.icon_file_unknown).into(holder.imageView);
        }
        String[] title = s.split("/");
        holder.title.setText(title[title.length - 1]);

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showTint((Activity) context, position);
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
