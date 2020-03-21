package com.example.artshoes2;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.core.app.ActivityCompat;

import static com.example.artshoes2.Utils.FileUtils.CreateFootMessageFolder;
import static com.example.artshoes2.Utils.FileUtils.CreateObjFolder;
import static com.example.artshoes2.Utils.FileUtils.CreatePdfFolder;
import static com.example.artshoes2.Utils.FileUtils.CreateSendMessageFolder;

public class MainActivity extends ActivityGroup implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private Bundle mBundle = new Bundle();
    private LinearLayout ll_container, ll_home, ll_pdf, ll_obj,ll_mine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission(MainActivity.this);
        CreateFootMessageFolder();
        CreateObjFolder();
        CreatePdfFolder();
        CreateSendMessageFolder();
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        ll_home = (LinearLayout) findViewById(R.id.ll_home);
        ll_pdf = (LinearLayout) findViewById(R.id.ll_pdf);
        ll_obj = (LinearLayout) findViewById(R.id.ll_obj);
        ll_mine=(LinearLayout)findViewById(R.id.ll_mine);
        ll_home.setOnClickListener(this);
        ll_pdf.setOnClickListener(this);
        ll_obj.setOnClickListener(this);
        ll_mine.setOnClickListener(this);
        mBundle.putString("tag", TAG);
        changeContainerView(ll_home);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.ll_home || v.getId()== R.id.ll_pdf || v.getId()== R.id.ll_obj||v.getId()== R.id.ll_mine) {
            changeContainerView(v);
        }
    }
    //定义点击标签栏跳转到不同页面的事件
    private void changeContainerView(View v) {
        ll_home.setSelected(false);
        ll_pdf.setSelected(false);
        ll_obj.setSelected(false);
        ll_mine.setSelected(false);
        v.setSelected(true);
        if (v == ll_home) {
            toActivity("home", TabHomeActivity.class);
        } else if (v == ll_pdf) {
            toActivity("pdf", TabPdfActivity.class);
        } else if (v == ll_obj) {
            toActivity("obj", TabObjActivity.class);
        } else if (v==ll_mine){
            toActivity("mine", TabMineActivity.class);
        }
    }

    private void toActivity(String label, Class<?> cls) {
        Intent intent = new Intent(this, cls).putExtras(mBundle);
        ll_container.removeAllViews();
        View v = getLocalActivityManager().startActivity(label, intent).getDecorView();
        v.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll_container.addView(v);
    }
    private void checkPermission(Activity activity) {
        // Storage Permissions
        final int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(MainActivity.this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}

