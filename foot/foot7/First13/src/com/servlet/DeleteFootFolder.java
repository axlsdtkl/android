package com.servlet;

import java.io.File;
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
/**
 * 删除操作，把当前用户的某个脚型文件夹进行删除操作
 */
public class DeleteFootFolder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteFootFolder() {
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

		//因为可能会出现中文用户名，Tomcat有些版本不支持直接中文路径访问，所以在安卓端的通信部分我加入了一层utf-8加密转化
		//在服务端相应的要进行utf-8的解密
		//下面的为解码
		//用户名
		String username = java.net.URLDecoder.decode(request.getParameter("username"),"utf-8");
		//用户当前要删除的脚型文件名（如：赵晓红的左脚)
		String filename = java.net.URLDecoder.decode(request.getParameter("filename"),"utf-8");

		
		System.out.println(username);
		System.out.println(filename);
		try {
			
			String uri = "jdbc:mysql://localhost/makefriend?useSSL=false&serverTimezone=UTC";
			con = DriverManager.getConnection(uri,UserInformation.mysqluser,UserInformation.mysqlpass);
			String condition = "delete from footid where username = '"+username+"'"+" and filename = '"+filename+"'";
			System.out.println(condition);
			sql = con.prepareStatement(condition);
			sql.execute(condition);
			
			
			String filepath=request.getSession().getServletContext().getRealPath("/files");
			
			//对于要删除的文件进行递归删除
			deleteFile(filepath+"/"+username+"/"+filename);
			out.println("删除成功!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void deleteFile(String filePath) {
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
				file.delete();
			}else//否则进行递归删除
			{
				for (int i = 0; i < files.length; i++) 
				{
					deleteFile(files[i].getAbsolutePath());
				}
				file.delete();
			}
		}
		System.out.println("文件已经被成功删除");
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
