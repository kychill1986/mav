package com.yang.gzip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.BaseEncoding;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class GZIPStringUtils {
	
	@Resource
	private RedisTemplate<String, String> jedisTemplate;
	
    private static ObjectMapper objectMapper=new ObjectMapper();

    public static ObjectMapper getObjectMapperInstance(){
        return objectMapper;
    }
    
	@Test
	public void testGetListFromRedisWithSort() {
		String a = "asdasd asd s asdf adsf asdf as fa dasfas dfasdf asdf asdf asdf asdf adsf asdf ads fasf asdf asdf asdf asdf asdf";
		System.out.println("原大小---》》" + a.length());
		String b = compress2String(a);

		System.out.println("压缩后---》》" + b.length());
		System.out.println(uncompressFromString(b));
	}

    /**
     * 将对象转化为json,并压缩字符串
     * @param originalString  原始字符串
     * @return  压缩后的字符串
     */
    public static String compress2String(Object object) {
        String originalString=null;
        try {
            originalString = getObjectMapperInstance().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        String newStr = encodeByte2String(compressToByte(originalString));
        return newStr;
    }
    /**
     * 解压缩字符串
     * @param compressedString 压缩后的字符串
     * @return  原始字符串
     */
    public static String uncompressFromString(String compressedString) {
        String originalString = uncompressToString(GZIPStringUtils.decodeString2Byte(compressedString));

        return originalString;

    }
    /**
     * 解压缩字符串，返回 对象
     * @param compressedString 压缩后的字符串
     * @param cls  class信息
     * @return  对象
     */
    public static Object uncompress2Object(String compressedString,Class<?> cls) {
        String originalString = uncompressToString(GZIPStringUtils.decodeString2Byte(compressedString));
        Object object=null;
        try {
            object=getObjectMapperInstance().readValue(originalString, cls);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return object;

    }

    /**
     * decode
     * 
     * @param data
     *            字符串串
     * @return base64编码字符串
     * @throws UnsupportedEncodingException
     */
    private static String encodeByte2String(byte[] data) {
        String base64String = BaseEncoding.base64().encode(data);
        return base64String;

    }

    /**
     * decode
     * 
     * @param base64String
     *            将编码后的字符串的反编码为原始字符串
     * @return 原始字符串信息
     * @throws UnsupportedEncodingException
     */
    private static byte[] decodeString2Byte(String base64String) {
        byte[] contentInBytes = BaseEncoding.base64().decode(base64String);
        return contentInBytes;
    }

    private static String encode = "utf-8";// "ISO-8859-1"

    public String getEncode() {
        return encode;
    }

    /*
     * 设置 编码，默认编码：UTF-8
     */
    public void setEncode(String encode) {
        this.encode = encode;
    }

    /*
     * 字符串压缩为字节数组
     */
    private static byte[] compressToByte(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(encode));
            gzip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    /*
     * 字符串压缩为字节数组
     */
    private static byte[] compressToByte(String str, String encoding) {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(encoding));
            gzip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    /*
     * 字节数组解压缩后返回字符串
     */
    private static String uncompressToString(byte[] b) {
        if (b == null || b.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(b);

        try {
            GZIPInputStream gunzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toString();
    }

    /*
     * 字节数组解压缩后返回字符串
     */
    private static String uncompressToString(byte[] b, String encoding) {
        if (b == null || b.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(b);

        try {
            GZIPInputStream gunzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toString(encoding);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}