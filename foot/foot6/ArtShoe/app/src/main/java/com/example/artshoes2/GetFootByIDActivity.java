package com.example.artshoes2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.artshoes2.user.UserInformation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

public class GetFootByIDActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText edt_id;
    private String url;
    private static final int WRITE_PERMISSION_CODE = 1000;
    //文件下载链接
    //private String url = "http://cloud.video.taobao.com/play/u/2577498496/p/1/e/6/t/1/50016582633.mp4";
    private Context mContext;

    private Button btnStartDownLoad, btnPauseDownLoad, btnCancelDownLoad;

    private DownLoadService.DownLoadBinder downLoadBinder;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            downLoadBinder = (DownLoadService.DownLoadBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getfootbyid);
        Toolbar tl_head = (Toolbar) findViewById(R.id.tl_head);
        //对导航栏进行设置
        if(UserInformation.downloadid==1){
            tl_head.setTitle("获取脚型参数报告");
        }else if(UserInformation.downloadid==2){
            tl_head.setTitle("获取脚型三维模型");
        }

        setSupportActionBar(tl_head);
        tl_head.setNavigationIcon(R.drawable.ic_back);
        edt_id=findViewById(R.id.edt_id);

        baseDataInit();
        bindViews();
        viewsAddListener();
        viewsDataInit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_getfootbyid, menu);
        ToolBarUnits.setIconVisable(menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
//        } else if (id == R.id.menu_uploadid) {
//            Toast.makeText(GetFootByIDActivity.this,"你点击了上传键",Toast.LENGTH_SHORT).show();
//        }
        return super.onOptionsItemSelected(item);
    }

    private void baseDataInit() {
        mContext = this;
    }

    private void bindViews() {
        btnStartDownLoad = findViewById(R.id.Main_btnStartDownLoad);
        btnPauseDownLoad = findViewById(R.id.Main_btnPauseDownLoad);
        btnCancelDownLoad = findViewById(R.id.Main_btnCancelDownLoad);

    }

    private void viewsAddListener() {
        btnStartDownLoad.setOnClickListener(this);
        btnPauseDownLoad.setOnClickListener(this);
        btnCancelDownLoad.setOnClickListener(this);
    }

    private void viewsDataInit() {
        Intent intent = new Intent(mContext, DownLoadService.class);
        startService(intent);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View view) {
        if (downLoadBinder == null) {
            Toast.makeText(mContext, "下载服务创建失败！", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (view.getId()) {
            case R.id.Main_btnStartDownLoad: {
                if(edt_id.getText().toString().trim().length()==6){
                    final String id=edt_id.getText().toString().trim();
                    final CountDownLatch latch= new CountDownLatch(1);//使用java并发库concurrent

                    new Thread(new Runnable() {
                        @Override
                        public void run() {//开启线程用来注册
                            String path=UserInformation.httpurl+"/GetUrlById?id="+id;
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
                                    UserInformation.path=result;
                                }catch (MalformedURLException e){}
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            latch.countDown();
                        }
                    }).start();

                    try {
                        latch.await();//阻塞当前线程直到latch中数值为零才执行
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    url =UserInformation.httpurl+UserInformation.path;
                    String urltarget;
                    if(UserInformation.downloadid==1)urltarget=url+".pdf";
                    else urltarget=url+".obj";

                    downLoadBinder.startDownLoad(urltarget);
                    Toast.makeText(GetFootByIDActivity.this,"脚型已重建完成，开始下载",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.Main_btnPauseDownLoad: {
                downLoadBinder.pauseDownLoad();
                break;
            }
            case R.id.Main_btnCancelDownLoad: {
                downLoadBinder.cancelDownLoad();
                break;
            }
            default:break;
        }
    }
}
