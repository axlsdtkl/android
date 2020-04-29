package com.example.artshoes2.user;

public class UserInformation {
    public static String username;//static全局变量，保存下当前登录用户的用户名，每次登录成功后就保存下来，上传时会发把用户名发给服务器
    public static int downloadid;
    public static String getUsername() {
        return username;
    }
    public static void setUsername(String username) {
        UserInformation.username=username;
    }
    public static String httpurl="http://192.168.191.1:8080/First13";
    public static int footid=0;
    public static String path="";
}