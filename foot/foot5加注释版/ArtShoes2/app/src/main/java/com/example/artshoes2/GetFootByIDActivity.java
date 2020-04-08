package com.example.artshoes2;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class GetFootByIDActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getfootbyid);
        Toolbar tl_head = (Toolbar) findViewById(R.id.tl_head);
        //对导航栏进行设置
        tl_head.setTitle("获取脚型");
        setSupportActionBar(tl_head);
        tl_head.setNavigationIcon(R.drawable.ic_back);
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
}
