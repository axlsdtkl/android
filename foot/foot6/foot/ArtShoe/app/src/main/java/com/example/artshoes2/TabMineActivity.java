package com.example.artshoes2;

import android.app.Activity;
import android.os.Bundle;

//我的个人信息页面
public class TabMineActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);

        String desc = String.format("我是%s页面，来自%s",
                "分类", getIntent().getExtras().getString("tag"));

    }
}
