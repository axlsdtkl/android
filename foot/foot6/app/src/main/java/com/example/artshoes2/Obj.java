package com.example.artshoes2;

//obj类，文件名，文件地址
public class Obj {
    private String filename;
    private int imageid;
    private String filepath;
    public Obj(String filename, String filepath, int imageid){
        this.filename=filename;
        this.filepath=filepath;
        this.imageid=imageid;
    }
    public String getFileName(){
        return filename;
    }
    public String getFilePath(){
        return filepath;
    }
    public int getImageId(){
        return imageid;
    }
}
