//这里主要是显示pdf列表
package com.example.artshoes2;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
import androidx.core.content.FileProvider;

import com.example.artshoes2.user.UserInformation;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.artshoes2.Utils.FileUtils.CreatePdfFolder;

//pdf页面
public class TabPdfActivity extends AppCompatActivity {
    private final static String TAG = "TabPdfActivity";
    private List<Pdf> pdfList=new ArrayList<Pdf>();
    private ListView listView;
    private PdfAdapter adapter;
    public Pdf pdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        Toolbar tl_head = (Toolbar) findViewById(R.id.tl_head);
        initPdf();//初始化本地当前的pdf状态
        adapter=new PdfAdapter(TabPdfActivity.this, R.layout.pdf_item,pdfList);
        listView=(ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        //ListView设置点击监听事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pdf=pdfList.get(position);
//                Toast.makeText(TabPdfActivity.this,"你点击了"+pdf.getFileName(),Toast.LENGTH_SHORT).show();
                try{
                    String filepath= CreatePdfFolder()+"/"+pdf.getFileName()+".pdf";//要存储在pdf文件夹，文件名为这个pdf类的文件名
                    //FileUtils.inputStream2File(getAssets().open("pdf/1.pdf"),filepath);//文件复制，将来网络上下载pdf可以直接放到filepath路径中

                    filepath= CreatePdfFolder()+"/"+pdf.filename+".pdf";//要存储在pdf文件夹，文件名为这个pdf类的文件名
                    File file=new File(filepath);
                    if(!file.exists()){
                        Toast.makeText(TabPdfActivity.this,"文件不存在，请先下载",Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent=getPdfFileIntent(file);//打开pdf文件，见下面的过程
                        startActivity(intent);

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }



            }
        });
        //ListView长按事件监听
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                pdf=pdfList.get(position);
                listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//                        menu.add(0, 0, 0, "进行三维重建");
                        menu.add(0, 0, 0, "删除");
//                        menu.add(0, 2, 0, "删除ALL");
                    }
                });
                return false;
            }
        });
        //对导航栏进行设置
        tl_head.setTitle("脚型报告");
        setSupportActionBar(tl_head);
    }

    @Override
    protected void onStart(){
        super.onStart();
        initPdf();
        adapter.notifyDataSetChanged();
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
            UserInformation.downloadid=1;
            Intent intent = new Intent(TabPdfActivity.this, GetFootByIDActivity.class);
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
//                Toast.makeText(TabPdfActivity.this, "删除", Toast.LENGTH_SHORT).show();
                //删除文件
                File file=new File(CreatePdfFolder()+"/"+pdf.filename+".pdf");
//                Toast.makeText(TabPdfActivity.this,CreatePdfFolder()+"/"+pdf.filename+".pdf",Toast.LENGTH_LONG).show();
                if (file == null || !file.exists() || file.isDirectory()){
                }else{
                    file.delete();
                }
                // 删除listview中的项目
                Iterator<Pdf> iterator = pdfList.iterator();
                while (iterator.hasNext()) {
                    Pdf value = iterator.next();
                    if (value.equals(pdf)) {
                        iterator.remove();//删除当前位置的pdf
                    }
                }
                adapter.notifyDataSetChanged();//改变当前列表装载器的显示
                break;
//            case 1:
//                // 删除操作
//                break;
//            case 2:
//                // 删除ALL操作
//                break;
            default:
                break;
        }
        return false;
    }

    private void initPdf(){
        File f=new File(CreatePdfFolder());//打开脚型报告文件存储目录
        File[] files= f.listFiles();//载入所有文件
//        Toast.makeText(this,files[0].toString(),Toast.LENGTH_LONG).show();
        if(files!=null && files.length!=0){
            for(int i=0;i<files.length;i++){//将txt文件一个个转换成相应foot类
                String filename=files[i].getName().substring(0,files[i].getName().length()-4);
                Pdf temp=new Pdf(filename,files[i].getAbsolutePath(),R.drawable.file_pdf);
                Iterator<Pdf> iterator = pdfList.iterator();
                int flag=0;
                while (iterator.hasNext()) {
                    Pdf value = iterator.next();
                    if (value.filename.equals(temp.filename)) {
                        flag=1;
                    }else{
                        continue;
                    }
                }
                if(flag==0){
                    pdfList.add(temp);
                }
            }
        }else{
            return;
        }
    }
    public Intent getPdfFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri uri = FileProvider.getUriForFile(TabPdfActivity.this, "com.example.artshoes2.fileprovider", file);
            intent.setDataAndType(uri, "application/pdf");
        } else {
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
        }
        return Intent.createChooser(intent, "Open File");
    }
}