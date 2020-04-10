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

import com.example.artshoes2.Utils.FileUtils;
import com.example.artshoes2.obj.ObjLoadActivity;
import com.example.artshoes2.obj.ObjReader;
import com.example.artshoes2.user.UserInformation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//obj表目录
public class TabObjActivity extends AppCompatActivity {
    private final static String TAG = "TabObjActivity";
    private List<Obj> objList=new ArrayList<Obj>();
    private ListView listView;
    private Obj obj;
    ObjAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obj);
        Toolbar tl_head = (Toolbar) findViewById(R.id.tl_head);
        initObj();
        adapter=new ObjAdapter(TabObjActivity.this, R.layout.obj_item,objList);
        listView=(ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        //ListView设置点击监听事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                obj=objList.get(position);
//                Toast.makeText(TabObjActivity.this,"你点击了"+obj.getFileName(),Toast.LENGTH_SHORT).show();

                String filepath= FileUtils.CreateObjFolder()+"/"+"2.obj";
                File file=new File(filepath);
                if(!file.exists()){
                    Toast.makeText(TabObjActivity.this,"文件不存在，请先下载",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(TabObjActivity.this, ObjLoadActivity.class);//进入obj浏览界面
//                intent.putExtra("filepath",filepath);
                    startActivity(intent);
                }

            }
        });
        //ListView长按事件监听
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                obj=objList.get(position);
                listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//                        menu.add(0, 0, 0, "进行三维重建");
                        menu.add(0, 0, 0, "删除");
//                        menu.add(0, , 0, "删除ALL");
                    }
                });
                return false;
            }
        });
        //对导航栏进行设置
        tl_head.setTitle("脚型模型");
        setSupportActionBar(tl_head);
    }

//    @Override
//    public boolean onMenuOpened(int featureId, Menu menu) {
//        // 显示菜单项左侧的图标
//        Utils.setOverflowIconVisible(featureId, menu);
//        return super.onMenuOpened(featureId, menu);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_footobj, menu);
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

        } else if (id == R.id.menu_get) {
            UserInformation.downloadid=2;
            Intent intent = new Intent(TabObjActivity.this,GetFootByIDActivity.class);//打开根据id从服务器获取地址的界面
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
                // 删除操作
                Toast.makeText(TabObjActivity.this, "删除", Toast.LENGTH_SHORT).show();
                Iterator<Obj> iterator = objList.iterator();
                while (iterator.hasNext()) {
                    Obj value = iterator.next();
                    if (value.equals(obj)) {//删除当前
                        iterator.remove();
                    }
                }
                adapter.notifyDataSetChanged();//删除后最新的列表显示
                break;
            default:
                break;
        }
        return false;
    }

    private void initObj(){//初始化obj，直接拿下面的先看看，之后再改成
        String name[]={"下载"};
        for(int i=0;i<name.length;i++){
            for(int j=0;j<2;j++){
                if(j==0){
                    String obj1="";
                    Obj temp=new Obj(name[i]+"的脚型模型",obj1, R.drawable.file_obj);
                    objList.add(temp);
                }
            }
        }




    }
}

