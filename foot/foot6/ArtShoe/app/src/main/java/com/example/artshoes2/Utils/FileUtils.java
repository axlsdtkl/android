package com.example.artshoes2.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.example.artshoes2.Foot;
import com.example.artshoes2.FootImage;
import com.example.artshoes2.user.UserInformation;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    //判断是否有sd卡
    public static boolean isHasSdCard(){
        String status= Environment.getExternalStorageState();
        if(status.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }else{
            return false;
        }
    }
    //创建app的文件夹
    public static String CreateAppFolder(){
        File file;
        String sDir;
        if(isHasSdCard()){
            sDir=Environment.getExternalStorageDirectory().toString()+"/ArtShoes";
        }else{
            sDir=Environment.getExternalStorageDirectory().toString()+"/ArtShoes";
        }
        file=new File(sDir);
        //不存在则创建文件
        if(!file.exists()){
            file.mkdirs();
        }
        return sDir;
    }
    //创建脚型信息文件夹，将来里面要放txt,用来放每个脚型的名字，左右脚，图片数量，还有各图片的地址
    public static String CreateFootMessageFolder(){
        File file;
        String path;
        path=CreateAppFolder()+"/FootMessage";
        file=new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        return path;
    }
    //创建存放obj的文件夹
    public static String CreateObjFolder(){
        File file;
        String path;
        path=CreateAppFolder()+"/FootObj";
        file=new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        return path;
    }
    //创建存放pdf的文件夹
    public static String CreatePdfFolder(){
        File file;
        String path;
        path=CreateAppFolder()+"/FootPdf";
        file=new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        return path;
    }
    //创建通信文件夹
    public static String CreateSendMessageFolder(){
        File file;
        String path;
        path=CreateAppFolder()+"/SendMessage";
        file=new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        return path;
    }
    //将脚型转化成txt文件
    public static void SaveFootToTxt(Foot foot){
        String content="";//content用来存要写入txt的字符串
        String sex;
        String RorLfoot;
        List<FootImage> footImageList;
        if(foot.sex==1){
            sex="男";
        }else{
            sex="女";
        }
        if(foot.foot==1){
            RorLfoot="左脚";
        }else{
            RorLfoot="右脚";
        }
        content=String.format("%s姓名：%s\n", content,foot.name);//在content字符串后增加关于姓名的信息，格式为姓名：username，换行，然后更新为新的content
        content=String.format("%s性别：%s\n", content,sex);
        content=String.format("%s左右脚：%s\n", content,RorLfoot);
        content=String.format("%s修改时间：%s\n", content,foot.datechange);
        content=String.format("%s图片数量：%s\n", content,foot.getFootImageList().size());
        footImageList=foot.getFootImageList();//读取当前脚型的图片信息表单
        for(int i=0;i<footImageList.size();i++){//循环整个List
            String imgpath;
            FootImage footImage=footImageList.get(i);//拿出表单的第i个元素
            imgpath=footImage.footimage_path;//取出这个元素的地址
            content=String.format("%s图片：%s\n", content,imgpath);
        }
        foot.reNewFileName();
        String file_path = CreateFootMessageFolder()+"/" + foot.filename + ".txt";//当前要放脚型txt的地址

        try {
            FileOutputStream fos = new FileOutputStream(file_path);
            fos.write(content.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        content="";
//        file_path = CreateFootMessageFolder()+"/" + "1" + ".txt";//当前要放脚型txt的地址
//        try {
//            FileOutputStream fos = new FileOutputStream(file_path);
//            fos.write(content.getBytes());
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    //下面表示将当前信息打包发给服务器
    public static int GetFootId(Foot foot) throws IOException {
        final String username=java.net.URLEncoder.encode(UserInformation.username,"utf-8");
        final String userfilename=java.net.URLEncoder.encode(foot.filename,"utf-8");
        final String path=UserInformation.httpurl+"/GetFootId?username="+username+"&filename="+userfilename;

        URL url=new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");//获取服务器数据
        connection.setReadTimeout(8000);//设置读取超时的毫秒数
        connection.setConnectTimeout(8000);//设置连接超时的毫秒数
        InputStream in = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String result = reader.readLine();//读取服务器进行逻辑处理后页面显示的数据
        System.out.println(result);
        UserInformation.footid=Integer.parseInt(result);
        return UserInformation.footid;
    }
    public static int  SendFootToServer(Foot foot,Context context){
        int flag=0;
        String filename=foot.filename;
        String HOST = UserInformation.httpurl+"/mustUpPicture";//服务器接口
        String imgpath;
        List<FootImage> footImageList;
        footImageList=foot.getFootImageList();
        for(int i=0;i<footImageList.size();i++){//将图片一张张发送
            FootImage footImage=footImageList.get(i);
            imgpath=footImage.footimage_path;
            Bitmap bitmap= BitmapFactory.decodeFile(imgpath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
            byte[] b = stream.toByteArray();
            String file= new String(Base64Coder.encodeLines(b));//图片转成Base64码发送
            HttpClient client = new DefaultHttpClient();
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("username", UserInformation.username));//后面添加相应标记为username的用户名
            formparams.add(new BasicNameValuePair("photofile", filename));
            formparams.add(new BasicNameValuePair("file", file));//发送图片
            formparams.add(new BasicNameValuePair("photoname", "" + (int) (Math.random() * 1000000)));
            HttpPost post = new HttpPost(HOST);
            UrlEncodedFormEntity entity;
            //下面是http发送过程
            try {
                entity = new UrlEncodedFormEntity(formparams, "UTF-8");
                post.addHeader("Accept",
                        "text/javascript, text/html, application/xml, text/xml");
                post.addHeader("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
                post.addHeader("Accept-Encoding", "gzip,deflate,sdch");
                post.addHeader("Connection", "Keep-Alive");
                post.addHeader("Cache-Control", "no-cache");
                post.addHeader("Content-Type", "application/x-www-form-urlencoded");
                post.setEntity(entity);
                HttpResponse response = client.execute(post);
                System.out.println(response.getStatusLine().getStatusCode());
                HttpEntity e = response.getEntity();
                System.out.println(EntityUtils.toString(e));
                if (200 == response.getStatusLine().getStatusCode()) {
                    System.out.println("上传完成");
                    flag=response.getStatusLine().getStatusCode();
                } else {
                    System.out.println("上传失败");
                    flag=response.getStatusLine().getStatusCode();
                }
                client.getConnectionManager().shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flag;
    }
    //将txt转回相应foot类
    public static void ReadTxtToFoot(File file, Foot foot, Context context){
        String name="";
        int sex=1;
        int LorRfoot=1;
        String datechange="";
        List<FootImage> footImageList=new ArrayList<FootImage>();
        try{
            FileInputStream stream=new FileInputStream(file);//打开文件路径地址
            byte[] b = new byte[stream.available()];
            stream.read(b);
            String readStr = new String(b);
//            Toast.makeText(context,readStr,Toast.LENGTH_LONG).show();
//            InputStreamReader isr=new InputStreamReader(stream);
//            BufferedReader br=new BufferedReader(isr);
//            String temps=null;
            String[] temp=readStr.split("\n");//按行读入字符串数组
            for(int i=0;i<temp.length;i++){//查找每一个元素
                String[] tempsa=temp[i].split("：");//以:为界线分开
//                Toast.makeText(context,"名字是"+tempsa[0],Toast.LENGTH_LONG).show();
                switch(tempsa[0].trim()){
                    case "姓名":
                        name=tempsa[1].trim();
                        break;
                    case "性别":
                        if(tempsa[1].trim().equals("男")){
                            sex=1;
                        }else{
                            sex=2;
                        }
                        break;
                    case "左右脚":
                        if(tempsa[1].trim().equals("左脚")){
                            LorRfoot=1;
                        }else{
                            LorRfoot=2;
                        }
                        break;
                    case "修改时间":
                        datechange=tempsa[1];
                        break;
                    case "图片":
                        FootImage footImage=new FootImage(tempsa[1].trim());
                        footImageList.add(footImage);
                        break;
                    default:
                        break;

                }
            }
//            Toast.makeText(context,"名字是"+name,Toast.LENGTH_LONG).show();
            //相应的修改当前foot的信息，外界调用时的foot就是这个，相应修改。
            foot.name=name;
            foot.sex=sex;
            foot.foot=LorRfoot;
            foot.datechange=datechange;
            foot.footimageList=footImageList;
            foot.reNewFileName();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    //这个是用来复制pdf文件，将前面的文件pdf写到后面一个位置的pdf
    public static void inputStream2File(InputStream is, String filepath) throws IOException {
        File file=new File(filepath);
        file.getParentFile().mkdirs();//创建本文件
        FileOutputStream fos=new FileOutputStream(new File(filepath));
        byte[] buffer=new byte[1024];
        int byteCount=0;
        // 循环从输入流读取字节
        while((byteCount=is.read(buffer))!=-1)//直到整个文件读取完
        {
            // 将读取的输入流写入到输出流
            fos.write(buffer,0,byteCount);
        }
        // 刷新缓冲区
        fos.flush();
        fos.close();
        is.close();
    }
}
