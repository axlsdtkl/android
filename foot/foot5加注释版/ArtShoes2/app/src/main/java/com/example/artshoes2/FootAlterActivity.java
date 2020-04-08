//这个主要是修改脚型，其实和创建脚型类似，点开以后先要根据当前的脚型信息进行一个默认的显示，然后根据用户最新的操作进行相应修改与显示
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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.artshoes2.Utils.FileUtils.CreateFootMessageFolder;
import static com.example.artshoes2.Utils.FileUtils.SaveFootToTxt;

public class FootAlterActivity extends AppCompatActivity {
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
    private final static String TAG = "FootAlterActivity";
    private List<FootImage> footimageList=new ArrayList<FootImage>();
    private FootImage camera=new FootImage(" ");//这个起到标记的作用，如果是" "路径，则图片会用加号这个图片来显示
    private FootImageAdapter adapter;
    private Foot foot;
    private String name;
    private int sex=0;
    private int leftorightfoot=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footalter);
        //获取本页所有控件
        edt_name=findViewById(R.id.edt_name);
        rg_sex=findViewById(R.id.rg_sex);
        rb_male=findViewById(R.id.male);
        rb_female=findViewById(R.id.female);
        rg_LorRfoot=findViewById(R.id.rg_foot);
        rb_left=findViewById(R.id.left);
        rb_right=findViewById(R.id.right);
        gv_footimage=findViewById(R.id.gv_footimage);
        adapter=new FootImageAdapter(FootAlterActivity.this,R.layout.footimage_item,footimageList);
        gv_footimage.setAdapter(adapter);//放入装配
        Toolbar tl_head = (Toolbar) findViewById(R.id.tl_head);
        //对导航栏进行设置
        tl_head.setTitle("脚型信息");
        setSupportActionBar(tl_head);
        tl_head.setNavigationIcon(R.drawable.ic_back);
        //为图片添加点击监听事件。
        gv_footimage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                footimage=footimageList.get(position);
                if(footimage.getFootImagePath()==" "){//如果点的是加号图片，则进入选本地照片界面
                    Intent intentFromGallery;
                    intentFromGallery = new Intent(Intent.ACTION_GET_CONTENT);
                    intentFromGallery.setType("image/*");
                    startActivityForResult(intentFromGallery, CHOOSE_PTHOTO);
                }else {//否则点的是图片，进入看具体大图片功能界面
                    String footimage_path = footimage.footimage_path;
//                Toast.makeText(FootAlterActivity.this,"你点击了照片"+footimagepath,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FootAlterActivity.this, FootImageViewActivity.class);
                    intent.putExtra("ImagePath", footimage_path);
                    startActivityForResult(intent, VIEW_PHOTO);
                }
            }
        });
        //添加性别单选监听事件。
        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.male){
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
                if(checkedId==R.id.left){
                    leftorightfoot=1;
                }else{
                    leftorightfoot=2;
                }
            }
        });
        //更新脚型信息
        Intent intent=getIntent();
        foot=(Foot)intent.getParcelableExtra("foot_data");//拿出传过来的脚型foot_data文件
        NotifyFootDataChange(foot);//执行初始化过程，见下面的NotifyFootDataChange函数
        edt_name.requestFocus();
    }

    public void NotifyFootDataChange(Foot foot) {
        initFootImageList(footimageList);//清除当前脚照片的列表
        //更新名字
        name=foot.getName();
        edt_name.setText(name);
        //更新性别单选项
        sex=foot.getSex();
        if(sex==1){
            rg_sex.check(rb_male.getId());
        }else{
            rg_sex.check(rb_female.getId());
        }
        //更新左右脚单选项
        leftorightfoot=foot.getFoot();
        if(leftorightfoot==1){
            rg_LorRfoot.check(rb_left.getId());
        }else{
            rg_LorRfoot.check(rb_right.getId());
        }
        //更新图片信息
        Iterator<FootImage> iterator = foot.getFootImageList().iterator();
        while (iterator.hasNext()) {
            FootImage value = iterator.next();
            footimageList.add(value);//增加各张图片地址
        }
        footimageList.add(camera);//加入加号图片空地址
        adapter.notifyDataSetChanged();//对适配器进行更新
    }
    public void initFootImageList(List<FootImage> footimageList){
        footimageList.clear();
        //设置选择图片按钮
    }
    public void AlterFootData(Foot foot){//将脚型foot类换成最新的数据
        foot.footimageList.clear();
        Iterator<FootImage> iterator = footimageList.iterator();
        while (iterator.hasNext()) {
            FootImage value = iterator.next();
            if(value.getFootImagePath()!=" "){
                foot.footimageList.add(value);
            }
        }
        //暂时删除文件
        File file=new File(CreateFootMessageFolder()+"/"+foot.filename+".txt");
        if(file.exists()){
            file.delete();
        }
        foot.name=edt_name.getText().toString();
        foot.sex=sex;
        foot.foot=leftorightfoot;
        foot.datechange=TimeUtils.getNowDateTime("yyyy-MM-dd HH:mm:ss");
        foot.reNewFileName();
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
        } else if (id == R.id.menu_save) {//如果进行存储操作的话
            if(edt_name.getText().toString().trim().length()<1||sex==0||leftorightfoot==0){//如果用户输入不合法的话
                if(edt_name.getText().toString().trim().length()<1){
                    Toast.makeText(FootAlterActivity.this,"请输入名字",Toast.LENGTH_SHORT).show();
                }else if(sex==0){
                    Toast.makeText(FootAlterActivity.this,"请选择性别",Toast.LENGTH_SHORT).show();
                }else if(leftorightfoot==0){
                    Toast.makeText(FootAlterActivity.this,"请选择左右脚",Toast.LENGTH_SHORT).show();
                }
            }else {//如果用户输入合法，则进行存储
                AlterFootData(foot);//将脚型foot类更新成最新的信息
                SaveFootToTxt(foot);//将当前的foot信息保存在本地
                Intent intent = new Intent();
                intent.putExtra("foot_data", foot);
                setResult(RESULT_OK, intent);//返回已经更新，这样外层界面也会相应修改更新脚型列表显示图
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_PTHOTO://如果为选照片的话
                if (resultCode == RESULT_OK) {
//                    Uri selectedImage = data.getData();
//                    String picturePath = PictureHelper.getPath(FootAlterActivity.this, selectedImage);
//                    setPicForGridView(picturePath);
//                    Uri uri=geturi(data);
//                    String imagepath=g(uri,null);
//                    setPicForGridView(imagepath);
                    handleImageOnKitKat(data);//处理data，将这个数据更新到当前列表中，执行下面这个handleImageOnKitKat过程
//                    Uri tempUri = data.getData();
//                    String path = PicUnits.getRealPathFromUri(FootAlterActivity.this, tempUri);
//                    setPicForGridView(path);
                }

                break;
            case VIEW_PHOTO:
                if(resultCode==RESULT_OK){//如果有删除操作的信号，则删除
                    Iterator<FootImage> iterator = footimageList.iterator();
                    while (iterator.hasNext()) {
                        FootImage value = iterator.next();
                        if (value.equals(footimage)) {
                            iterator.remove();
                        }
                    }
                    adapter.notifyDataSetChanged();//显示最新的表单
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
    private void handleImageOnKitKat(Intent data) {//data图片路径拿取，放入imagePath
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
        setPicForGridView(imagePath);//将imagePath这个路径的图片显示在最新的可视列表中
    }
    private void setPicForGridView(String image_path){
        if(image_path==null){
            Toast.makeText(FootAlterActivity.this,"没有此图片",Toast.LENGTH_SHORT).show();
            return;
        }else {
//            Toast.makeText(FootAlterActivity.this,image_path,Toast.LENGTH_SHORT).show();
            //把添加图片按钮后移一位
//            Iterator<FootImage> iterator = footimageList.iterator();
//            while (iterator.hasNext()) {
//                FootImage value = iterator.next();
//                if(value.getFootImagePath()==" "){
//                    footimageList.remove(value);
//                }
//            }
            footimageList.remove(camera);//删除最后一个加号
            FootImage footimage = new FootImage(image_path);
            footimageList.add(footimage);//加入新的图片
            footimageList.add(camera);//补上加号
            adapter.notifyDataSetChanged();//更新
        }
    }


}
