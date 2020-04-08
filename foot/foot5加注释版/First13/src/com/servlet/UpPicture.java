package com.servlet;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.servlet.utils.Base64Coder;


public class UpPicture extends HttpServlet {

	private String file;
	private String username;
	private String photofile;
	private String photoname;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
//		super.doPost(req, resp);
		
	}
	ArrayList<String> strArray = new ArrayList<String> (); 
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		file=new String(req.getParameter("file").getBytes("iso-8859-1"), "utf-8");
		username=new String(req.getParameter("username").getBytes("iso-8859-1"), "utf-8");
		photofile=new String(req.getParameter("photofile").getBytes("iso-8859-1"), "utf-8");
		photoname=new String(req.getParameter("photoname").getBytes("iso-8859-1"), "utf-8");
		System.out.println(username);
		System.out.println(photofile);
		System.out.println(photoname);
		
		if(file!=null){
			byte[] b= Base64Coder.decodeLines(file);
			String filepath=req.getSession().getServletContext().getRealPath("/files");
			File file=new File(filepath);
			if(!file.exists())
				file.mkdirs();
			filepath=filepath+"/"+username;
			System.out.println(filepath);
			file=new File(filepath);
			if(!file.exists())
				file.mkdirs();
			filepath=filepath+"/"+photofile;
			file=new File(filepath);
			if(!file.exists())
				file.mkdirs();
			FileOutputStream fos=new FileOutputStream(file.getPath()+'/'+photoname+".png");
			System.out.println(file.getPath());
			fos.write(b);
			fos.flush();
			fos.close();
		}
	}

	

}
