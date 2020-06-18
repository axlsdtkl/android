//主页界面，主要显示了各种脚型
package com.example.artshoes2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
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
import com.example.artshoes2.user.UserInformation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static com.example.artshoes2.Utils.FileUtils.CreateFootMessageFolder;
import static com.example.artshoes2.Utils.FileUtils.GetFootId;
import static com.example.artshoes2.Utils.FileUtils.ReadTxtToFoot;
import static com.example.artshoes2.Utils.FileUtils.SendFootToServer;

public class TabHomeActivity extends AppCompatActivity {
    private final static String TAG = "TabHomeActivity";
    private List<Foot> footList=new ArrayList<Foot>();
    private ListView listView;
    public static final int CREATENEW=0;
    public static final int ALTEROLD=1;
    private FootAdapter adapter;
    private Foot foot;
    private ProgressDialog progressDialog;
    private String servicestate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar tl_head = (Toolbar) findViewById(R.id.tl_head);
        initFeet();//初始化脚型
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
                Intent intent = new Intent(TabHomeActivity.this,FootAlterActivity.class);//进入修改脚型界面
                intent.putExtra("foot_data",foot);//加入返回标签名和作为存储信息的对象文件
                startActivityForResult(intent,ALTEROLD);//有返回值的跳转，返回标记是ALTEROLD
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
                        menu.add(0, 1, 0, "查看此脚型的id");
                        menu.add(0, 2, 0, "删除");
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
            Intent intent = new Intent(TabHomeActivity.this, FootCreateActivity.class);
            startActivityForResult(intent,CREATENEW);//进行添加脚型界面，返回值为CREATENEW)，，返回的对象foot_data在进入的新页面里面也可以设置返回的对象值
        } else if (id == R.id.menu_get) {
            Intent intent = new Intent(TabHomeActivity.this, GetFootByIDActivity.class);//进入根据id获取脚型界面
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
                final CountDownLatch latch= new CountDownLatch(1);//使用java并发库concurrent
                new Thread(new Runnable() {
                    @Override
                    public void run() {//开启线程来获得真实要下载的目录url地址
                        String path=UserInformation.httpurl+"/GetService?question=state";
                        try {
                            try{
                                URL url = new URL(path); //新建url并实例化
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setRequestMethod("GET");//获取服务器数据
                                connection.setReadTimeout(8000);//设置读取超时的毫秒数
                                connection.setConnectTimeout(8000);//设置连接超时的毫秒数
                                InputStream in = connection.getInputStream();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                                servicestate = reader.readLine();//读取服务器进行逻辑处理后页面显示的数据
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
                if(servicestate.equals("running")){
                    String warn="稍等片刻后上传";
                    String message="服务器正在进行三维重建，请稍后再试";
                    AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(TabHomeActivity.this);
                    alertDialogBuilder.setTitle(warn);//标题
                    alertDialogBuilder.setMessage(message);//内容
                    alertDialogBuilder.setIcon(R.drawable.ic_no);//图标
                    alertDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog alert=alertDialogBuilder.create();
                    alert.show();
                    break;
                }


                // 三维重建
                //SaveFootToFile(foot);存储到本地，由于图片比较大，速度会比较慢，所以我不存直接发送给服务器了
                //Toast.makeText(TabHomeActivity.this, "已存储到本地等待发送", Toast.LENGTH_SHORT).show();
                progressDialog=new ProgressDialog(TabHomeActivity.this);
                progressDialog.setTitle("本地图片正在上传...");
                progressDialog.setMessage("请不要关闭上传");
                progressDialog.setCancelable(true);
                progressDialog.show();
                new Thread(new Runnable() {
                    public void run() {//开启线程，进行发送
                        int footid= 0;
                        try {
                            footid = GetFootId(foot,0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        int flag=SendFootToServer(foot,TabHomeActivity.this);
                        progressDialog.dismiss();
                        Looper.prepare();
                        String warn="上传成功";
                        String message="您的脚型id为："+String.valueOf(footid);
                        AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(TabHomeActivity.this);
                        alertDialogBuilder.setTitle(warn);//标题
                        alertDialogBuilder.setMessage(message);//内容
                        alertDialogBuilder.setIcon(R.drawable.ic_yes);//图标
                        alertDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        AlertDialog alert=alertDialogBuilder.create();
                        alert.show();
                        Looper.loop();
                    }
                }).start();
                break;
            case 1:
                System.out.println("123");
                new Thread(new Runnable() {
                    public void run() {//开启线程，进行发送
                        int footid= 0;
                        System.out.println("123546");
                        try {
                            footid = GetFootId(foot,1);
                            System.out.println(footid);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String warn;
                        String message;
                        Looper.prepare();
                        if(footid==-1){
                            warn="没有上传";
                            message="请先上传脚型进行重建，服务器会发送相应id";
                        }else{
                            warn="你的脚型id为:";
                            message=String.valueOf(footid);
                        }

                        AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(TabHomeActivity.this);
                        alertDialogBuilder.setTitle(warn);//标题
                        alertDialogBuilder.setMessage(message);//内容
                        alertDialogBuilder.setIcon(R.drawable.ic_yes);//图标
                        alertDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        AlertDialog alert=alertDialogBuilder.create();
                        alert.show();
                        Looper.loop();
                    }
                }).start();
                break;
            case 2:
                final CountDownLatch latch2= new CountDownLatch(1);//使用java并发库concurrent
                new Thread(new Runnable() {
                    @Override
                    public void run() {//开启线程来获得真实要下载的目录url地址
                        String path=UserInformation.httpurl+"/GetService?question=state";
                        try {
                            try{
                                URL url = new URL(path); //新建url并实例化
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setRequestMethod("GET");//获取服务器数据
                                connection.setReadTimeout(8000);//设置读取超时的毫秒数
                                connection.setConnectTimeout(8000);//设置连接超时的毫秒数
                                InputStream in = connection.getInputStream();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                                servicestate = reader.readLine();//读取服务器进行逻辑处理后页面显示的数据
                            }catch (MalformedURLException e){}
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        latch2.countDown();
                    }
                }).start();
                try {
                    latch2.await();//阻塞当前线程直到latch中数值为零才执行
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(servicestate.equals("running")) {
                    String warn = "服务器正在进行三维重建";
                    String message = "请稍后再尝试删除";
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TabHomeActivity.this);
                    alertDialogBuilder.setTitle(warn);//标题
                    alertDialogBuilder.setMessage(message);//内容
                    alertDialogBuilder.setIcon(R.drawable.ic_no);//图标
                    alertDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog alert = alertDialogBuilder.create();
                    alert.show();
                    break;
                }
                // 删除操作
                //删除本地数据文件



                File file=new File(CreateFootMessageFolder()+"/"+foot.filename+".txt");
                if (file == null || !file.exists() || file.isDirectory()){
                }else{
                    file.delete();
                }
                //删除对应Foot类
                Iterator<Foot> iterator = footList.iterator();
                while (iterator.hasNext()) {
                    Foot value = iterator.next();
                    if (value.equals(foot)) {
                        iterator.remove();
                    }
                }
                adapter.notifyDataSetChanged();//将列表适配器进行显示的更新，显示最新的表单情况
                try {
                    deleteserverfolder(foot.filename);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        return false;
    }
    //删除当前脚型
    private void deleteserverfolder(String filename)throws   IOException {

        //因为有中文，服务器会无法识别这个url，所以对中文进行编码
        final String username=java.net.URLEncoder.encode(UserInformation.username,"utf-8");
        final String userfilename=java.net.URLEncoder.encode(filename,"utf-8");

        new Thread(new Runnable() {
            @Override
            public void run() {//开启线程用来注册
                String path= UserInformation.httpurl+"/deleteFootFile?username="+username+"&filename="+userfilename;
                try {
                    try{
                        System.out.println("delete");
                        URL url = new URL(path); //新建url并实例化
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");//获取服务器数据
                        connection.setReadTimeout(8000);//设置读取超时的毫秒数
                        connection.setConnectTimeout(8000);//设置连接超时的毫秒数
                        InputStream in = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String result = reader.readLine();//读取服务器进行逻辑处理后页面显示的数据
                        if (result.equals("删除成功!")){
                            new Thread(new Runnable(){
                                int initial=0;
                                public void run(){
                                    Looper.prepare();
                                    Toast.makeText(TabHomeActivity.this,"服务器删除成功!",Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            }).start();
                        }else if(result.equals("删除失败!")){
                            Looper.prepare();
                            Toast.makeText(TabHomeActivity.this,"服务器删除失败!",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }catch (MalformedURLException e){}
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //对脚型信息文件进行初始化，在写软件的时候作为测试用
    private void initFeet(){
        //测试
        File f=new File(CreateFootMessageFolder());//打开脚型存储目录
        File[] files= f.listFiles();//载入所有文件
//        Toast.makeText(this,files[0].toString(),Toast.LENGTH_LONG).show();
        if(files.length!=0){
            for(int i=0;i<files.length;i++){//将txt文件一个个转换成相应foot类
                Foot temp=new Foot("",1,1,null, R.drawable.file_foot);
                ReadTxtToFoot(files[i],temp,this);
                footList.add(temp);//加入脚型的列表
            }
        }else{
            int x=1;
            return;
        }

    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {//当界面返回结果
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CREATENEW://如果为创建返回值
                if(resultCode==RESULT_OK){
                    //对本页foot进行创建：姓名，性别，左右脚，更改时间；
                    Foot temp=(Foot)data.getParcelableExtra("foot_data");//拿出当前的foot_data，下面是拿出各种信息，我觉得直接是Foot类型的话，直接拿来用，所以下面这段注释了，以后我再仔细看看

                    /*List<FootImage> footImageListtemp=new ArrayList<FootImage>();
                    String name=foot1.name;
                    int sex=foot1.sex;
                    int LorRfoot=foot1.foot;
                    Iterator<FootImage> iterator = foot1.footimageList.iterator();
                    while (iterator.hasNext()) {
                        FootImage value = iterator.next();
                        footImageListtemp.add(value);
                    }
                    Foot temp=new Foot(name,sex,LorRfoot,footImageListtemp, R.drawable.file_foot);*/
                    temp.reNewFileName();
                    temp.datechange= TimeUtils.getNowDateTime("yyyy-MM-dd HH:mm:ss");//获取当前时间
                    footList.add(temp);//脚型显示列表中加入当前对象
                    adapter.notifyDataSetChanged();//改成最新的脚型列表
                }
                break;
            case ALTEROLD://如果为替换返回值
                if(resultCode==RESULT_OK){//如果进行过修改的话
                    //对本页foot进行更新：姓名，性别，左右脚，更改时间；
                    Foot foot1=(Foot)data.getParcelableExtra("foot_data");//返回foot_data这个foot型，下面为将列表中的当前foot改为最新的foot
                    foot.name=foot1.name;
                    foot.sex=foot1.sex;
                    foot.foot=foot1.foot;
                    foot.datechange= TimeUtils.getNowDateTime("yyyy-MM-dd HH:mm:ss");
                    foot.footimageList.clear();//清除之前的图片记录列表
                    Iterator<FootImage> iterator = foot1.footimageList.iterator();
                    while (iterator.hasNext()) {
                        FootImage value = iterator.next();
                        foot.footimageList.add(value);//重新将图片路径一个个加入当前foot对象，形成最新的foot类，这样下次打开就能做到始终是最新的
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

