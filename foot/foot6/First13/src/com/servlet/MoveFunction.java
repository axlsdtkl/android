package com.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MoveFunction {
	public static void movefile(String origin,String filepath,String filename) throws IOException {
		File originalFile = new File(origin+"\\"+"1.pdf");//ָ��Ҫ��ȡ��ͼƬ
		
		
		if(originalFile.exists()) {
			File result = new File(filepath+"\\"+filename+".pdf");//Ҫд���ͼƬ
            if (result.exists()) {//У����ļ��Ƿ��Ѵ���
                result.delete();//ɾ����Ӧ���ļ����Ӵ�����ɾ��
                result = new File(filepath+"\\"+filename+".pdf");//ֻ�Ǵ�����һ��File���󣬲�û���ڴ����´����ļ�
            }
            if (!result.exists()) {//����ļ�������
                result.createNewFile();//���ڴ����´����ļ�������ʱ��СΪ0K
            }
            FileInputStream inimage = new FileInputStream(originalFile);
            FileOutputStream outimage = new FileOutputStream(result);// ָ��Ҫд���
            int n = 0;// ÿ�ζ�ȡ���ֽڳ���
            byte[] bb = new byte[1024];// �洢ÿ�ζ�ȡ������
            while ((n = inimage.read(bb)) != -1) {
                outimage.write(bb, 0, n);// ����ȡ�����ݣ�д�뵽���������
            }
            //ִ�������Ϻ󣬴����µĸ��ļ�����������С��ʵ�ʴ�С
            inimage.close();
            outimage.close();// �ر����������
		}
		
		originalFile = new File(origin+"\\"+"1.obj");//ָ��Ҫ��ȡ��ͼƬ
		
		
		if(originalFile.exists()) {
			File result = new File(filepath+"\\"+filename+".obj");//Ҫд���ͼƬ
            if (result.exists()) {//У����ļ��Ƿ��Ѵ���
                result.delete();//ɾ����Ӧ���ļ����Ӵ�����ɾ��
                result = new File(filepath+"\\"+filename+".obj");//ֻ�Ǵ�����һ��File���󣬲�û���ڴ����´����ļ�
            }
            if (!result.exists()) {//����ļ�������
                result.createNewFile();//���ڴ����´����ļ�������ʱ��СΪ0K
            }
            FileInputStream inimage = new FileInputStream(originalFile);
            FileOutputStream outimage = new FileOutputStream(result);// ָ��Ҫд���
            int n = 0;// ÿ�ζ�ȡ���ֽڳ���
            byte[] bb = new byte[1024];// �洢ÿ�ζ�ȡ������
            while ((n = inimage.read(bb)) != -1) {
                outimage.write(bb, 0, n);// ����ȡ�����ݣ�д�뵽���������
            }
            //ִ�������Ϻ󣬴����µĸ��ļ�����������С��ʵ�ʴ�С
            inimage.close();
            outimage.close();// �ر����������
		}
	}
}
