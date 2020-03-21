package com.example.foot;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PersonalPage extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_page);
        ActionBar actionbar=getSupportActionBar();
        if(actionbar!=null)actionbar.hide();//隐藏默认布局

        Button buttonback=(Button)findViewById(R.id.button_back);//后退键
        buttonback.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });
        Button buttonstore=(Button)findViewById(R.id.button_store);//保存按键
        buttonstore.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(PersonalPage.this,"保存成功",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
