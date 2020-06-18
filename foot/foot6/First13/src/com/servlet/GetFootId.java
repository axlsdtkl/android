package com.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

import static com.servlet.MoveFunction.movefile;
/**
 * Servlet implementation class LoginTest
 */
public class GetFootId extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetFootId() {
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		Connection con;
		Statement sql;
		
		String username = java.net.URLDecoder.decode(request.getParameter("username"),"utf-8");
		String filename = java.net.URLDecoder.decode(request.getParameter("filename"),"utf-8");
		String uri = "jdbc:mysql://localhost/MakeFriend?useSSL=false&serverTimezone=UTC";
		try {
			con = DriverManager.getConnection(uri,"root","gunxueqiu");
			String condition = "select * from footid where username = '"+username+"'"+" and filename = '"+filename+"'";
			boolean haveuser=false;
			sql = con.prepareStatement(condition);
			ResultSet rSet = sql.executeQuery(condition);
			if(rSet.next()) {
				haveuser=true;
				int id=rSet.getInt("id");
				out.println(""+id);
				con.close();
			}
			if(haveuser==false) {
				con = DriverManager.getConnection(uri,"root","gunxueqiu");
				String filepath=request.getSession().getServletContext().getRealPath("/files");
				File file=new File(filepath);
				if(!file.exists())
					file.mkdirs();
				filepath=filepath+"/"+username;
				file=new File(filepath);
				if(!file.exists())
					file.mkdirs();
				filepath=filepath+"/"+filename;
				file=new File(filepath);
				if(!file.exists())
					file.mkdirs();
				movefile(request.getSession().getServletContext().getRealPath("/files"),filepath,filename);
				
				int id=(int)(Math.random() * 1000000);
				String sqladd="insert into footid(username,filename,id,url,path)values(?,?,?,?,?)";
				PreparedStatement ps=con.prepareStatement(sqladd);
				ps.setString(1,username);
				ps.setString(2,filename);
				ps.setLong(3,id);
				ps.setString(4,filepath);
				ps.setString(5,"/files"+"/"+username+"/"+filename+"/"+filename);
				int row=ps.executeUpdate();
				if(row>0){
					out.println(""+id);
				}
				ps.close();
				con.close();
			}
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
