package com.servlet;
/*
 * 本类主要用来复制根目录下的pdf和obj当相应的图片文件夹中，这样前端可以直接进行下载（不过只是样例，并不是真正的）
 * 我们将来要做的就是将这里的pdf和obj换成正确的，根据文件夹中这些图片进行三维重建后生成的最终pdf和obj，将目录下的obj与pdf替换成匹配的
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MoveFunction {
	public static void movefile(String origin,String filepath,String filename) throws IOException {
		File originalFile = new File(origin);//指定要读取的图片
		
		
		System.out.println("dizhi "+origin);
		if(originalFile.exists()) {
			File result = new File(filepath+"/"+filename);//要写入的图片
            if (result.exists()) {//校验该文件是否已存在
                result.delete();//删除对应的文件，从磁盘中删除
                result = new File(filepath+"/"+filename);//只是创建了一个File对象，并没有在磁盘下创建文件
            }
            if (!result.exists()) {//如果文件不存在
                result.createNewFile();//会在磁盘下创建文件，但此时大小为0K
            }
            System.out.println("mubiao "+result);
            FileInputStream inimage = new FileInputStream(originalFile);
            FileOutputStream outimage = new FileOutputStream(result);// 指定要写入的
            int n = 0;// 每次读取的字节长度
            byte[] bb = new byte[1024];// 存储每次读取的内容
            while ((n = inimage.read(bb)) != -1) {
                outimage.write(bb, 0, n);// 将读取的内容，写入到输出流当中
            }
            //执行完以上后，磁盘下的该文件才完整，大小是实际大小
            inimage.close();
            outimage.close();// 关闭输入输出流
		}
	}
}
