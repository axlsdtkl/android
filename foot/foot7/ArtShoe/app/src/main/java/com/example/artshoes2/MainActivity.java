package com.example.artshoes2;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import static com.example.artshoes2.Utils.FileUtils.CreateFootMessageFolder;
import static com.example.artshoes2.Utils.FileUtils.CreateObjFolder;
import static com.example.artshoes2.Utils.FileUtils.CreatePdfFolder;
import static com.example.artshoes2.Utils.FileUtils.CreateSendMessageFolder;

public class MainActivity extends ActivityGroup implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private Bundle mBundle = new Bundle();
    private LinearLayout ll_container, ll_home, ll_pdf, ll_obj,ll_mine;

    @Override
    //创建主页面
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //在本地创建四个文件夹
        CreateFootMessageFolder();//脚型信息文件夹
        CreateObjFolder();//脚型obj文件夹
        CreatePdfFolder();//脚型pdf文件夹
        CreateSendMessageFolder();//通信文件夹
        ll_container = (LinearLayout) findViewById(R.id.ll_container);//显示脚型列表的视图
        ll_home = (LinearLayout) findViewById(R.id.ll_home);
        ll_pdf = (LinearLayout) findViewById(R.id.ll_pdf);
        ll_obj = (LinearLayout) findViewById(R.id.ll_obj);
        ll_mine=(LinearLayout)findViewById(R.id.ll_mine);
        ll_home.setOnClickListener(this);
        ll_pdf.setOnClickListener(this);
        ll_obj.setOnClickListener(this);
        ll_mine.setOnClickListener(this);
        mBundle.putString("tag", TAG);
        changeContainerView(ll_home);//刚登录成功时默认是主页面
    }

    @Override
    public void onClick(View v) {//如果有相应点击，则换成相应界面
        if (v.getId()== R.id.ll_home || v.getId()== R.id.ll_pdf || v.getId()== R.id.ll_obj||v.getId()== R.id.ll_mine) {
            changeContainerView(v);
        }
    }
    //定义点击标签栏跳转到不同页面的事件
    private void changeContainerView(View v) {
        ll_home.setSelected(false);//标为未选
        ll_pdf.setSelected(false);//标为未选
        ll_obj.setSelected(false);//标为未选
        ll_mine.setSelected(false);//标为未选
        v.setSelected(true);//当前页面标为已选
        if (v == ll_home) {//转到相应界面
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
        View v = getLocalActivityManager().startActivity(label, intent).getDecorView();//v为当前要显示的相应信息的表视图
        v.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll_container.addView(v);
    }

}

