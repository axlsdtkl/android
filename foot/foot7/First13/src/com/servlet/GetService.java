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
/*得到服务器的状态
 *是否服务器正在重建中
 *是否服务器已经完成了重建
 *通过数据库中的state状态来告诉客户端当前情况
 */

public class GetService extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetService() {
        super();
    }
    public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception e) {
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
		PreparedStatement sql; 
		
		String question = request.getParameter("question").trim();
		String uri = "jdbc:mysql://localhost/makefriend?useSSL=false&serverTimezone=UTC";
		if(question.equals("state")) {
			try {
				con = DriverManager.getConnection(uri,"root","gunxueqiu");
				//查询服务器当前的state状态
				String condition = "select * from buildingstate where building = 'bat'";
				sql = con.prepareStatement(condition);
				ResultSet rSet = sql.executeQuery(condition);
				if(rSet.next()) {
					//获取数据库state状态
					String state=rSet.getString("state");
					System.out.println(state);
					out.println(state);
					con.close();
					sql.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
