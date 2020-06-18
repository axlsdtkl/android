package com.example.artshoes2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ObjAdapter extends ArrayAdapter<Obj> {
    private int resourceId;
    public ObjAdapter(Context context, int textViewResourceId, List<Obj> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Obj obj=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,null);
        ImageView objImage=(ImageView)view.findViewById(R.id.obj_image);
        TextView objName=(TextView)view.findViewById(R.id.obj_name);
        objImage.setImageResource(obj.getImageId());
        objName.setText(obj.getFileName());
        return view;
    }
}
