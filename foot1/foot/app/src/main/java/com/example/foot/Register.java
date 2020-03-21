package com.example.foot;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionbar=getSupportActionBar();
        if(actionbar!=null)actionbar.hide();//隐藏默认布局

        final EditText username = (EditText)findViewById(R.id.username);
        final EditText password = (EditText)findViewById(R.id.password);
        Button buttonsend=(Button)findViewById(R.id.button_send);
        buttonsend.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(Register.this,"您的反馈已发送，感谢您对我们的支持",
                        Toast.LENGTH_SHORT).show();
            }
        });

        Button buttonlogin=(Button)findViewById(R.id.button_register);//登入按钮，下面做了进度条效果，必须开个线程用，不能直接在UI主函数中用
        buttonlogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                final String name = username.getText().toString().trim();
                final String psw = password.getText().toString().trim();
                //显示进度条
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String path="http://192.168.191.1:8080/First13/mustRegister?logname="+name+"&password="+psw;
                        try {
                            try{
                                URL url = new URL(path); //新建url并实例化
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setRequestMethod("GET");//获取服务器数据
                                connection.setReadTimeout(8000);//设置读取超时的毫秒数
                                connection.setConnectTimeout(8000);//设置连接超时的毫秒数
                                InputStream in = connection.getInputStream();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                                String result = reader.readLine();//读取服务器进行逻辑处理后页面显示的数据
                                if (result.equals("register successfully!")){
                                    new Thread(new Runnable(){
                                        int initial=0;
                                        public void run(){
                                            Looper.prepare();
                                            Toast.makeText(Register.this,"You register successfully!",Toast.LENGTH_SHORT).show();
                                            Looper.loop();
                                        }
                                    }).start();
                                    finish();
                                }else if(result.equals("can not register!")){
                                    Looper.prepare();
                                    Toast.makeText(Register.this,"You can not register!!",Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            }catch (MalformedURLException e){}
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
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
