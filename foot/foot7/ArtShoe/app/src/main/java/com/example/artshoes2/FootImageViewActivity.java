//这里是观看图片的界面
package com.example.artshoes2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;

public class FootImageViewActivity extends AppCompatActivity {
    private ImageView img_footpic;
    private String footimage_uri;
    private FootImage footImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footimageview);
        img_footpic=findViewById(R.id.img_footpic);
        Toolbar tl_head = (Toolbar) findViewById(R.id.tl_head);
        //对导航栏进行设置
        tl_head.setTitle("");
        setSupportActionBar(tl_head);
        tl_head.setNavigationIcon(R.drawable.ic_back);
        Bundle bundle=getIntent().getExtras();
        img_footpic.setImageURI(Uri.fromFile(new File(bundle.getString("ImagePath"))));//将当前图片位置显示为"ImagePath"表示地址的图片
//        try {
//            img_footpic.setImageBitmap(PicUnits.getBitmapFormUri(FootImageViewActivity.this,Uri.parse(bundle.getString("footimage"))));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_footimageview, menu);
        ToolBarUnits.setIconVisable(menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.menu_delete) {//如果为删除的话
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);//返回一个RESULT_OK的标记，让外层界面根据这个返回值进行删除操作
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
