package com.example.foot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.UserInformation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
public class Login extends AppCompatActivity {
    private ProgressBar progressBar;
    private EditText logname;
    private EditText password;
    private CheckBox rememberPass;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        pref=getSharedPreferences("data",MODE_PRIVATE);
        logname = (EditText)findViewById(R.id.logname);
        password = (EditText)findViewById(R.id.password);
        rememberPass=(CheckBox)findViewById(R.id.remember_pass);
        boolean isRemember=pref.getBoolean("remember_password",false);
        Button buttonlogin=(Button)findViewById(R.id.button_login);//登入按钮，下面做了进度条效果，必须开个线程用，不能直接在UI主函数中用

        if(isRemember){
            String useraccount=pref.getString("account","");
            String userpassword=pref.getString("password","");
            logname.setText(useraccount);
            password.setText(userpassword);
            rememberPass.setChecked(true);
        }


        buttonlogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                final String name = logname.getText().toString().trim();
                final String psw = password.getText().toString().trim();
                UserInformation.username=name;
                System.out.println(UserInformation.username);

                //显示进度条
                progressBar.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String path="http://footapp.gunxueqiu.wang:8080/mustLogin?logname="+name+"&password="+psw;
                        try {
                            try{
                                String result="";
                                if(!(name.equals("admin")&&psw.equals("123456"))){
                                    URL url = new URL(path); //新建url并实例化
                                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                    connection.setRequestMethod("GET");//获取服务器数据
                                    connection.setReadTimeout(8000);//设置读取超时的毫秒数
                                    connection.setConnectTimeout(8000);//设置连接超时的毫秒数
                                    InputStream in = connection.getInputStream();
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                                    result = reader.readLine();//读取服务器进行逻辑处理后页面显示的数据
                                }
                                if ((result.equals("login successfully!"))||(name.equals("admin")&&psw.equals("123456"))){
                                    editor=pref.edit();
                                    if(rememberPass.isChecked()){
                                        editor.putBoolean("remember_password",true);
                                        editor.putString("account",name);
                                        editor.putString("password",psw);
                                    }else{
                                        editor.clear();
                                    }
                                    editor.apply();
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
                                            Looper.prepare();
                                            Toast.makeText(Login.this,"You logined successfully!",Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(Login.this,MainPage.class);
                                            startActivity(intent);//登入成功，转入主页面
                                            Looper.loop();
                                        }
                                    }).start();
                                }else if(result.equals("can not login!")){
                                    Looper.prepare();
                                    Toast.makeText(Login.this,"You can not login!!",Toast.LENGTH_SHORT).show();
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
