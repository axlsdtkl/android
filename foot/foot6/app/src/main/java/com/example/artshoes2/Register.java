package com.example.artshoes2;

import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.artshoes2.user.UserInformation;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionbar=getSupportActionBar();
        if(actionbar!=null)actionbar.hide();//隐藏默认布局

        final EditText username = (EditText)findViewById(R.id.username);
        final EditText password = (EditText)findViewById(R.id.password);
        Button buttonlogin=(Button)findViewById(R.id.button_register);
        buttonlogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){//监听注册按键
                final String name = username.getText().toString().trim();//用户名
                final String psw = password.getText().toString().trim();//密码
                //显示进度条
                new Thread(new Runnable() {
                    @Override
                    public void run() {//开启线程用来注册
                        String path=UserInformation.httpurl+"/mustRegister?logname="+name+"&password="+psw;
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
                                if (result.equals("注册成功!")){
                                    new Thread(new Runnable(){
                                        int initial=0;
                                        public void run(){
                                            Looper.prepare();
                                            Toast.makeText(Register.this,"注册成功!",Toast.LENGTH_SHORT).show();
                                            Looper.loop();
                                        }
                                    }).start();
                                    finish();
                                }else if(result.equals("注册失败!")){
                                    Looper.prepare();
                                    Toast.makeText(Register.this,"注册失败!",Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }else if(result.equals("注册失败，用户已存在!")){
                                    Looper.prepare();
                                    Toast.makeText(Register.this,"注册失败，用户已存在!",Toast.LENGTH_SHORT).show();
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

        Button buttonback=(Button)findViewById(R.id.button_back);//后退键，即回登录界面
        buttonback.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });
    }
}
