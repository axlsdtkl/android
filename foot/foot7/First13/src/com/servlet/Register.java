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
 * ������������ע�����
 * ����ǰ�˷��������û��������ڷ������˵����ݿ���в��أ������ظ�����д����û��������ظ�ǰ����Ӧ��Ϣ
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
		
		//ǰ�˴������û���������
		String logname = request.getParameter("logname").trim();
		String password = request.getParameter("password").trim();
		String uri = "jdbc:mysql://localhost/makefriend?useSSL=false&serverTimezone=UTC";
		try {
			con = DriverManager.getConnection(uri,"root","gunxueqiu");
			
			//mysql������䣬�б�ǰ�û����Ƿ��Ѿ�����
			String condition = "select * from member where logname = '"+logname+"'";
			boolean haveuser=false;
			sql = con.prepareStatement(condition);
			ResultSet rSet = sql.executeQuery(condition);
			
			//�����������û��������¼��
			if(rSet.next()) {
				haveuser=true;
			}
			//�������û���˵�����Խ���ע�ᣬ�����ע�����
			if(haveuser==false) {
				con = DriverManager.getConnection(uri,"root","gunxueqiu");
				
				//д����Ӧ���û���������
				String sqladd="insert into member(logname,password)values(?,?)";
				PreparedStatement ps=con.prepareStatement(sqladd);
				ps.setString(1,logname);
				System.out.println("name:"+logname);
				ps.setString(2,password);
				int row=ps.executeUpdate();
				
				//����ע���������ظ�ǰ�˳ɹ����
				if(row>0){
					out.println("ע��ɹ�!");
				}
				else {
					out.println("ע��ʧ��!");
				}
				ps.close();
				con.close();
			}
			else out.println("ע��ʧ�ܣ��û��Ѵ���!");
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
