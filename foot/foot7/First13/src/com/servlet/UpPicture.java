package com.servlet;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.servlet.utils.Base64Coder;

import user.UserInformation;
import static com.servlet.MoveFunction.movefile;

public class UpPicture extends HttpServlet {

	private String file;
	private String username;
	private String photofile;
	private String photoname;
	private String mark;
	private String nowfile;
	private String rank;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
//		super.doPost(req, resp);
		
	}
	ArrayList<String> strArray = new ArrayList<String> (); 
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {	
		
		System.out.println(2);
		file=new String(req.getParameter("file").getBytes("iso-8859-1"), "utf-8");
		username=new String(req.getParameter("username").getBytes("iso-8859-1"), "utf-8");
		photofile=new String(req.getParameter("photofile").getBytes("iso-8859-1"), "utf-8");
		photoname=new String(req.getParameter("photoname").getBytes("iso-8859-1"), "utf-8");
		mark=new String(req.getParameter("mark").getBytes("iso-8859-1"), "utf-8");
		rank=new String(req.getParameter("rank").getBytes("iso-8859-1"), "utf-8");
		
		if(rank.equals("0")) {
	        try{
		        Connection con;
	    		PreparedStatement sql; 
	        	String uri = "jdbc:mysql://localhost/makefriend?useSSL=false&serverTimezone=UTC";
				con = DriverManager.getConnection(uri,UserInformation.mysqluser,UserInformation.mysqlpass);
				String condition = "update footid set state = '0' where username = '"+username+"' and filename = '"+photofile+"'";
				sql = con.prepareStatement(condition);
				sql.executeUpdate();
				sql.close();
				con.close();
			}catch (SQLException e) {
				e.printStackTrace();
			}
	        
			String path=req.getSession().getServletContext().getRealPath("/files");
			path=path+"/"+username+"/"+photofile;
	        deleteFile(path,0);
		}
		System.out.println(username);
		System.out.println(photofile);
		System.out.println(photoname);
		System.out.println(mark);
		
		if(file!=null){
			byte[] b= Base64Coder.decodeLines(file);
			String filepath=req.getSession().getServletContext().getRealPath("/files");
			File file=new File(filepath);
			if(!file.exists())
				file.mkdirs();
			filepath=filepath+"/"+username;
			System.out.println(filepath);
			file=new File(filepath);
			if(!file.exists())
				file.mkdirs();
			filepath=filepath+"/"+photofile;
			file=new File(filepath);
			if(!file.exists())
				file.mkdirs();
			FileOutputStream fos=new FileOutputStream(file.getPath()+'/'+photoname+".jpg");
			System.out.println(file.getPath());
			fos.write(b);
			fos.flush();
			fos.close();
		}
		
		
		
		
		if(mark.equals("1")) {
	    	try{
	    		Connection con;
	    		PreparedStatement sql; 
	        	String uri = "jdbc:mysql://localhost/makefriend?useSSL=false&serverTimezone=UTC";
				con = DriverManager.getConnection(uri,UserInformation.mysqluser,UserInformation.mysqlpass);
				String condition = "select * from buildingstate where building = 'bat'";
				sql = con.prepareStatement(condition);
				ResultSet rSet = sql.executeQuery(condition);
				//如果成功则返回给客户端登录成功
				if(rSet.next()) {
					String state=rSet.getString("state");
					if(state.equals("rest")) {
						condition = "update buildingstate set state = 'running' where building = 'bat'";
						sql = con.prepareStatement(condition);
						sql.executeUpdate();
						sql.close();
						con.close();
					}else {
					    Thread.sleep(1000*10);   // 休眠10秒
					}
				}
			}catch (SQLException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			deleteFile(UserInformation.buildfile+"\\example",0);
			String path=req.getSession().getServletContext().getRealPath("/files");
			path=path+"/"+username+"/"+photofile;
			boolean flag = false;
	        File file = new File(path);
	        File[] tempList = file.listFiles();
	        ArrayList<String> fileNameList = null;
	        int cnt=0;
	        for (int i = 0; i < tempList.length; i++) {
	            if (tempList[i].isFile()) {
	            	String filename=tempList[i].getName();
	            	String fileclass= filename.substring(filename.indexOf(".")+1);
	            	System.out.println(fileclass);
	            	if(!fileclass.equals("jpg"))continue;
	            	System.out.println("文     件：" + tempList[i]);
	            	cnt++;
	            	movefile(""+tempList[i],UserInformation.buildfile+"//example",""+cnt+".jpg");
	            }
	        }
	        nowfile=path;
	        try {
				movefile(UserInformation.buildfile+"\\K.txt",UserInformation.buildfile+"\\example","K.txt");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        
	        
	        Thread t = new Thread(new Runnable(){  
	            public void run(){
	            	String operation="cmd /c "+UserInformation.buildfile+"\\a.bat";
	    			Process process = null;
					try {
						process = Runtime.getRuntime().exec(operation);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    	        //由于我的python代码里有大量输出，会和java形成无限的阻塞，所以输出的东西要马上读取
	    	        new InputStreamRunnable(process.getErrorStream(), "Error").start();
	    	        new InputStreamRunnable(process.getInputStream(), "Info").start();
	    	        try {
	    				boolean code = process.waitFor(30,TimeUnit.MINUTES);
	    			} catch (InterruptedException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
	    	        process.destroy();
	    	        try {
						movefile(UserInformation.buildfile+"//example//optimize//df_icp.obj",nowfile,photofile+".obj");
						movefile(UserInformation.buildfile+"//1.pdf",nowfile,photofile+".pdf");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    	        
	    	        updata(username,photofile,1);
      
	    	        deleteFile(UserInformation.buildfile+"\\example",0);
	    	        
	    	        try{
			    		Connection con;
			    		PreparedStatement ps;
			        	String uri = "jdbc:mysql://localhost/makefriend?useSSL=false&serverTimezone=UTC";
						con = DriverManager.getConnection(uri,UserInformation.mysqluser,UserInformation.mysqlpass);
						String condition = "update buildingstate set state = 'rest' where building = 'bat'";
						ps = con.prepareStatement(condition);
						ps.executeUpdate();
						ps.close();
						con.close();
					}catch (SQLException e) {
						e.printStackTrace();
					}
	    	        
	    	        
	            }});  
	        t.start();  

	        /**/
	        
		}
	}
	protected void updata(String username, String photofile,int state) {
		Connection con;
		PreparedStatement ps;
		String uri = "jdbc:mysql://localhost/makefriend?useSSL=false&serverTimezone=UTC";
		try {
			con = DriverManager.getConnection(uri,UserInformation.mysqluser,UserInformation.mysqlpass);
			//查找当前用户的此脚型是否在数据库中
			String condition = "update footid set state = ? where username = '"+username+"'"+" and filename = '"+photofile+"'";
			System.out.println(condition);
			ps = con.prepareStatement(condition);
			ps.setLong(1,1);
			ps.executeUpdate();
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private void deleteFile(String filePath,int cs)  {
		//找到文件
		File file=new File(filePath);
		//如果直接是文件，则删除
		if(file.isFile())
		{
			file.delete();
		}else	//文件夹的情况
		{
			//找当前文件夹目录情况
			File[] files = file.listFiles();
			//如果是空的，则删除当前文件夹
			if(files == null)
			{
				if(cs>0)file.delete();
			}else//否则进行递归删除
			{
				for (int i = 0; i < files.length; i++) 
				{
					deleteFile(files[i].getAbsolutePath(),cs+1);
				}
				if(cs>0)file.delete();
			}
		}
		System.out.println("文件已经被成功删除");
	}
	class InputStreamRunnable extends Thread {
	    BufferedReader bReader = null;
	    String type = null;
	    public InputStreamRunnable(InputStream is, String _type) {
	        try {
	            bReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(is), "UTF-8"));
	            type = _type;
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
	    public void run() {
	        String line;
	        int lineNum = 0;
	        try {
	            while ((line = bReader.readLine()) != null) {
	                lineNum++;
	                System.out.println(type+":"+line);
	            }
	            bReader.close();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
	}
}
