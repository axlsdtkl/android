//登录界面
package com.example.artshoes2;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.artshoes2.user.UserInformation;

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
    private int usernamelength=0;
    private int pswlength=0;
    Button buttonlogin;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission(Login.this);
        setContentView(R.layout.activity_login);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        pref=getSharedPreferences("data",MODE_PRIVATE);//这个是本地的文件，用来存储当前app的一些信息
        logname = (EditText)findViewById(R.id.logname);
        password = (EditText)findViewById(R.id.password);
        rememberPass=(CheckBox)findViewById(R.id.remember_pass);
        boolean isRemember=pref.getBoolean("remember_password",false);//读取标记，是否有记住密码选项
        buttonlogin=(Button)findViewById(R.id.button_login);//登入按钮，下面做了进度条效果，必须开个线程用，不能直接在UI主函数中用
        buttonlogin.setEnabled(false);
        logname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                usernamelength=s.toString().trim().length();
                if(pswlength!=0&&usernamelength!=0){
                    buttonlogin.setEnabled(true);
                }else{
                    buttonlogin.setEnabled(false);
                }
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pswlength=s.toString().trim().length();
                if(pswlength!=0&&usernamelength!=0){
                    buttonlogin.setEnabled(true);
                }else{
                    buttonlogin.setEnabled(false);
                }
            }
        });
        if(isRemember){//如果已经勾选了记住密码
            String useraccount=pref.getString("account","");//则选出相应的账号与密码
            String userpassword=pref.getString("password","");
            logname.setText(useraccount);//将当前的界面进行用户名填充
            password.setText(userpassword);//将当前的界面进行密码填充
            rememberPass.setChecked(true);//显示成记住密码
        }


        buttonlogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){//监听按键
                final String name = logname.getText().toString().trim();//从样式那里读入当前用户名，并去掉首尾空格
                final String psw = password.getText().toString().trim();//同上，密码
                UserInformation.username=name;//当前app全局变量记录下这个用户名，方便之后上传
                System.out.println(UserInformation.username);

                //显示进度条
                progressBar.setVisibility(View.VISIBLE);//进度条显示
                new Thread(new Runnable() {
                    @Override
                    public void run() {     //开启线程
                        String path=UserInformation.httpurl+"/mustLogin?logname="+name+"&password="+psw;//发送用户名与密码信息给服务器
                        try {
                            try{
                                String result="";
                                if(!(name.equals("admin")&&psw.equals("123456"))){      //如果不是本地管理员离线密码的话， （方便调试，不上网也能登录）
                                    URL url = new URL(path); //新建url并实例化
                                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                    connection.setRequestMethod("GET");//获取服务器数据
                                    connection.setReadTimeout(8000);//设置读取超时的毫秒数
                                    connection.setConnectTimeout(8000);//设置连接超时的毫秒数
                                    InputStream in = connection.getInputStream();
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                                    result = reader.readLine();//读取服务器进行逻辑处理后页面显示的数据，字符串形式
                                }
                                if ((result.equals("login successfully!"))||(name.equals("admin")&&psw.equals("123456"))){//如果返回登录成功或者直接是本地离线万能密码
                                    editor=pref.edit();//开启本地SharedPreferences的写入
                                    if(rememberPass.isChecked()){//如果记住密码逻辑开启的话
                                        editor.putBoolean("remember_password",true);//记住密码标为true
                                        editor.putString("account",name);//写入用户名
                                        editor.putString("password",psw);//写入密码
                                    }else{
                                        editor.clear();//登录成功时如果记住密码没选的话，则清除本地的所有记录
                                    }
                                    editor.apply();//editor应用
                                    new Thread(new Runnable(){
                                        int initial=0;//开启线程用来显示登录进度条
                                        public void run(){
                                            while(initial<progressBar.getMax()){
                                                progressBar.setProgress(initial+=3);//每次进度增加3，最大为100
                                                try {
                                                    Thread.sleep(10);//线程休眠10毫秒
                                                }catch(InterruptedException e){
                                                    e.printStackTrace();
                                                }
                                            }
                                            Looper.prepare();
//                                            Toast.makeText(Login.this,"You logined successfully!",Toast.LENGTH_SHORT).show();//显示登录成功
                                            Intent intent=new Intent(Login.this,MainActivity.class);//跳转页面到主界面
                                            startActivity(intent);//登入成功，转入主页面
                                            Looper.loop();
                                        }
                                    }).start();
                                }else if(result.equals("can not login!")){//否则表示有误，输出不能登录
                                    Looper.prepare();
                                    Toast.makeText(Login.this,"您的用户名或者密码错误",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Login.this,"本版本为方案一企业版本，" +
                                "方案二为个人测试版",
                        Toast.LENGTH_LONG).show();
            }
        });

        Button buttonregister=(Button)findViewById(R.id.button_register);//注册按钮进入注册页面
        buttonregister.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(Login.this,Register.class);//将从登录界面跳转到注册界面
                startActivity(intent);
            }
        });

    }
    private void checkPermission(Activity activity) {//开启本地的照片读取与写入权限
        // Storage Permissions
        final int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,//读内存权限
                Manifest.permission.WRITE_EXTERNAL_STORAGE};//写内存权限

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(Login.this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(Login.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
