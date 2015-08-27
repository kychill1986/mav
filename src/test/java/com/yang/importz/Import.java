package com.yang.importz;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Import {

	public static void oper(List<Map<String, Object>> re) {
		// ����������
		String driver = "com.mysql.jdbc.Driver";
		
		
		// URLָ��Ҫ���ʵ����ݿ���scutcs
		String url = "jdbc:mysql://103.6.223.146:3306/halo?useUnicode=true&characterEncoding=utf-8";
		// MySQL����ʱ���û���
		String user = "root";
		// MySQL����ʱ������
		String password = "root+_)(*";
		
		
		/*
		// URLָ��Ҫ���ʵ����ݿ���scutcs
		String url = "jdbc:mysql://haloglobal.czrpdpldoj2d.us-west-2.rds.amazonaws.com:3306/haloglobal?useUnicode=true&characterEncoding=utf-8";
		// MySQL����ʱ���û���
		String user = "qiigame";
		// MySQL����ʱ������
		String password = "gl7R1g3oEHJ5MFdL";
		*/
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// ������������
			Class.forName(driver);

			// �������ݿ�
			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);

			Map<String, Object> reMap = null;
			ps = conn.prepareStatement("insert into wallpaper_tag_relation(wallpaper_id, wallpaper_tag_code) values(?,?)");
			for (int i = 0; i < re.size(); i++) {
				reMap = re.get(i);
				ps.setInt(1, Integer.valueOf(reMap.get("wallpaper_id").toString()));
				ps.setString(2, reMap.get("wallpaper_tag_code").toString());
				ps.addBatch();

				if (i % 300 == 0) {// �������ò�ͬ�Ĵ�С����50��100��500��1000�ȵ�
					ps.executeBatch();
					conn.commit();
					ps.clearBatch();
				}
			}
			ps.executeBatch();
			conn.commit();
			
			System.out.println("Finish");

		}
		catch (ClassNotFoundException e) {
			System.out.println("Sorry,can`t find the Driver!");
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static Map<String, String> readProperties(String filePath) {
		Map<String, String> nationalFlagMap = new HashMap<String, String>();
		Properties props = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(filePath));
			props.load(in);
			Enumeration en = props.propertyNames();
			String key = "";
			while (en.hasMoreElements()) {
				key = (String) en.nextElement();
				nationalFlagMap.put(key, props.getProperty(key));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return nationalFlagMap;
	}

	public static void main(String[] args) {
		Map<String, String> map = readProperties(Import.class.getClassLoader().getResource("halo.properties").getPath());
		
		List<Map<String, Object>> re = new ArrayList<Map<String, Object>>();
		Map<String, Object> map2 = null;
		String[] idArr = null;
		
		for (Map.Entry<String, String> entry : map.entrySet()) {
			idArr = entry.getValue().split(",");
			for (String id : idArr) {
				map2 = new HashMap<String, Object>();
				map2.put("wallpaper_id", id);
				map2.put("wallpaper_tag_code", entry.getKey());
				re.add(map2);
            }
		}
		
		oper(re);
		
	}
}
