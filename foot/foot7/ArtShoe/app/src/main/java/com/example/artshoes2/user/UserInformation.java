package com.example.artshoes2.user;

public class UserInformation {
    public static String username;//static全局变量，保存下当前登录用户的用户名，每次登录成功后就保存下来，上传时会发把用户名发给服务器
    public static int downloadid;
    public static String getUsername() {
        return username;
    }
    public static void setUsername(String username) {
        UserInformation.username=username;
    } //用户名
    //public static String httpurl="http://footapp.gunxueqiu.wang:8080";//地址，根据具体情况修改
    //public static String httpurl="http://123.57.245.186/First13/";//地址，根据具体情况修改
    //public static String httpurl="http://192.168.191.1:8080/First13/";
    //public static String httpurl="http://123.57.245.186";//地址，根据具体情况修改
    public static String httpurl="http://106.54.124.42/First13/";
    public static int footid=0;//当前要上传的脚型的id,这个由服务器返回，之后通过这个id进行图片信息上传，下载相应obj和pdf也通过这个id下载
    public static String path="";//表示obj,pdf的相当地址，注意这个是不全的，前面还要加上httpurl，后面还得选择性加上.obj或者.pdf
    public static int downloadprogress=0;//下载进度
    public static String buildstate="";
}