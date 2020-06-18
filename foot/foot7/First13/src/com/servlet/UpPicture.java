package com.servlet;
/*
 * 本函数的效果相当于是让图片传上来服务器接受
 * 然后服务器将图片信息传至三维重建文件夹
 * 调用bat，然后bat再调用python接口进行三维重建
 * 最后将重建后结果复制到用户目录，然后删除清理三维重建里面的相应图片
 */

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
		
		//通过http协议将信息读取
		System.out.println(2);
		//file是图片信息，进行了相应base64转码
		file=new String(req.getParameter("file").getBytes("iso-8859-1"), "utf-8");
		//username用户名
		username=new String(req.getParameter("username").getBytes("iso-8859-1"), "utf-8");
		//脚型名
		photofile=new String(req.getParameter("photofile").getBytes("iso-8859-1"), "utf-8");
		//每张脚的照片名
		photoname=new String(req.getParameter("photoname").getBytes("iso-8859-1"), "utf-8");
		//mark是来判断当前照片是不是这一组的最后一张，因为前台是一张张发送的，最后一张发送结束，我们再开始处理这些照片
		mark=new String(req.getParameter("mark").getBytes("iso-8859-1"), "utf-8");
		//当前是第几张照片
		rank=new String(req.getParameter("rank").getBytes("iso-8859-1"), "utf-8");
		
		//如果是第一张照片
		if(rank.equals("0")) {
	        try{
		        Connection con;
	    		PreparedStatement sql; 
	        	String uri = "jdbc:mysql://localhost/makefriend?useSSL=false&serverTimezone=UTC";
				con = DriverManager.getConnection(uri,UserInformation.mysqluser,UserInformation.mysqlpass);
				//将当前脚型id的state变成0,因为只要一上传成功，就意味着这个脚型已经是新的了，需要重建进行三维重建
				String condition = "update footid set state = '0' where username = '"+username+"' and filename = '"+photofile+"'";
				sql = con.prepareStatement(condition);
				sql.executeUpdate();
				sql.close();
				con.close();
			}catch (SQLException e) {
				e.printStackTrace();
			}
	        
	        //得到要存的地方的地址
			String path=req.getSession().getServletContext().getRealPath("/files");
			//连上用户名，连上脚型名
			path=path+"/"+username+"/"+photofile;
			//先递归调用删除这个文件里面的所有无关信息
	        deleteFile(path,0);
		}
		System.out.println(username);
		System.out.println(photofile);
		System.out.println(photoname);
		System.out.println(mark);
		
		//如果文件确实是传上来的
		if(file!=null){
			//对base64编码的图片进行解码
			byte[] b= Base64Coder.decodeLines(file);
			//如果没有files文件夹，则创建，用来放以后的图片信息
			String filepath=req.getSession().getServletContext().getRealPath("/files");
			File file=new File(filepath);
			if(!file.exists())
				file.mkdirs();
			//接下来创建这个用户的文件夹
			filepath=filepath+"/"+username;
			System.out.println(filepath);
			file=new File(filepath);
			if(!file.exists())
				file.mkdirs();
			//创建这个脚型的文件夹
			filepath=filepath+"/"+photofile;
			file=new File(filepath);
			if(!file.exists())
				file.mkdirs();
			//写入这个图片，后缀名jpg
			FileOutputStream fos=new FileOutputStream(file.getPath()+'/'+photoname+".jpg");
			System.out.println(file.getPath());
			fos.write(b);
			fos.flush();
			fos.close();
		}
		
		
		
		//如果这是本脚型的最后一张图片，我们这里用前端传上来1作为标记，表示已经传完这个脚型了
		if(mark.equals("1")) {
	    	try{
	    		Connection con;
	    		PreparedStatement sql; 
	        	String uri = "jdbc:mysql://localhost/makefriend?useSSL=false&serverTimezone=UTC";
				con = DriverManager.getConnection(uri,UserInformation.mysqluser,UserInformation.mysqlpass);
				//找到数据库bat
				String condition = "select * from buildingstate where building = 'bat'";
				sql = con.prepareStatement(condition);
				ResultSet rSet = sql.executeQuery(condition);
				//如果成功则返回给客户端登录成功
				if(rSet.next()) {
					String state=rSet.getString("state");
					//如果是rest状态，则把服务器状态锁死，状态变为running，这样的目的是防止其它进程也来访问这个重建过程，然后形成了混乱
					//所以这里的重建处理其实是单进程的，从商业化角度，为了提高吞吐量，以后是得改成多线程的
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
			//删除重建文件模型目录里的所有无关文件
			deleteFile(UserInformation.buildfile+"\\example",0);
			//找到当前脚型的目录，打算进去拷贝
			String path=req.getSession().getServletContext().getRealPath("/files");
			path=path+"/"+username+"/"+photofile;
			boolean flag = false;
	        File file = new File(path);
	        //取出本脚型图片的集合
	        File[] tempList = file.listFiles();
	        ArrayList<String> fileNameList = null;
	        int cnt=0;
	        //下面是一张张的复制过程
	        for (int i = 0; i < tempList.length; i++) {
	            if (tempList[i].isFile()) {
	            	String filename=tempList[i].getName();
	            	//取出从.开始后面的文件后缀
	            	String fileclass= filename.substring(filename.indexOf(".")+1);
	            	System.out.println(fileclass);
	            	//如果文件后缀不是jpg，则不用复制
	            	if(!fileclass.equals("jpg"))continue;
	            	System.out.println("文     件：" + tempList[i]);
	            	cnt++;
	            	//移动函数
	            	movefile(""+tempList[i],UserInformation.buildfile+"//example",""+cnt+".jpg");
	            }
	        }
	        nowfile=path;
	        try {
	        	//把相机内外参K.txt这个文件也移动到这个深度学习模型文件中
				movefile(UserInformation.buildfile+"\\K.txt",UserInformation.buildfile+"\\example","K.txt");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        
	        //开启线程进行脚本运行，开线程的目的是让代码仍然运行到后面，毕竟前端只是上传图片而已，不用等待重建完就可以结束发送这个过程
	        Thread t = new Thread(new Runnable(){  
	            public void run(){
	            	//调用重建目录中的bat脚本
	            	String operation="cmd /c "+UserInformation.buildfile+"\\a.bat";
	    			Process process = null;
					try {
						//执行这个操作
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
	    	        	//我们完成了重建，将obj和pdf复制到用户的脚型文件夹中，方便用户直接下载
						movefile(UserInformation.buildfile+"//example//optimize//df_icp.obj",nowfile,photofile+".obj");
						movefile(UserInformation.buildfile+"//1.pdf",nowfile,photofile+".pdf");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    	        //在数据库中将当前脚型状态改为1，已经重建完
	    	        updata(username,photofile,1);
	    	        //删除重建模型文件夹中的所有图片和文件
	    	        deleteFile(UserInformation.buildfile+"\\example",0);
	    	        
	    	        try{
			    		Connection con;
			    		PreparedStatement ps;
			    		//重新将数据库状态改为rest,这样其它进程可以继续调用脚本
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
	//将username这个用户的photofile这个脚型状态在数据库中改为1，表示已经重建完
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
