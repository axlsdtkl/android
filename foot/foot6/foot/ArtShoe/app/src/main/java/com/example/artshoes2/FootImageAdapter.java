//这个是单个脚型观看界面里面的下面列表图片显示适配器重写
package com.example.artshoes2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.artshoes2.PicUnits.CropAndCompressBitmap;

public class FootImageAdapter extends ArrayAdapter<FootImage> {
    private int resourceId;
    public Context context1;
    public FootImageAdapter(Context context, int textViewResourceId, List<FootImage> objects){
        super(context,textViewResourceId,objects);
        context1=context;
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        FootImage footimage=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,null);
        ImageView footimage_preview=(ImageView)view.findViewById(R.id.iv_footimage_preview);
        footimage_preview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        if(footimage.getFootImagePath()==" "){//如果是空地址的话，则装配为加号图片
            footimage_preview.setImageResource(R.drawable.ic_addpic);
        }else if(footimage.getFootImagePath()!=null){
            Bitmap bitmap= BitmapFactory.decodeFile(footimage.getFootImagePath());//根据当前图片转化成Bitmap图（位图）
            Bitmap compressbitmap = CropAndCompressBitmap(bitmap);//进行图片的压缩，引用了CropAndCompressBitmap函数中的东西
            footimage_preview.setImageBitmap(compressbitmap);
        }else{
            Toast.makeText(context1,"failed get image",Toast.LENGTH_SHORT).show();
        }
//        Uri uri=Uri.fromFile(new File(footimage.getFootImagePath()));
//        Uri uri=footimage.getFootImagePreUri();
//        try {
//            Bitmap temp=PicUnits.getPreViewImageFromUri((Activity) context1,uri);
//            footimage_preview.setImageBitmap(temp);
//            footimage_preview.setScaleType(ImageView.ScaleType.CENTER);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        try {
//
//            footimage_preview.setImageBitmap(PicUnits.getBitmapFormUri((Activity) context1,uri));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return view;
    }
}
