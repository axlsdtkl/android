package com.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import user.UserInformation;


/**
 * Servlet implementation class LoginTest
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }
    public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		Connection con;
		Statement sql; 
		String logname = request.getParameter("logname").trim();
		System.out.println(logname);
		String password = request.getParameter("password").trim();
		System.out.print(1);
		String uri = "jdbc:mysql://localhost:3306/makefriend?useSSL=false&serverTimezone=UTC";
		
		try {
			con = DriverManager.getConnection(uri,UserInformation.mysqluser,UserInformation.mysqlpass);
			//查找mysql中是否存在当前username和匹配的password
			String condition = "select * from member where logname = '"+logname+"' and password = '"+password+"'";
			sql = con.prepareStatement(condition);
			ResultSet rSet = sql.executeQuery(condition);
			//如果成功则返回给客户端登录成功
			if(rSet.next()) {
				out.println("login successfully!");	
				/*Process process = Runtime.getRuntime().exec("cmd /c D:\\gxq\\sys\\foot\\online_rebuild_system\\online_rebuild_system\\data\\a.bat");
		        //由于我的python代码里有大量输出，会和java形成无限的阻塞，所以输出的东西要马上读取
		        new InputStreamRunnable(process.getErrorStream(), "Error").start();
		        new InputStreamRunnable(process.getInputStream(), "Info").start();
		        boolean code = process.waitFor(30,TimeUnit.MINUTES);
		        process.destroy();*/
			
				//Process process = Runtime.getRuntime().exec("cmd /c D:\\gxq\\sys\\foot\\online_rebuild_system\\online_rebuild_system\\data\\a.bat");
				//System.out.println(123);
				//String[] args1 = new String[]{"sh","-c", "sh /www/wwwroot/1.sh"};
				//String[] args1=new String[]{"/root/anaconda3/envs/tensorflow/bin/python","/data/wwwroot/default/First13/WEB-INF/classes/com/servlet/1.py"};
	            //Process process=Runtime.getRuntime().exec(args1);
				
		        //Process process = Runtime.getRuntime().exec("sh /data/hello.sh");
		        //由于我的python代码里有大量输出，会和java形成无限的阻塞，所以输出的东西要马上读取
		        //new InputStreamRunnable(process.getErrorStream(), "Error").start();
		        //new InputStreamRunnable(process.getInputStream(), "Info").start();
		        //boolean code = process.waitFor(30,TimeUnit.MINUTES);
		        //process.destroy();
			}
			else {//否则返回给客户端登录失败
				out.println("can not login!");
				//String[] args1 = new String[]{"sh","-c", "sh /www/wwwroot/footapp2.gunxueqiu.wang/2.sh"};
				//String[] args1=new String[]{"/root/anaconda3/envs/tensorflow/bin/python","/data/wwwroot/default/First13/WEB-INF/classes/com/servlet/1.py"};
	            //Process process=Runtime.getRuntime().exec(args1);
				
		        //Process process = Runtime.getRuntime().exec("sh /data/hello.sh");
		        //由于我的python代码里有大量输出，会和java形成无限的阻塞，所以输出的东西要马上读取
		        //new InputStreamRunnable(process.getErrorStream(), "Error").start();
		        //new InputStreamRunnable(process.getInputStream(), "Info").start();
		        //boolean code = process.waitFor(30,TimeUnit.MINUTES);
		        //process.destroy();
			}
			con.close();		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //catch (InterruptedException e) {
			 //TODO Auto-generated catch block
			//e.printStackTrace();
		//}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
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
