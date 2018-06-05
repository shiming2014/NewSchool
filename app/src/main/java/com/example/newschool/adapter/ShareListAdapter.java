package com.example.newschool.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.newschool.R;
import com.example.newschool.bean.ShareFile;
import com.example.newschool.utils.ColorUtil;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteBatchListener;
import cn.bmob.v3.listener.UpdateListener;

import static cn.bmob.v3.BmobConstants.TAG;

/**
 * Created by 世铭 on 2018/6/4.
 */

public class ShareListAdapter extends RecyclerView.Adapter<ShareListAdapter.ViewHolder> {
    private List<ShareFile> shareFiles;
    private Context context;

    public ShareListAdapter(List<ShareFile> shareFiles) {
        this.shareFiles = shareFiles;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_sharelist, parent, false);
        final ShareListAdapter.ViewHolder holder = new ShareListAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ShareFile shareFile = shareFiles.get(position);
        holder.name.setText(new StringBuilder().append("#").append(shareFile.getStudentInfo().getStuNo()).append("#").append(shareFile.getStudentInfo().getStuName()).toString());
        holder.time.setText(shareFile.getCreatedAt());
        holder.theme.setText(shareFile.getTitle());
        holder.linearLayout.setBackgroundColor(ColorUtil.nextColor());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.recyclerView.getVisibility() == View.GONE)
                    holder.recyclerView.setVisibility(View.VISIBLE);
                else
                    holder.recyclerView.setVisibility(View.GONE);
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(BmobUser.getCurrentUser().getObjectId().equals(shareFile.getStudentInfo().getObjectId())){
                    showDialog(shareFile.getObjectId(),shareFile.getUrls(),shareFile,shareFiles);

                }else {
                    Snackbar.make(view,"只有管理员和本人才能删除此分享",Snackbar.LENGTH_LONG).show();
                }




                return true;
            }
        });
//        GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
        FileListAdapter3 adapter3 = new FileListAdapter3(shareFile.getUrls());
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setAdapter(adapter3);
    }
    private void showDialog(final String shareId, final List<String> urls, final ShareFile shareFile, final List<ShareFile> shareFiles) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("删除此分享吗？");
        dialog.setMessage("此分享列表将从服务器删除");
        dialog.setCancelable(false);

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shareFiles.remove(shareFile);
                notifyDataSetChanged();
                final ShareFile shareFile=new ShareFile();
                shareFile.setObjectId(shareId);
                shareFile.delete(new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Log.i("bmob","成功");

                            BmobFile.deleteBatch(((String[])urls.toArray()), new DeleteBatchListener() {

                                @Override
                                public void done(String[] failUrls, BmobException e) {
                                    if(e==null){

                                        Log.i(TAG, "done: "+"全部删除成功");
                                    }else{
                                        if(failUrls!=null){
                                            Log.i(TAG, "done: 删除失败个数："+failUrls.length+","+e.toString());
                                        }else{
                                            Log.i(TAG, "done: 全部文件删除失败："+e.getErrorCode()+","+e.toString());
                                        }
                                    }
                                }
                            });



                        }else{
                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });




            }
        });

        dialog.show();

    }
    @Override
    public int getItemCount() {
        return shareFiles.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView name, time, theme;
        private LinearLayout linearLayout;
        private RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.shareList_cardView);
            name = itemView.findViewById(R.id.shareList_name);
            time = itemView.findViewById(R.id.shareList_time);
            theme = itemView.findViewById(R.id.shareList_theme);
            linearLayout = itemView.findViewById(R.id.shareList_linearLayout);
            recyclerView = itemView.findViewById(R.id.shareList_recyclerView);
        }
    }
}
