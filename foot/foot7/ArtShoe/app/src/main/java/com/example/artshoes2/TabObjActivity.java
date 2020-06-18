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

import com.example.artshoes2.obj.ObjLoadActivity;
import com.example.artshoes2.user.UserInformation;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.artshoes2.Utils.FileUtils.CreateObjFolder;

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

                String filepath= obj.filepath;
                File file=new File(filepath);
                if(!file.exists()){
                    Toast.makeText(TabObjActivity.this,"文件不存在，请先下载",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(TabObjActivity.this, ObjLoadActivity.class);//进入obj浏览界面
                    intent.putExtra("filepath",filepath);
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
    protected void onStart(){
        super.onStart();
        initObj();
        adapter.notifyDataSetChanged();
    }
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
                //删除文件
                File file=new File(CreateObjFolder()+"/"+obj.filename+".obj");
//                Toast.makeText(TabPdfActivity.this,CreatePdfFolder()+"/"+pdf.filename+".pdf",Toast.LENGTH_LONG).show();
                if (file == null || !file.exists() || file.isDirectory()){
                }else{
                    file.delete();
                }
                // 删除listview中的项目
                Iterator<Obj> iterator = objList.iterator();
                while (iterator.hasNext()) {
                    Obj value = iterator.next();
                    if (value.equals(obj)) {
                        iterator.remove();//删除当前位置的pdf
                    }
                }
                adapter.notifyDataSetChanged();//改变当前列表装载器的显示
                break;
            default:
                break;
        }
        return false;
    }

    private void initObj(){//拿随意的填充一下，
        File f=new File(CreateObjFolder());//打开脚型报告文件存储目录
        File[] files= f.listFiles();//载入所有文件
//        Toast.makeText(this,files[0].toString(),Toast.LENGTH_LONG).show();
        if(files!=null&&files.length!=0){
            for(int i=0;i<files.length;i++){//将txt文件一个个转换成相应foot类
                String filename=files[i].getName().substring(0,files[i].getName().length()-4);
                Obj temp=new Obj(filename,files[i].getAbsolutePath(),R.drawable.file_obj);
                Iterator<Obj> iterator = objList.iterator();
                int flag=0;
                while (iterator.hasNext()) {
                    Obj value = iterator.next();
                    if (value.filename.equals(temp.filename)) {
                        flag=1;
                    }else{
                        continue;
                    }
                }
                if(flag==0){
                    objList.add(temp);
                }
            }
        }else{
            return;
        }
    }
}