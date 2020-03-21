package com.example.foot;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ModelLook extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_look);
        ActionBar actionbar=getSupportActionBar();
        if(actionbar!=null)actionbar.hide();//隐藏默认布局
        
        Button buttonback=(Button)findViewById(R.id.button_back);//后退键
        buttonback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

}
