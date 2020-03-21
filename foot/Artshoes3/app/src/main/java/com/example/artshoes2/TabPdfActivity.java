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

import com.example.artshoes2.Utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        initPdf();
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
                    String filepath= FileUtils.CreatePdfFolder()+"/"+pdf.getFileName()+".pdf";
                    FileUtils.inputStream2File(getAssets().open("pdf/1.pdf"),filepath);
                    File file=new File(filepath);
                    if(file!=null){
                        Intent intent=getPdfFileIntent(file);
                        startActivity(intent);
                    }else{
                        Toast.makeText(TabPdfActivity.this,"文件不存在",Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(TabPdfActivity.this,GetFootByIDActivity.class);
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
                Toast.makeText(TabPdfActivity.this, "删除", Toast.LENGTH_SHORT).show();
                Iterator<Pdf> iterator = pdfList.iterator();
                while (iterator.hasNext()) {
                    Pdf value = iterator.next();
                    if (value.equals(pdf)) {
                        iterator.remove();
                    }
                }
                adapter.notifyDataSetChanged();
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
        String name[]={"赵晓明","赵小红","刘晓明","刘小红","孙晓明","孙小红","王小明","王晓红","周晓明","周小红","李晓明","李晓红"};
        for(int i=0;i<name.length;i++){
            for(int j=0;j<2;j++){
                if(j==0){
                    String pdf1="";
                    Pdf temp=new Pdf(name[i]+"左脚的脚型报告",pdf1, R.drawable.file_pdf);
                    pdfList.add(temp);
                }else{
                    String pdf1="";
                    Pdf temp=new Pdf(name[i]+"右脚的脚型报告",pdf1, R.drawable.file_pdf);
                    pdfList.add(temp);
                }

            }
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
    public File getFromAssets(String fileName,File file){
        String result = "";
        InputStream in=null;
        OutputStream out = null;
        try {
            in = getResources().getAssets().open(fileName);
            out=new FileOutputStream(file);
            //获取文件的字节数   
            int lenght = in.available();
            //创建byte数组   
            byte[] buffer = new byte[lenght];
            //将文件中的数据读到byte数组中   
            int bytes= in.read(buffer);
            //.getString(buffer, ENCODING);
            //将读取的数据写到本地
            out.write(buffer, 0, bytes);
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            file.delete();
        }finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        return file;
    }
}