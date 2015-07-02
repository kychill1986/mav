package com.yang.hive;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HiveTest {

	public static void main(String[] args) throws ClassNotFoundException {
		dropTable();
		
		createTable();
		
		loadDate();
		
		search();
	}
	
	private static void search() {
		try {
			Connection con = getConnection();
			PreparedStatement sta = con.prepareStatement("select * from cite2 limit 10");
			ResultSet result = sta.executeQuery();
			while (result.next()) {
				System.out.println(result.getString(2));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
        catch (ClassNotFoundException e) {
	        e.printStackTrace();
        }
	}
	
	private static void loadDate() {
		try {
			Connection con = getConnection();
			//'/home/hadoop/cite75_99.txt' 必须是hive服务器的路径，也就是cite75_99.txt文件存放在hive服务器上，不是本地的文件
			PreparedStatement sta = con.prepareStatement("LOAD DATA LOCAL INPATH '/home/hadoop/cite75_99.txt' overwrite into table cite2");
			sta.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
        catch (ClassNotFoundException e) {
	        e.printStackTrace();
        }
	}
	
	private static void dropTable() {
		try {
			Connection con = getConnection();
			PreparedStatement sta = con.prepareStatement("drop table cite2");
			sta.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
        catch (ClassNotFoundException e) {
	        e.printStackTrace();
        }
	}
	
	private static void createTable() {
		try {
			Connection con = getConnection();
			PreparedStatement sta = con.prepareStatement("create table cite2 (citing int, cited int) row format delimited fields terminated by ',' stored as textfile ");
			sta.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
        catch (ClassNotFoundException e) {
	        e.printStackTrace();
        }
	}

	private static Connection getConnection() throws ClassNotFoundException {
		Class.forName("org.apache.hive.jdbc.HiveDriver");
		Connection con = null;
		try {
			// Connection cnct = DriverManager.getConnection("jdbc:hive2://<host>:<port>", "<user>", "<password>");
			//The default <port> is 10000. In non-secure configurations, specify a <user> for the query to run as. The <password> field value is ignored in non-secure mode.
			con = DriverManager.getConnection("jdbc:hive2://192.168.1.208:10000", "hadoop", "");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
}
