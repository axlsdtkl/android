package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.cj.x.protobuf.MysqlxExpr.Identifier;

/**
 * Servlet implementation class LoginTest
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
		
		String logname = request.getParameter("logname").trim();
		String password = request.getParameter("password").trim();
		String uri = "jdbc:mysql://localhost/MakeFriend?useSSL=false&serverTimezone=UTC";
		try {
			con = DriverManager.getConnection(uri,"root","gunxueqiu");
			String condition = "select * from member where logname = '"+logname+"'";
			boolean haveuser=false;
			sql = con.prepareStatement(condition);
			ResultSet rSet = sql.executeQuery(condition);
			if(rSet.next()) {
				haveuser=true;
			}
			if(haveuser==false) {
				con = DriverManager.getConnection(uri,"root","gunxueqiu");
				String sqladd="insert into member(logname,password)values(?,?)";
				PreparedStatement ps=con.prepareStatement(sqladd);
				ps.setString(1,logname);
				System.out.println("name:"+logname);
				ps.setString(2,password);
				int row=ps.executeUpdate();
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
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
