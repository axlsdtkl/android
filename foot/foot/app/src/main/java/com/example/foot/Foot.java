package com.example.foot;


public class Foot {
    private String name;        //脚名
    private int imageId;        //脚的图片资源id

    public Foot(String name, int imageId) {//获得脚的名字和id
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {//取脚的名字
        return name;
    }

    public int getImageId() {
        return imageId;
    }//取脚的id号
}
