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
 * ���������Ǹ���ǰ�˴�������id�ţ�����id�Ų������ݿ⣬�����ظ�ǰ����Ӧ��obj��pdf��·����ַ
 */
public class GetUrlById extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetUrlById() {
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
		
		//ǰ�˴�������Ӧid
		String footid = request.getParameter("id").trim();
		String uri = "jdbc:mysql://localhost/makefriend?useSSL=false&serverTimezone=UTC";
		try {
			con = DriverManager.getConnection(uri,UserInformation.mysqluser,UserInformation.mysqlpass);
			
			//mysql�в�����Ӧ��id���
			String condition = "select * from footid where id = '"+footid+"'";
			boolean haveuser=false;
			sql = con.prepareStatement(condition);
			ResultSet rSet = sql.executeQuery(condition);
			
			//����ҵ���Ӧ��id,�򷵻ظ��û�path,��pdf��obj����Ӧ���ص�ַ
			if(rSet.next()) {
				haveuser=true;
				String state=rSet.getString("state");
				if(state.equals("0")) {
					out.println("need more time");
					con.close();
				}
				else if(state.equals("1")){
					String path=rSet.getString("path");
					out.println(""+path);
					con.close();
				}

			}
			
			//��������ڣ��򷵻ظ�ǰ�˲����ڴ�id
			if(haveuser==false) {
				out.println("no such id");
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
