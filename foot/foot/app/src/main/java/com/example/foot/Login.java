package com.example.foot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    private ProgressBar progressBar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        Button buttonlogin=(Button)findViewById(R.id.button_login);//登入按钮，下面做了进度条效果，必须开个线程用，不能直接在UI主函数中用
        buttonlogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //显示进度条
                progressBar.setVisibility(View.VISIBLE);
                //下面的必须单独开进程，如果Thread.sleep()放在放函数UI里面，会出现无法更新的情况
                new Thread(new Runnable(){
                    int initial=0;
                    public void run(){
                        while(initial<progressBar.getMax()){
                            progressBar.setProgress(initial+=3);
                            try {
                                Thread.sleep(10);//线程休眠10毫秒
                            }catch(InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                        Intent intent=new Intent(Login.this,MainPage.class);
                        startActivity(intent);//登入成功，转入主页面
                    }
                }).start();
            }
        });

        Button buttonhelp=(Button)findViewById(R.id.button_help);//帮助按钮获得提示
        buttonhelp.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(Login.this,"本版本为方案二轻量级版本，" +
                                "适合个人用户，如需管理多脚型，请下载方案一企业版",
                        Toast.LENGTH_LONG).show();
            }
        });

        Button buttonregister=(Button)findViewById(R.id.button_register);//注册按钮进入注册页面
        buttonregister.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });

    }
}
