//脚型图片类，主要存这个脚型图片的文件地址
package com.example.artshoes2;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

public class FootImage implements Parcelable {
    public String footimage_path;
    public FootImage(String footimage_path) {
        this.footimage_path=footimage_path;
    }
    public String getFootImagePath(){
        return footimage_path;
    }
    @Override
    public int describeContents(){
        return 0;
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(footimage_path);
    }
    public static final Creator<FootImage> CREATOR=new Creator<FootImage>(){
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public FootImage createFromParcel(Parcel source){
            String temp=source.readString();
            FootImage footImage=new FootImage(temp);
            return footImage;
        }
        @Override
        public FootImage[] newArray(int size){
            return new FootImage[size];
        }
    };
}
