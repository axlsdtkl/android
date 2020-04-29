package com.example.artshoes2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FootAdapter extends ArrayAdapter<Foot> {//脚型适配器，下面的都是重载显示
    private int resourceId;
    public FootAdapter(Context context, int textViewResourceId, List<Foot> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Foot foot=getItem(position);//得到当前位置的脚型
        View view= LayoutInflater.from(getContext()).inflate(resourceId,null);
        ImageView footImage=(ImageView)view.findViewById(R.id.foot_image);
        TextView footName=(TextView)view.findViewById(R.id.foot_name);
        TextView footDateChange=(TextView)view.findViewById(R.id.foot_datechange);
        footImage.setImageResource(foot.getImageId());//相应填充这个脚型的相应信息
        footName.setText(foot.getFileName());
        footDateChange.setText("修改于"+foot.GetChangeDate());
        return view;
    }
}
