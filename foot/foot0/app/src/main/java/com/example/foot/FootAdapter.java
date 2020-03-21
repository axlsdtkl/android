package com.example.foot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FootAdapter extends ArrayAdapter<Foot>{
    private int resourceId;

    public FootAdapter(Context context, int textViewResourceId, List<Foot> objects) {
        super(context, textViewResourceId, objects);
        //textViewResourceId为ListView子项布局的Id
        resourceId = textViewResourceId;
    }

    //getView()方法在每个子项被滚动屏幕内的时候被调用
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Foot foot = getItem(position);    //获取当前项的Foot实例
        //通过resourceId来加载我们自己定义的item的布局
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ImageView footImage = (ImageView) view.findViewById(R.id.foot_image);
        TextView fruitName = (TextView) view.findViewById(R.id.foot_name);
        footImage.setImageResource(foot.getImageId());
        fruitName.setText(foot.getName());
        return view;        //返回我们自己定义的布局

    }
}