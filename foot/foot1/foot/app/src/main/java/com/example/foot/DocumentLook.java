package com.example.foot;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class DocumentLook extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentlook);
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
