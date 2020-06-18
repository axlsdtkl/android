package com.servlet;
/*
 * ��������Ч���൱������ͼƬ����������������
 * Ȼ���������ͼƬ��Ϣ������ά�ؽ��ļ���
 * ����bat��Ȼ��bat�ٵ���python�ӿڽ�����ά�ؽ�
 * ����ؽ��������Ƶ��û�Ŀ¼��Ȼ��ɾ��������ά�ؽ��������ӦͼƬ
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
		
		//ͨ��httpЭ�齫��Ϣ��ȡ
		System.out.println(2);
		//file��ͼƬ��Ϣ����������Ӧbase64ת��
		file=new String(req.getParameter("file").getBytes("iso-8859-1"), "utf-8");
		//username�û���
		username=new String(req.getParameter("username").getBytes("iso-8859-1"), "utf-8");
		//������
		photofile=new String(req.getParameter("photofile").getBytes("iso-8859-1"), "utf-8");
		//ÿ�Žŵ���Ƭ��
		photoname=new String(req.getParameter("photoname").getBytes("iso-8859-1"), "utf-8");
		//mark�����жϵ�ǰ��Ƭ�ǲ�����һ������һ�ţ���Ϊǰ̨��һ���ŷ��͵ģ����һ�ŷ��ͽ����������ٿ�ʼ������Щ��Ƭ
		mark=new String(req.getParameter("mark").getBytes("iso-8859-1"), "utf-8");
		//��ǰ�ǵڼ�����Ƭ
		rank=new String(req.getParameter("rank").getBytes("iso-8859-1"), "utf-8");
		
		//����ǵ�һ����Ƭ
		if(rank.equals("0")) {
	        try{
		        Connection con;
	    		PreparedStatement sql; 
	        	String uri = "jdbc:mysql://localhost/makefriend?useSSL=false&serverTimezone=UTC";
				con = DriverManager.getConnection(uri,UserInformation.mysqluser,UserInformation.mysqlpass);
				//����ǰ����id��state���0,��ΪֻҪһ�ϴ��ɹ�������ζ����������Ѿ����µ��ˣ���Ҫ�ؽ�������ά�ؽ�
				String condition = "update footid set state = '0' where username = '"+username+"' and filename = '"+photofile+"'";
				sql = con.prepareStatement(condition);
				sql.executeUpdate();
				sql.close();
				con.close();
			}catch (SQLException e) {
				e.printStackTrace();
			}
	        
	        //�õ�Ҫ��ĵط��ĵ�ַ
			String path=req.getSession().getServletContext().getRealPath("/files");
			//�����û��������Ͻ�����
			path=path+"/"+username+"/"+photofile;
			//�ȵݹ����ɾ������ļ�����������޹���Ϣ
	        deleteFile(path,0);
		}
		System.out.println(username);
		System.out.println(photofile);
		System.out.println(photoname);
		System.out.println(mark);
		
		//����ļ�ȷʵ�Ǵ�������
		if(file!=null){
			//��base64�����ͼƬ���н���
			byte[] b= Base64Coder.decodeLines(file);
			//���û��files�ļ��У��򴴽����������Ժ��ͼƬ��Ϣ
			String filepath=req.getSession().getServletContext().getRealPath("/files");
			File file=new File(filepath);
			if(!file.exists())
				file.mkdirs();
			//��������������û����ļ���
			filepath=filepath+"/"+username;
			System.out.println(filepath);
			file=new File(filepath);
			if(!file.exists())
				file.mkdirs();
			//����������͵��ļ���
			filepath=filepath+"/"+photofile;
			file=new File(filepath);
			if(!file.exists())
				file.mkdirs();
			//д�����ͼƬ����׺��jpg
			FileOutputStream fos=new FileOutputStream(file.getPath()+'/'+photoname+".jpg");
			System.out.println(file.getPath());
			fos.write(b);
			fos.flush();
			fos.close();
		}
		
		
		
		//������Ǳ����͵����һ��ͼƬ������������ǰ�˴�����1��Ϊ��ǣ���ʾ�Ѿ��������������
		if(mark.equals("1")) {
	    	try{
	    		Connection con;
	    		PreparedStatement sql; 
	        	String uri = "jdbc:mysql://localhost/makefriend?useSSL=false&serverTimezone=UTC";
				con = DriverManager.getConnection(uri,UserInformation.mysqluser,UserInformation.mysqlpass);
				//�ҵ����ݿ�bat
				String condition = "select * from buildingstate where building = 'bat'";
				sql = con.prepareStatement(condition);
				ResultSet rSet = sql.executeQuery(condition);
				//����ɹ��򷵻ظ��ͻ��˵�¼�ɹ�
				if(rSet.next()) {
					String state=rSet.getString("state");
					//�����rest״̬����ѷ�����״̬������״̬��Ϊrunning��������Ŀ���Ƿ�ֹ��������Ҳ����������ؽ����̣�Ȼ���γ��˻���
					//����������ؽ�������ʵ�ǵ����̵ģ�����ҵ���Ƕȣ�Ϊ��������������Ժ��ǵøĳɶ��̵߳�
					if(state.equals("rest")) {
						condition = "update buildingstate set state = 'running' where building = 'bat'";
						sql = con.prepareStatement(condition);
						sql.executeUpdate();
						sql.close();
						con.close();
					}else {
					    Thread.sleep(1000*10);   // ����10��
					}
				}
			}catch (SQLException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//ɾ���ؽ��ļ�ģ��Ŀ¼��������޹��ļ�
			deleteFile(UserInformation.buildfile+"\\example",0);
			//�ҵ���ǰ���͵�Ŀ¼�������ȥ����
			String path=req.getSession().getServletContext().getRealPath("/files");
			path=path+"/"+username+"/"+photofile;
			boolean flag = false;
	        File file = new File(path);
	        //ȡ��������ͼƬ�ļ���
	        File[] tempList = file.listFiles();
	        ArrayList<String> fileNameList = null;
	        int cnt=0;
	        //������һ���ŵĸ��ƹ���
	        for (int i = 0; i < tempList.length; i++) {
	            if (tempList[i].isFile()) {
	            	String filename=tempList[i].getName();
	            	//ȡ����.��ʼ������ļ���׺
	            	String fileclass= filename.substring(filename.indexOf(".")+1);
	            	System.out.println(fileclass);
	            	//����ļ���׺����jpg�����ø���
	            	if(!fileclass.equals("jpg"))continue;
	            	System.out.println("��     ����" + tempList[i]);
	            	cnt++;
	            	//�ƶ�����
	            	movefile(""+tempList[i],UserInformation.buildfile+"//example",""+cnt+".jpg");
	            }
	        }
	        nowfile=path;
	        try {
	        	//����������K.txt����ļ�Ҳ�ƶ���������ѧϰģ���ļ���
				movefile(UserInformation.buildfile+"\\K.txt",UserInformation.buildfile+"\\example","K.txt");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        
	        //�����߳̽��нű����У����̵߳�Ŀ�����ô�����Ȼ���е����棬�Ͼ�ǰ��ֻ���ϴ�ͼƬ���ѣ����õȴ��ؽ���Ϳ��Խ��������������
	        Thread t = new Thread(new Runnable(){  
	            public void run(){
	            	//�����ؽ�Ŀ¼�е�bat�ű�
	            	String operation="cmd /c "+UserInformation.buildfile+"\\a.bat";
	    			Process process = null;
					try {
						//ִ���������
						process = Runtime.getRuntime().exec(operation);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    	        //�����ҵ�python�������д�����������java�γ����޵���������������Ķ���Ҫ���϶�ȡ
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
	    	        	//����������ؽ�����obj��pdf���Ƶ��û��Ľ����ļ����У������û�ֱ������
						movefile(UserInformation.buildfile+"//example//optimize//df_icp.obj",nowfile,photofile+".obj");
						movefile(UserInformation.buildfile+"//1.pdf",nowfile,photofile+".pdf");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    	        //�����ݿ��н���ǰ����״̬��Ϊ1���Ѿ��ؽ���
	    	        updata(username,photofile,1);
	    	        //ɾ���ؽ�ģ���ļ����е�����ͼƬ���ļ�
	    	        deleteFile(UserInformation.buildfile+"\\example",0);
	    	        
	    	        try{
			    		Connection con;
			    		PreparedStatement ps;
			    		//���½����ݿ�״̬��Ϊrest,�����������̿��Լ������ýű�
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
	//��username����û���photofile�������״̬�����ݿ��и�Ϊ1����ʾ�Ѿ��ؽ���
	protected void updata(String username, String photofile,int state) {
		Connection con;
		PreparedStatement ps;
		String uri = "jdbc:mysql://localhost/makefriend?useSSL=false&serverTimezone=UTC";
		try {
			con = DriverManager.getConnection(uri,UserInformation.mysqluser,UserInformation.mysqlpass);
			//���ҵ�ǰ�û��Ĵ˽����Ƿ������ݿ���
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
		//�ҵ��ļ�
		File file=new File(filePath);
		//���ֱ�����ļ�����ɾ��
		if(file.isFile())
		{
			file.delete();
		}else	//�ļ��е����
		{
			//�ҵ�ǰ�ļ���Ŀ¼���
			File[] files = file.listFiles();
			//����ǿյģ���ɾ����ǰ�ļ���
			if(files == null)
			{
				if(cs>0)file.delete();
			}else//������еݹ�ɾ��
			{
				for (int i = 0; i < files.length; i++) 
				{
					deleteFile(files[i].getAbsolutePath(),cs+1);
				}
				if(cs>0)file.delete();
			}
		}
		System.out.println("�ļ��Ѿ����ɹ�ɾ��");
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
