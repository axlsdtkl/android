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
 * �����û���username�ͽ����ļ���
 * �����ݿ����ҵ���Ӧ��ID
 * ������ڴ�ID���򷵻�
 * ���û�д�ID�������mysql�����ݿ�Ĵ���
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
		
		//usernameΪ�û���
		//filenameΪ�����ļ������磺����������)
		String username = java.net.URLDecoder.decode(request.getParameter("username"),"utf-8");
		String filename = java.net.URLDecoder.decode(request.getParameter("filename"),"utf-8");
		String operation = java.net.URLDecoder.decode(request.getParameter("operation"),"utf-8");
		//�������ݿ�
		String uri = "jdbc:mysql://localhost/makefriend?useSSL=false&serverTimezone=UTC";
		try {
			con = DriverManager.getConnection(uri,UserInformation.mysqluser,UserInformation.mysqlpass);
			//���ҵ�ǰ�û��Ĵ˽����Ƿ������ݿ���
			String condition = "select * from footid where username = '"+username+"'"+" and filename = '"+filename+"'";
			boolean haveuser=false;
			sql = con.prepareStatement(condition);
			ResultSet rSet = sql.executeQuery(condition);
			//�������������ͣ���ֱ�Ӵ����ݿ���ȡ����Ӧ��id��
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
					//�����ݿ�����������û�������Ϣ
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
					
					
					//���������ظ�id
					int id;
					for(;;) {
						//�������id�ţ�Ϊ6λ
						id=((int)(Math.random() * 1000000));
						System.out.println("123");
						//Ѱ���Ƿ������Ӧid����������id�ظ�
						String condition1 = "select * from footid where id = "+id;
						System.out.println(condition1);
						sql = con.prepareStatement(condition1);
						ResultSet rSet1 = sql.executeQuery(condition1);
						System.out.println("now"+id);
						//�������id�ɹ����򷵻ؿͻ���yes
						if(rSet1.next()) {
							System.out.println("yes");
							continue;
						}
						else {//����id���ɹ����򷵻ؿͻ���no
							System.out.println("no");
							break;
						}
					}
					
					//����ǰid�������ݿ�
					//�����������Ϣ��Ϊ�û����������ļ�����id�š�������Ӧ�ļ�Ŀ¼����ʽΪfiles/username/filename),���ɵ�obj��pdfȫ·������ʽΪfiles/username/filename/filename)
					String sqladd="insert into footid(username,filename,id,url,path,state)values(?,?,?,?,?,?)";
					PreparedStatement ps=con.prepareStatement(sqladd);
					
					//���������ݿ��д�����Ӧ���û����������ļ�������id��������Ӧ�ļ�Ŀ¼��Ϣ��objpdf�ľ���·��
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
