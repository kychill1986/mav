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

public class ImportLanguage {

	public static void oper(List<Map<String, Object>> re) {
		// 驱动程序名
		String driver = "com.mysql.jdbc.Driver";

		// URL指向要访问的数据库名scutcs
		String url = "jdbc:mysql://127.0.0.1:3306/haloglobal2?useUnicode=true&characterEncoding=utf-8";
		// MySQL配置时的用户名
		String user = "root";
		// MySQL配置时的密码
		String password = "123456";

		/*
		 * // URL指向要访问的数据库名scutcs String url =
		 * "jdbc:mysql://haloglobal.czrpdpldoj2d.us-west-2.rds.amazonaws.com:3306/haloglobal?useUnicode=true&characterEncoding=utf-8"
		 * ; // MySQL配置时的用户名 String user = "qiigame"; // MySQL配置时的密码 String
		 * password = "gl7R1g3oEHJ5MFdL";
		 */

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// 加载驱动程序
			Class.forName(driver);

			// 连续数据库
			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);

			Map<String, Object> reMap = null;
			ps = conn.prepareStatement("insert into feedback_language(name, code) values(?,?)");
			for (int i = 0; i < re.size(); i++) {
				reMap = re.get(i);
				ps.setString(1, reMap.get("name").toString());
				ps.setString(2, reMap.get("code").toString());
				ps.addBatch();

				if (i % 300 == 0) {// 可以设置不同的大小；如50，100，500，1000等等
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
		Map<String, String> map = readProperties(ImportLanguage.class.getClassLoader().getResource("feedback_language.properties").getPath());

		List<Map<String, Object>> re = new ArrayList<Map<String, Object>>();
		Map<String, Object> map2 = null;
		for (Map.Entry<String, String> entry : map.entrySet()) {
			map2 = new HashMap<String, Object>();
			map2.put("code", entry.getKey());
			map2.put("name", entry.getValue());
			re.add(map2);
		}

		oper(re);

	}
}
