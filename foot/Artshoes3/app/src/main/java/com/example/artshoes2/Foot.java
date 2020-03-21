package com.example.artshoes2;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

//脚型类，存放用户脚型的信息
public class Foot implements Parcelable {
    public String datechange;
    public int imageid;
    public String filename;
    public String name;
    //性别变量：1代表男,2代表女。
    public int sex;
    //脚的变量：1代表左脚，2代表右脚。
    public int foot;
    public List<FootImage> footimageList=new ArrayList<FootImage>();
    public Foot(String name, int sex, int foot,List<FootImage> footimageList,int imageid){
        this.name=name;
        this.sex=sex;
        this.foot=foot;
        this.footimageList=footimageList;
        this.imageid=imageid;
        if(foot==1){
            this.filename=name+"的"+"左脚";
        }else{
            this.filename=name+"的"+"右脚";
        }
    }
    public void reNewFileName(){
        if(foot==1){
            filename=name+"的"+"左脚";
        }else{
            filename=name+"的"+"右脚";
        }
    }
    public int getImageId(){
        return imageid;
    }
    public String getFileName(){
        return filename;
    }
    public String getName(){
        return name;
    }
    public int getSex(){
        return sex;
    }
    public int getFoot(){
        return foot;
    }
    public List<FootImage> getFootImageList(){
        return footimageList;
    }
    public void ChangeDate(String nowdate){
        datechange=nowdate;
    }
    public String GetChangeDate(){
        return datechange;
    }
    public int describeContents(){
        return 0;
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest,int flags){
        dest.writeString(name);
        dest.writeInt(sex);
        dest.writeInt(foot);
        dest.writeTypedList(footimageList);
        dest.writeInt(imageid);
    }
    public static final Creator<Foot> CREATOR=new Creator<Foot>(){
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public Foot createFromParcel(Parcel source){

            String name=source.readString();
            int sex=source.readInt();
            int foot=source.readInt();
            List<FootImage> footimageList=new ArrayList<FootImage>();
            source.readTypedList(footimageList,FootImage.CREATOR);
            int imageid=source.readInt();
            Foot foot1=new Foot(name,sex,foot,footimageList,imageid);
            return foot1;
        }
        @Override
        public Foot[] newArray(int size){
            return new Foot[size];
        }
    };

}
