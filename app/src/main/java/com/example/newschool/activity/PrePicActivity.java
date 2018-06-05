package com.example.newschool.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.newschool.R;

public class PrePicActivity extends AppCompatActivity {
    private ImageView webView;
    private String imgUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_pic);
        initView();
    }

    private void initView() {
        imgUrl=getIntent().getStringExtra("url");
        webView=findViewById(R.id.PrePicActivity_webView);
        Glide.with(this).load(imgUrl).into(webView);
    }
}
