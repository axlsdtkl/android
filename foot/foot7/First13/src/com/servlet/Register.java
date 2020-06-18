package com.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

import com.mysql.cj.x.protobuf.MysqlxExpr.Identifier;
import com.servlet.Login.InputStreamRunnable;


/**
 * 本类用来进行注册操作
 * 根据前端发过来的用户和密码在服务器端的数据库进行查重，如无重复则进行创建用户，并返回给前端相应信息
 */
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
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
		
		System.out.print(1);
		
		//前端传来的用户名和密码
		String logname = request.getParameter("logname").trim();
		String password = request.getParameter("password").trim();
		String uri = "jdbc:mysql://localhost/makefriend?useSSL=false&serverTimezone=UTC";
		try {
			con = DriverManager.getConnection(uri,"root","gunxueqiu");
			
			//mysql查找语句，判别当前用户名是否已经存在
			String condition = "select * from member where logname = '"+logname+"'";
			boolean haveuser=false;
			sql = con.prepareStatement(condition);
			ResultSet rSet = sql.executeQuery(condition);
			
			//如果存在这个用户名，则记录下
			if(rSet.next()) {
				haveuser=true;
			}
			//不存在用户，说明可以进行注册，则进行注册操作
			if(haveuser==false) {
				con = DriverManager.getConnection(uri,"root","gunxueqiu");
				
				//写入相应的用户名和密码
				String sqladd="insert into member(logname,password)values(?,?)";
				PreparedStatement ps=con.prepareStatement(sqladd);
				ps.setString(1,logname);
				System.out.println("name:"+logname);
				ps.setString(2,password);
				int row=ps.executeUpdate();
				
				//根据注册结果，返回给前端成功与否
				if(row>0){
					out.println("注册成功!");
				}
				else {
					out.println("注册失败!");
				}
				ps.close();
				con.close();
			}
			else out.println("注册失败，用户已存在!");
		} catch (SQLException e) {
			e.printStackTrace();
		} //catch (InterruptedException e) {
			// TODO Auto-generated catch block
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
