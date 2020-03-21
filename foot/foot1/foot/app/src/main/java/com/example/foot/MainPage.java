package com.example.foot;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        ActionBar actionbar=getSupportActionBar();
        if(actionbar!=null)actionbar.hide();//隐藏默认布局
        Toast.makeText(MainPage.this,"登入成功",
                Toast.LENGTH_SHORT).show();
        //下面是四个主要按键
        Button buttonfootphoto=(Button)findViewById(R.id.button_footphoto);//打开脚型管理界面
        buttonfootphoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, FootLook.class);
                startActivity(intent);
            }
        });

        Button button3dmodel=(Button)findViewById(R.id.button_3dmodel);//打开脚型三维重建界面
        button3dmodel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, ModelLook.class);
                startActivity(intent);
            }
        });

        Button buttontestdocument=(Button)findViewById(R.id.button_testdocument);//打开文件管理界面
        buttontestdocument.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, DocumentLook.class);
                startActivity(intent);
            }
        });

        Button buttonpersonalinfo=(Button)findViewById(R.id.button_personalinfo);//打开个人管理因而
        buttonpersonalinfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, PersonalPage.class);
                startActivity(intent);
            }
        });

        Button buttonpback=(Button)findViewById(R.id.button_back);//后退键
        buttonpback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

}
