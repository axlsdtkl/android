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

import user.UserInformation;

import static com.servlet.MoveFunction.movefile;
/**
 * 根据用户名username和脚型文件名
 * 在数据库中找到相应的ID
 * 如果存在此ID，则返回
 * 如果没有此ID，则进行mysql中数据库的创建
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
		
		//username为用户名
		//filename为脚型文件名（如：赵晓红的左脚)
		String username = java.net.URLDecoder.decode(request.getParameter("username"),"utf-8");
		String filename = java.net.URLDecoder.decode(request.getParameter("filename"),"utf-8");
		String operation = java.net.URLDecoder.decode(request.getParameter("operation"),"utf-8");
		//查找数据库
		String uri = "jdbc:mysql://localhost/makefriend?useSSL=false&serverTimezone=UTC";
		try {
			con = DriverManager.getConnection(uri,UserInformation.mysqluser,UserInformation.mysqlpass);
			//查找当前用户的此脚型是否在数据库中
			String condition = "select * from footid where username = '"+username+"'"+" and filename = '"+filename+"'";
			boolean haveuser=false;
			sql = con.prepareStatement(condition);
			ResultSet rSet = sql.executeQuery(condition);
			//如果存在这个脚型，则直接从数据库中取出相应的id号
			if(rSet.next()) {
				haveuser=true;
				int id=rSet.getInt("id");
				out.println(""+id);
				con.close();
			}
			else if(operation.equals("1")){
				out.println("-1");
				con.close();
			}else if(operation.equals("0")) {
				if(haveuser==false) {
					//在数据库中生成这个用户脚型信息
					con = DriverManager.getConnection(uri,UserInformation.mysqluser,UserInformation.mysqlpass);
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
					//movefile(request.getSession().getServletContext().getRealPath("/files"),filepath,filename);
					movefile(request.getSession().getServletContext().getRealPath("/files")+"/1.pdf",filepath,filename+".pdf");
					movefile(request.getSession().getServletContext().getRealPath("/files")+"/1.obj",filepath,filename+".obj");
					
					
					//避免生成重复id
					int id;
					for(;;) {
						//生成随机id号，为6位
						id=((int)(Math.random() * 1000000));
						System.out.println("123");
						//寻找是否存在相应id，避免多脚型id重复
						String condition1 = "select * from footid where id = "+id;
						System.out.println(condition1);
						sql = con.prepareStatement(condition1);
						ResultSet rSet1 = sql.executeQuery(condition1);
						System.out.println("now"+id);
						//如果生成id成功，则返回客户端yes
						if(rSet1.next()) {
							System.out.println("yes");
							continue;
						}
						else {//生成id不成功，则返回客户端no
							System.out.println("no");
							break;
						}
					}
					
					//将当前id加入数据库
					//依次填入的信息量为用户名、脚型文件名、id号、脚型相应文件目录（格式为files/username/filename),生成的obj与pdf全路径（格式为files/username/filename/filename)
					String sqladd="insert into footid(username,filename,id,url,path,state)values(?,?,?,?,?,?)";
					PreparedStatement ps=con.prepareStatement(sqladd);
					
					//依次在数据库中存入相应的用户名、脚型文件夹名、id、脚型相应文件目录信息、objpdf的绝对路径
					ps.setString(1,username);
					ps.setString(2,filename);
					ps.setLong(3,id);
					ps.setString(4,filepath);
					ps.setString(5,"/files"+"/"+username+"/"+filename+"/"+filename);
					ps.setLong(6,0);
					int row=ps.executeUpdate();
					if(row>0){
						out.println(""+id);
					}
					ps.close();
					con.close();
				}
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
