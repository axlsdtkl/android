package com.example.artshoes2.Utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.example.artshoes2.Foot;
import com.example.artshoes2.FootImage;
import com.example.artshoes2.PicUnits;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    public static String CreateAppFolder(){
        File file;
        String sDir;
        if(isHasSdCard()){
            sDir=Environment.getExternalStorageDirectory().toString()+"/ArtShoes";
        }else{
            sDir=Environment.getExternalStorageDirectory().toString()+"/ArtShoes";
        }
        file=new File(sDir);
        if(!file.exists()){
            file.mkdirs();
        }
        return sDir;
    }
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
    //将Foot信息转换成本地文件
    public static void SaveFootToTxt(Foot foot){
        String content="";
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
        content=String.format("%s姓名：%s\n", content,foot.name);
        content=String.format("%s性别：%s\n", content,sex);
        content=String.format("%s左右脚：%s\n", content,RorLfoot);
        content=String.format("%s修改时间：%s\n", content,foot.datechange);
        content=String.format("%s图片数量：%s\n", content,foot.getFootImageList().size());
        footImageList=foot.getFootImageList();
        for(int i=0;i<footImageList.size();i++){
            String imgpath;
            FootImage footImage=footImageList.get(i);
            imgpath=footImage.footimage_path;
            content=String.format("%s图片：%s\n", content,imgpath);
        }
        foot.reNewFileName();
        String file_path = CreateFootMessageFolder()+"/" + foot.filename + ".txt";
        try {
            FileOutputStream fos = new FileOutputStream(file_path);
            fos.write(content.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //将Foot信息转换成通信文件
    public static void SaveFootToFile(Foot foot){
        String content="";
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
        content=String.format("%s姓名：%s\n", content,foot.name);
        content=String.format("%s性别：%s\n", content,sex);
        content=String.format("%s左右脚：%s\n", content,RorLfoot);
        content=String.format("%s图片数量：%s\n", content,foot.getFootImageList().size());
        footImageList=foot.getFootImageList();
        for(int i=0;i<footImageList.size();i++){
            String pic;
            String imgpath;
            FootImage footImage=footImageList.get(i);
            imgpath=footImage.footimage_path;
            Bitmap bitmap= BitmapFactory.decodeFile(imgpath);
            pic= PicUnits.bitmapToString(bitmap);
            content=String.format("%s图片：%s\n", content,pic);
        }
        foot.reNewFileName();
        String file_path = CreateSendMessageFolder()+"/" + foot.filename + ".txt";
        try {
            FileOutputStream fos = new FileOutputStream(file_path);
            fos.write(content.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void ReadTxtToFoot(File file, Foot foot, Context context){
        String name="";
        int sex=1;
        int LorRfoot=1;
        String datechange="";
        List<FootImage> footImageList=new ArrayList<FootImage>();
        try{
            FileInputStream stream=new FileInputStream(file);
            byte[] b = new byte[stream.available()];
            stream.read(b);
            String readStr = new String(b);
//            Toast.makeText(context,readStr,Toast.LENGTH_LONG).show();
//            InputStreamReader isr=new InputStreamReader(stream);
//            BufferedReader br=new BufferedReader(isr);
//            String temps=null;
            String[] temp=readStr.split("\n");
            for(int i=0;i<temp.length;i++){
                String[] tempsa=temp[i].split("：");
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
    public static void inputStream2File(InputStream is, String filepath) throws IOException {
        File file=new File(filepath);
        file.getParentFile().mkdirs();
        FileOutputStream fos=new FileOutputStream(new File(filepath));
        byte[] buffer=new byte[1024];
        int byteCount=0;
        // 循环从输入流读取字节
        while((byteCount=is.read(buffer))!=-1)
        {
            // 将读取的输入流写入到输出流
            fos.write(buffer,0,byteCount);
        }
        // 刷新缓冲区
        fos.flush();
        fos.close();
        is.close();
    }
    public static String getFilePathByUri(Context context, Uri uri) {
        String path = null;
        // 以 file:// 开头的
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            path = uri.getPath();
            return path;
        }
        // 以 content:// 开头的，比如 content://media/extenral/images/media/17766
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (columnIndex > -1) {
                        path = cursor.getString(columnIndex);
                    }
                }
                cursor.close();
            }
            return path;
        }
        // 4.4及之后的 是以 content:// 开头的，比如 content://com.android.providers.media.documents/document/image%3A235700
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                if (isExternalStorageDocument(uri)) {
                    // ExternalStorageProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        path = Environment.getExternalStorageDirectory() + "/" + split[1];
                        return path;
                    }
                } else if (isDownloadsDocument(uri)) {
                    // DownloadsProvider
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                            Long.valueOf(id));
                    path = getDataColumn(context, contentUri, null, null);
                    return path;
                } else if (isMediaDocument(uri)) {
                    // MediaProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};
                    path = getDataColumn(context, contentUri, selection, selectionArgs);
                    return path;
                }
            }
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
