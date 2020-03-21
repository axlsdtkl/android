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
        setSupportActionBar(tl_head);
        tl_head.setNavigationIcon(R.drawable.ic_back);
        footimageList.add(camera);
        gv_footimage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                footimage=footimageList.get(position);
                if(footimage.getFootImagePath()==" "){
                    Intent intentFromGallery;
                    intentFromGallery = new Intent(Intent.ACTION_GET_CONTENT);
                    intentFromGallery.setType("image/*");
                    startActivityForResult(intentFromGallery, CHOOSE_PTHOTO);
                }else {
                    String footimage_path = footimage.footimage_path;
//                Toast.makeText(FootAlterActivity.this,"你点击了照片"+footimagepath,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FootCreateActivity.this, FootImageViewActivity.class);
                    intent.putExtra("ImagePath", footimage_path);
                    startActivityForResult(intent, VIEW_PHOTO);
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
        getMenuInflater().inflate(R.menu.menu_footmessage, menu);
        ToolBarUnits.setIconVisable(menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.menu_save) {
            if(edt_name.getText().toString().trim().length()<1||sex==0||leftorightfoot==0){
                if(edt_name.getText().toString().trim().length()<1){
                    Toast.makeText(FootCreateActivity.this,"请输入名字",Toast.LENGTH_SHORT).show();
                }else if(sex==0){
                    Toast.makeText(FootCreateActivity.this,"请选择性别",Toast.LENGTH_SHORT).show();
                }else if(leftorightfoot==0){
                    Toast.makeText(FootCreateActivity.this,"请选择左右脚",Toast.LENGTH_SHORT).show();
                }
            }else {
                footimageList.remove(camera);
                Foot foot = new Foot(edt_name.getText().toString(), sex, leftorightfoot, footimageList, R.drawable.file_foot);
                foot.reNewFileName();
                foot.datechange = TimeUtils.getNowDateTime("yyyy-MM-dd HH:mm:ss");
                Toast.makeText(FootCreateActivity.this, "保存", Toast.LENGTH_SHORT).show();
                SaveFootToTxt(foot);
                Intent intent = new Intent();
                intent.putExtra("foot_data", foot);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_PTHOTO:
                if (resultCode == RESULT_OK) {
                    handleImageOnKitKat(data);
                }

                break;
            case VIEW_PHOTO:
                if(resultCode==RESULT_OK){
                    Iterator<FootImage> iterator = footimageList.iterator();
                    while (iterator.hasNext()) {
                        FootImage value = iterator.next();
                        if (value.equals(footimage)) {
                            iterator.remove();
                        }
                    }
                    adapter.notifyDataSetChanged();
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
        setPicForGridView(imagePath);
    }
    private void setPicForGridView(String image_path){
        if(image_path==null){
            Toast.makeText(FootCreateActivity.this,"没有此图片",Toast.LENGTH_SHORT).show();
            return;
        }else {
//            Toast.makeText(FootCreateActivity.this,image_path,Toast.LENGTH_SHORT).show();
            footimageList.remove(camera);
            FootImage footimage = new FootImage(image_path);
            footimageList.add(footimage);
            adapter.notifyDataSetChanged();
            footimageList.add(camera);
        }
    }
}
