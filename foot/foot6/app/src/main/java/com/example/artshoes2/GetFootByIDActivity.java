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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.artshoes2.user.UserInformation;

public class GetFootByIDActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int WRITE_PERMISSION_CODE = 1000;
    //文件下载链接
    //private String url = "http://cloud.video.taobao.com/play/u/2577498496/p/1/e/6/t/1/50016582633.mp4";
    private String url = UserInformation.httpurl+"/files/";
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
        tl_head.setTitle("获取脚型");
        setSupportActionBar(tl_head);
        tl_head.setNavigationIcon(R.drawable.ic_back);

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
        } else if (id == R.id.menu_uploadid) {
            Toast.makeText(GetFootByIDActivity.this,"你点击了上传键",Toast.LENGTH_SHORT).show();
        }
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
                String urltarget="";
                if(UserInformation.downloadid==1)urltarget=url+"1.pdf";
                else if(UserInformation.downloadid==2)urltarget=url+"2.obj";
                downLoadBinder.startDownLoad(urltarget);
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
