package com.example.artshoes2;

//pdf类的文件名，文件id，文件路径
public class Pdf {
    public String filename;
    public int imageid;
    public String filepath;
    public Pdf(String filename, String filepath, int imageid){
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
