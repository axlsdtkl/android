//增加新脚型界面
package com.example.artshoes2;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.artshoes2.Utils.TimeUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.artshoes2.Utils.FileUtils.SaveFootToTxt;

public class FootCreateActivity extends AppCompatActivity {
    private FootImage footimage;
    private GridView gv_footimage;
    private EditText edt_name;
    private RadioGroup rg_sex;
    private RadioButton rb_male;
    private RadioButton rb_female;
    private RadioGroup rg_LorRfoot;
    private RadioButton rb_left;
    private RadioButton rb_right;
    public static final int CHOOSE_PTHOTO=0;
    public static final int VIEW_PHOTO=1;
    private final static String TAG = "FootCreateActivity";
    private List<FootImage> footimageList=new ArrayList<FootImage>();
    private FootImageAdapter adapter;
    private String name;
    private int sex=0;
    private int leftorightfoot=0;
    private FootImage camera=new FootImage(" ");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footcreate);
        gv_footimage=findViewById(R.id.gv_footimage);
        adapter=new FootImageAdapter(FootCreateActivity.this, R.layout.footimage_item,footimageList);
        gv_footimage.setAdapter(adapter);
        Toolbar tl_head = (Toolbar) findViewById(R.id.tl_head);
        edt_name=findViewById(R.id.edt_name);
        rg_sex=findViewById(R.id.rg_sex);
        rb_male=findViewById(R.id.male);
        rb_female=findViewById(R.id.female);
        rg_LorRfoot=findViewById(R.id.rg_foot);
        rb_left=findViewById(R.id.left);
        rb_right=findViewById(R.id.right);
        //对导航栏进行设置
        tl_head.setTitle("脚型信息");
        setSupportActionBar(tl_head);//设置标题栏
        tl_head.setNavigationIcon(R.drawable.ic_back);//设置后退键
        footimageList.add(camera);//增加空地址，在显示的时候会自动填充为加号的图片
        gv_footimage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                footimage=footimageList.get(position);
                if(footimage.getFootImagePath()==" "){//如果点击的是加号的话
                    Intent intentFromGallery;
                    intentFromGallery = new Intent(Intent.ACTION_GET_CONTENT);
                    intentFromGallery.setType("image/*");
                    startActivityForResult(intentFromGallery, CHOOSE_PTHOTO);//进入选择图片界面
                }else {
                    String footimage_path = footimage.footimage_path;
//                Toast.makeText(FootAlterActivity.this,"你点击了照片"+footimagepath,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FootCreateActivity.this, FootImageViewActivity.class);
                    intent.putExtra("ImagePath", footimage_path);//在ImagePath这个对象中放入当前的路径地址
                    startActivityForResult(intent, VIEW_PHOTO);//进入观看图片界面
                }
            }
        });
        //添加性别单选监听事件。
        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId== R.id.male){
                    sex=1;
                }else{
                    sex=2;
                }
            }
        });
        //添加左右脚单选监听事件。
        rg_LorRfoot.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId== R.id.left){
                    leftorightfoot=1;
                }else{
                    leftorightfoot=2;
                }
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_footmessage, menu);//右上角的菜单扩展
        ToolBarUnits.setIconVisable(menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.menu_save) {//如果为存储的话
            if(edt_name.getText().toString().trim().length()<1||sex==0||leftorightfoot==0){//如果不合法的话
                if(edt_name.getText().toString().trim().length()<1){
                    Toast.makeText(FootCreateActivity.this,"请输入名字",Toast.LENGTH_SHORT).show();
                }else if(sex==0){
                    Toast.makeText(FootCreateActivity.this,"请选择性别",Toast.LENGTH_SHORT).show();
                }else if(leftorightfoot==0){
                    Toast.makeText(FootCreateActivity.this,"请选择左右脚",Toast.LENGTH_SHORT).show();
                }
            }else {//合法的话
                footimageList.remove(camera);
                Foot foot = new Foot(edt_name.getText().toString(), sex, leftorightfoot, footimageList, R.drawable.file_foot);
                foot.reNewFileName();
                foot.datechange = TimeUtils.getNowDateTime("yyyy-MM-dd HH:mm:ss");
//                Toast.makeText(FootCreateActivity.this, "保存", Toast.LENGTH_SHORT).show();
                SaveFootToTxt(foot);//将当前脚型信息保存成txt文件
                Intent intent = new Intent();
                intent.putExtra("foot_data", foot);//设置名字为foot_data的返回foot类
                setResult(RESULT_OK, intent);//返回RESULT_OK表示有创建成功的
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_PTHOTO://如果为选择图片功能的返回值
                if (resultCode == RESULT_OK) {//如果选择了的话
                    handleImageOnKitKat(data);//根据数据保存
                }

                break;
            case VIEW_PHOTO://如果为看图片返回的话
                if(resultCode==RESULT_OK){//如果为RESULT_OK，说明进行了删除操作
                    Iterator<FootImage> iterator = footimageList.iterator();
                    while (iterator.hasNext()) {
                        FootImage value = iterator.next();
                        if (value.equals(footimage)) {//删除当前图片
                            iterator.remove();
                        }
                    }
                    adapter.notifyDataSetChanged();//重新显示列表图
                }
            default:
                break;
        }
    }
    private String getImagePath(Uri uri,String selection){
        String path=null;
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];
                String selection= MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath=getImagePath(uri,null);
        }
        setPicForGridView(imagePath);//加入这个地址的图片，调用下面这个setPicForGridView函数
    }
    private void setPicForGridView(String image_path){
        if(image_path==null){
            Toast.makeText(FootCreateActivity.this,"没有此图片",Toast.LENGTH_SHORT).show();
            return;
        }else {
//            Toast.makeText(FootCreateActivity.this,image_path,Toast.LENGTH_SHORT).show();
            footimageList.remove(camera);//去掉最后的加号图片
            FootImage footimage = new FootImage(image_path);
            footimageList.add(footimage);//加入当前选择的图片地址
            adapter.notifyDataSetChanged();//将适配器更新
            footimageList.add(camera);//最后补上加号图片
        }
    }
}
