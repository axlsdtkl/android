package com.example.foot;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionbar=getSupportActionBar();
        if(actionbar!=null)actionbar.hide();//隐藏默认布局

        Button buttonsend=(Button)findViewById(R.id.button_send);
        buttonsend.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(Register.this,"您的反馈已发送，感谢您对我们的支持",
                        Toast.LENGTH_SHORT).show();
            }
        });
        Button buttonregister=(Button)findViewById(R.id.button_register);
        buttonregister.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(Register.this,"账号注册成功",
                        Toast.LENGTH_SHORT).show();
            }
        });
        Button buttonback=(Button)findViewById(R.id.button_back);
        buttonback.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });
    }
}
