package com.yang.image;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import org.apache.commons.codec.digest.DigestUtils;


public class A {
    //读取远程url图片,得到宽高
    public int[] returnImgWH(String imgurl) {
        boolean b=false;
        try {
            //实例化url
            URL url = new URL(imgurl);
            //载入图片到输入流
            java.io.BufferedInputStream bis = new BufferedInputStream(url.openStream());
            //实例化存储字节数组
            byte[] bytes = new byte[100];
            //设置写入路径以及图片名称
            OutputStream bos = new FileOutputStream(new File("/Users/Chill/Documents/Holaverse/a/a.jpg"));
            int len;
            while ((len = bis.read(bytes)) > 0) {
                bos.write(bytes, 0, len);
            }
            bis.close();
            bos.flush();
            bos.close();
            //关闭输出流
            b=true;
        } catch (Exception e) {
            //如果图片未找到
            b=false;
        }
        int[] a = new int[2];
        if(b){//图片存在
            //得到文件
            java.io.File file = new java.io.File("/Users/Chill/Documents/Holaverse/a/a.jpg");
            BufferedImage bi = null;
            boolean imgwrong=false;
            try {
                //读取图片
                bi = javax.imageio.ImageIO.read(file);
                try{
                    //判断文件图片是否能正常显示,有些图片编码不正确
                    int i = bi.getType();
                    imgwrong=true;
                }catch(Exception e){
                    imgwrong=false;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if(imgwrong){
                a[0] = bi.getWidth(); //获得 宽度
                a[1] = bi.getHeight(); //获得 高度
            }else{
                a=null;
            }
            //删除文件
            file.delete();
        }else{//图片不存在
            a=null;
        }
       return a;

    }

    public static void main(String[] args) {
//    	A i = new A();
//          int[] a=i.returnImgWH("http://img1.test.holaworld.cn/0341de6577e6abe4259c5de89caf0cc4");
//          if(a==null){
//              System.out.println("图片未找到!");
//          }else{
//              System.out.println("宽为" + a[0]);
//              System.out.println("高为" + a[1]);
//          }
    	
    	String s = "asdas123123123123d";
    	System.out.println(DigestUtils.md5Hex(s).length());
    }
}

