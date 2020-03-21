package com.example.artshoes2;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.artshoes2.Utils.TimeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.artshoes2.Utils.FileUtils.CreateFootMessageFolder;
import static com.example.artshoes2.Utils.FileUtils.CreateSendMessageFolder;
import static com.example.artshoes2.Utils.FileUtils.ReadTxtToFoot;
import static com.example.artshoes2.Utils.FileUtils.SaveFootToFile;

public class TabHomeActivity extends AppCompatActivity {
    private final static String TAG = "TabHomeActivity";
    private List<Foot> footList=new ArrayList<Foot>();
    private ListView listView;
    public static final int CREATENEW=0;
    public static final int ALTEROLD=1;
    private FootAdapter adapter;
    private Foot foot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar tl_head = (Toolbar) findViewById(R.id.tl_head);
        initFeet();
        adapter=new FootAdapter(TabHomeActivity.this, R.layout.foot_item,footList);
        listView=(ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        //ListView设置点击监听事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foot=footList.get(position);
//                Toast.makeText(TabHomeActivity.this,"你点击了"+foot.getFileName(),Toast.LENGTH_SHORT).show();
                foot.ChangeDate(TimeUtils.getNowDateTime("yyyy-MM-dd HH:mm:ss"));
                Intent intent = new Intent(TabHomeActivity.this,FootAlterActivity.class);
                intent.putExtra("foot_data",foot);
                startActivityForResult(intent,ALTEROLD);
            }
        });
        //ListView长按事件监听
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                foot=footList.get(position);
                listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                        menu.add(0, 0, 0, "进行三维重建");
                        menu.add(0, 1, 0, "删除");
                    }
                });
                return false;
            }
        });
        //对导航栏进行设置
        tl_head.setTitle("首页");
        setSupportActionBar(tl_head);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overflow, menu);
        ToolBarUnits.setIconVisable(menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.menu_search) {
            ToolBarUnits.CreateOfOverflowMenu(this);

        } else if (id == R.id.menu_add) {
//            Toast.makeText(this,"您点击了添加键",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(TabHomeActivity.this,FootCreateActivity.class);
            startActivityForResult(intent,CREATENEW);
        } else if (id == R.id.menu_get) {
            Intent intent = new Intent(TabHomeActivity.this,GetFootByIDActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_help){
            Toast.makeText(this,"您点击了帮助键",Toast.LENGTH_LONG).show();
        } else if (id == R.id.menu_suggest){
            Toast.makeText(this,"您点击了建议键",Toast.LENGTH_LONG).show();

        } else if (id == R.id.menu_quit){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        MID = (int) info.id;// 这里的info.id对应的就是数据库中_id的值
        switch(item.getItemId()) {
            case 0:
                // 三维重建
                SaveFootToFile(foot);
                Toast.makeText(TabHomeActivity.this, "已存储到本地等待发送", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                // 删除操作
                //删除本地数据文件
                File file=new File(CreateFootMessageFolder()+"/"+foot.filename+".txt");
                if (file == null || !file.exists() || file.isDirectory()){
                }else{
                    file.delete();
                }
                //删除通信文件
                File file1=new File(CreateSendMessageFolder()+"/"+foot.filename+".txt");
                if (file1 == null || !file1.exists() || file1.isDirectory()){
                }else{
                    file1.delete();
                }
                //删除对应Foot类
                Iterator<Foot> iterator = footList.iterator();
                while (iterator.hasNext()) {
                    Foot value = iterator.next();
                    if (value.equals(foot)) {
                        iterator.remove();
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
        return false;
    }
    //对脚型信息文件进行初始化，在写软件的时候作为测试用
    private void initFeet(){
        //测试
        File f=new File(CreateFootMessageFolder());
        File[] files= f.listFiles();
//        Toast.makeText(this,files[0].toString(),Toast.LENGTH_LONG).show();
        if(files.length!=0){
            for(int i=0;i<files.length;i++){
                Foot temp=new Foot("",1,1,null, R.drawable.file_foot);
                ReadTxtToFoot(files[i],temp,this);
                footList.add(temp);
            }
        }else{
            return;
        }
//        String name[]={"赵小红","刘晓明","刘小红","孙晓明","孙小红","王小明","王晓红","周晓明","周小红","李晓明","李晓红"};
//        for(int i=0;i<name.length;i++){
//            for(int j=0;j<2;j++){
//                if(j==0){
//                    List<FootImage> footimage=new ArrayList<FootImage>();
//                    Foot temp=new Foot(name[i],1,1,footimage,R.drawable.file_foot);
//                    temp.ChangeDate(TimeUtils.getNowDateTime("yyyy-MM-dd HH:mm:ss"));
//                    footList.add(temp);
//                }else{
//                    List<FootImage> footimage=new ArrayList<FootImage>();
//                    Foot temp=new Foot(name[i],1,2,footimage,R.drawable.file_foot);
//                    temp.ChangeDate(TimeUtils.getNowDateTime("yyyy-MM-dd HH:mm:ss"));
//                    footList.add(temp);
//                }
//
//            }
//        }

    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CREATENEW:
                if(resultCode==RESULT_OK){
                    //对本页foot进行创建：姓名，性别，左右脚，更改时间；
                    Foot foot1=(Foot)data.getParcelableExtra("foot_data");
                    List<FootImage> footImageListtemp=new ArrayList<FootImage>();
                    String name=foot1.name;
                    int sex=foot1.sex;
                    int LorRfoot=foot1.foot;
                    Iterator<FootImage> iterator = foot1.footimageList.iterator();
                    while (iterator.hasNext()) {
                        FootImage value = iterator.next();
                        footImageListtemp.add(value);
                    }
                    Foot temp=new Foot(name,sex,LorRfoot,footImageListtemp, R.drawable.file_foot);
                    temp.reNewFileName();
                    temp.datechange=TimeUtils.getNowDateTime("yyyy-MM-dd HH:mm:ss");
                    footList.add(temp);
                    adapter.notifyDataSetChanged();
                }
                break;
            case ALTEROLD:
                if(resultCode==RESULT_OK){
                    //对本页foot进行更新：姓名，性别，左右脚，更改时间；
                    Foot foot1=(Foot)data.getParcelableExtra("foot_data");
                    foot.name=foot1.name;
                    foot.sex=foot1.sex;
                    foot.foot=foot1.foot;
                    foot.datechange=TimeUtils.getNowDateTime("yyyy-MM-dd HH:mm:ss");
                    foot.footimageList.clear();
                    Iterator<FootImage> iterator = foot1.footimageList.iterator();
                    while (iterator.hasNext()) {
                        FootImage value = iterator.next();
                        foot.footimageList.add(value);
                    }
                    foot.reNewFileName();
                    adapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
    }
}
