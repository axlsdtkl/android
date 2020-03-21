package com.example.artshoes2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

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
        if(footimage.getFootImagePath()==" "){
            footimage_preview.setImageResource(R.drawable.ic_addpic);
        }else if(footimage.getFootImagePath()!=null){
            Bitmap bitmap= BitmapFactory.decodeFile(footimage.getFootImagePath());
            Bitmap compressbitmap = CropAndCompressBitmap(bitmap);
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
