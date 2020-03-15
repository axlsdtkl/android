package com.example.foot;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FootLook extends AppCompatActivity {

    private List<Foot> footList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foot_look);
        //初始化脚的数据
        initFoots();
        FootAdapter adapter = new FootAdapter(this, R.layout.foot_item, footList);//装入装配
        ListView listView = (ListView) findViewById(R.id.list_view);//脚型表单
        listView.setAdapter(adapter);//表单中输入装配的内容
    }

    private void initFoots() {//循环生成脚的图片
        for(int i=1;i<=10;i++) {
            Foot foot1 = new Foot("脚型照片1", R.drawable.foot1);
            footList.add(foot1);
            Foot foot2= new Foot("脚型照片2",R.drawable.foot2);
            footList.add(foot2);
            Foot foot3 = new Foot("脚型照片3", R.drawable.foot3);
            footList.add(foot3);
            Foot foot4 = new Foot("脚型照片4", R.drawable.foot4);
            footList.add(foot4);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//右上角的菜单项
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {//菜单项上的按钮
        switch(item.getItemId()){
            case R.id.add_item:
                Toast.makeText(this,"添加照片成功",Toast.LENGTH_SHORT).show();//屏幕底端的提示
                break;
            case R.id.remove_item:
                Toast.makeText(this,"删除照片成功",Toast.LENGTH_SHORT).show();
                break;
            case R.id.synchro_item:
                Toast.makeText(this,"与服务器同步中",Toast.LENGTH_SHORT).show();
                Toast.makeText(this,"与服务器同步完成",Toast.LENGTH_SHORT).show();
                break;
            case R.id.back_item:
                finish();//返回上一页
                break;
            default:
        }
        return true;
    }
}