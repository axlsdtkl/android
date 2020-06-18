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
 * ɾ���������ѵ�ǰ�û���ĳ�������ļ��н���ɾ������
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

		//��Ϊ���ܻ���������û�����Tomcat��Щ�汾��֧��ֱ������·�����ʣ������ڰ�׿�˵�ͨ�Ų����Ҽ�����һ��utf-8����ת��
		//�ڷ������Ӧ��Ҫ����utf-8�Ľ���
		//�����Ϊ����
		//�û���
		String username = java.net.URLDecoder.decode(request.getParameter("username"),"utf-8");
		//�û���ǰҪɾ���Ľ����ļ������磺����������)
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
			
			//����Ҫɾ�����ļ����еݹ�ɾ��
			deleteFile(filepath+"/"+username+"/"+filename);
			out.println("ɾ���ɹ�!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void deleteFile(String filePath) {
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
				file.delete();
			}else//������еݹ�ɾ��
			{
				for (int i = 0; i < files.length; i++) 
				{
					deleteFile(files[i].getAbsolutePath());
				}
				file.delete();
			}
		}
		System.out.println("�ļ��Ѿ����ɹ�ɾ��");
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
