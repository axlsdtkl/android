package com.servlet;
/*
 * ������Ҫ�������Ƹ�Ŀ¼�µ�pdf��obj����Ӧ��ͼƬ�ļ����У�����ǰ�˿���ֱ�ӽ������أ�����ֻ�������������������ģ�
 * ���ǽ���Ҫ���ľ��ǽ������pdf��obj������ȷ�ģ������ļ�������ЩͼƬ������ά�ؽ������ɵ�����pdf��obj����Ŀ¼�µ�obj��pdf�滻��ƥ���
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MoveFunction {
	public static void movefile(String origin,String filepath,String filename) throws IOException {
		File originalFile = new File(origin);//ָ��Ҫ��ȡ��ͼƬ
		
		
		System.out.println("dizhi "+origin);
		if(originalFile.exists()) {
			File result = new File(filepath+"/"+filename);//Ҫд���ͼƬ
            if (result.exists()) {//У����ļ��Ƿ��Ѵ���
                result.delete();//ɾ����Ӧ���ļ����Ӵ�����ɾ��
                result = new File(filepath+"/"+filename);//ֻ�Ǵ�����һ��File���󣬲�û���ڴ����´����ļ�
            }
            if (!result.exists()) {//����ļ�������
                result.createNewFile();//���ڴ����´����ļ�������ʱ��СΪ0K
            }
            System.out.println("mubiao "+result);
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
