package com.yang.importz;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class ExcelImport {

	private POIFSFileSystem fs;

	private HSSFWorkbook wb;

	private HSSFSheet sheet;

	private HSSFRow row;

	/**
	 * ��ȡExcel����ͷ������
	 * 
	 * @param InputStream
	 * @return String ��ͷ���ݵ�����
	 */
	public String[] readExcelTitle(InputStream is) {
		try {
			fs = new POIFSFileSystem(is);
			wb = new HSSFWorkbook(fs);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		sheet = wb.getSheetAt(0);
		row = sheet.getRow(0);
		// ����������
		int colNum = row.getPhysicalNumberOfCells();
//		System.out.println("colNum:" + colNum);
		String[] title = new String[colNum];
		for (int i = 0; i < colNum; i++) {
			// title[i] = getStringCellValue(row.getCell((short) i));
			title[i] = getCellFormatValue(row.getCell((int) i));
		}
		return title;
	}

	/**
	 * ��ȡExcel��������
	 * 
	 * @param InputStream
	 * @return Map ������Ԫ���������ݵ�Map����
	 */
    public Map<Integer, String> readExcelContent(InputStream is) {
		Map<Integer, String> content = new HashMap<Integer, String>();
		String str = "";
		try {
			fs = new POIFSFileSystem(is);
			wb = new HSSFWorkbook(fs);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		sheet = wb.getSheetAt(0);
		// �õ�������
		int rowNum = sheet.getLastRowNum();
		row = sheet.getRow(0);
		int colNum = row.getPhysicalNumberOfCells();
		// ��������Ӧ�ôӵڶ��п�ʼ,��һ��Ϊ��ͷ�ı���
		for (int i = 1; i <= rowNum; i++) {
			row = sheet.getRow(i);
			int j = 0;
			while (j < colNum) {
				// ÿ����Ԫ�������������"-"�ָ���Ժ���Ҫʱ��String���replace()������ԭ����
				// Ҳ���Խ�ÿ����Ԫ����������õ�һ��javabean�������У���ʱ��Ҫ�½�һ��javabean
				// str += getStringCellValue(row.getCell((short) j)).trim() +
				// "-";
				str += getCellFormatValue(row.getCell((int) j)).trim() + "@";
				j++;
			}
			content.put(i, str);
			str = "";
		}
		return content;
	}

	/**
	 * ����HSSFCell������������
	 * 
	 * @param cell
	 * @return
	 */
	private String getCellFormatValue(HSSFCell cell) {
		String cellvalue = "";
		if (cell != null) {
			// �жϵ�ǰCell��Type
			switch (cell.getCellType()) {
			// �����ǰCell��TypeΪNUMERIC
				case HSSFCell.CELL_TYPE_NUMERIC:
				case HSSFCell.CELL_TYPE_FORMULA: {
					// �жϵ�ǰ��cell�Ƿ�ΪDate
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						// �����Date������ת��ΪData��ʽ

						// ����1�������ӵ�data��ʽ�Ǵ�ʱ����ģ�2011-10-12 0:00:00
						// cellvalue = cell.getDateCellValue().toLocaleString();

						// ����2�������ӵ�data��ʽ�ǲ�����ʱ����ģ�2011-10-12
						Date date = cell.getDateCellValue();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						cellvalue = sdf.format(date);

					}
					// ����Ǵ�����
					else {
						// ȡ�õ�ǰCell����ֵ
						cellvalue = String.valueOf(cell.getNumericCellValue());
					}
					break;
				}
				// �����ǰCell��TypeΪSTRIN
				case HSSFCell.CELL_TYPE_STRING:
					// ȡ�õ�ǰ��Cell�ַ���
					cellvalue = cell.getRichStringCellValue().getString();
					break;
				// Ĭ�ϵ�Cellֵ
				default:
					cellvalue = " ";
			}
		} else {
			cellvalue = "";
		}
		return cellvalue;

	}

	public static String getUUID() {
		String uuid = UUID.randomUUID().toString();
		return uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23) + uuid.substring(24);
	}

	public static void operGlobal(List<Map<String, Object>> re) {
		// ����������
		String driver = "com.mysql.jdbc.Driver";

		// URLָ��Ҫ���ʵ����ݿ���scutcs
		String url = "jdbc:mysql://127.0.0.1:3306/haloglobal2?useUnicode=true&characterEncoding=utf-8";
		// MySQL����ʱ���û���
		String user = "root";
		// MySQL����ʱ������
		String password = "123456";

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
			ps = conn.prepareStatement("insert into utility_site(pkg,name,url,icon,color,brief,category_pkg,sort,status,is_hot,allow_user_del,country_code,create_time,update_time, api_version) "
			        + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			int sort = 1;
			int index = 1;
			String coutry=null;
			for (int i = 0; i < re.size(); i++) {
				reMap = re.get(i);
				ps.setString(index++, getUUID());
				ps.setString(index++, reMap.get("name").toString());
				ps.setString(index++, reMap.get("url").toString());
				ps.setString(index++, "http://res.holalauncher.com/holalauncher/popular/" + reMap.get("icon").toString());
//				ps.setString(index++, reMap.get("color").toString());
				ps.setString(index++, null);
				ps.setString(index++, null);
				ps.setString(index++, null);
				if(!reMap.get("country_code").toString().equals(coutry)){
					sort = 1;
				}
				ps.setInt(index++, sort++);
				ps.setString(index++, "0");
				ps.setString(index++, "1");
				ps.setString(index++, "1");
				ps.setString(index++, reMap.get("country_code").toString());
				ps.setDate(index++, new java.sql.Date(new Date().getTime()));
				ps.setDate(index++, new java.sql.Date(new Date().getTime()));
				ps.setString(index++, "3");
				ps.addBatch();
				coutry = reMap.get("country_code").toString();
				
				index = 1;
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
	
	public static void operZh(List<Map<String, Object>> re) {
		// ����������
		String driver = "com.mysql.jdbc.Driver";

		// URLָ��Ҫ���ʵ����ݿ���scutcs
		String url = "jdbc:mysql://127.0.0.1:3306/halo?useUnicode=true&characterEncoding=utf-8";
		// MySQL����ʱ���û���
		String user = "root";
		// MySQL����ʱ������
		String password = "123456";

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
			ps = conn.prepareStatement("insert into utility_site(pkg,name,url,icon,color,brief,category_pkg,sort,status,is_hot,allow_user_del,create_time,update_time, api_version) "
			        + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			int index = 1;
			int sort = 1;
			for (int i = 0; i < re.size(); i++) {
				reMap = re.get(i);
				ps.setString(index++, getUUID());
				ps.setString(index++, reMap.get("name").toString());
				ps.setString(index++, reMap.get("url").toString());
				ps.setString(index++, "http://res.holaworld.cn/hola/popular/" + reMap.get("icon").toString());
//				ps.setString(index++, reMap.get("color").toString());
				ps.setString(index++, null);
				ps.setString(index++, null);
				ps.setString(index++, null);
				ps.setInt(index++, sort++);
				ps.setString(index++, "0");
				ps.setString(index++, "1");
				ps.setString(index++, "1");
				ps.setDate(index++, new java.sql.Date(new Date().getTime()));
				ps.setDate(index++, new java.sql.Date(new Date().getTime()));
				ps.setString(index++, "3");
				ps.addBatch();
				
				index = 1;
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

	private static void global(){
		try {
			// �Զ�ȡExcel���������
			InputStream is = new FileInputStream("F:\\global.xls");
			ExcelImport excelReader = new ExcelImport();
			String[] title = excelReader.readExcelTitle(is);
//			System.out.println("���Excel���ı���:");
//			for (String s : title) {
//				System.out.print(s + " ");
//			}

			List<Map<String, Object>> re = new ArrayList<Map<String, Object>>();
			Map<String, Object> map2 = null;
			
			// �Զ�ȡExcel������ݲ���
			InputStream is2 = new FileInputStream("f:\\global.xls");
			Map<Integer, String> map = excelReader.readExcelContent(is2);
//			System.out.println("���Excel��������:");
			String[] arr = null;
			for (int i = 1; i <= map.size(); i++) {
				arr = map.get(i).split("@");
				map2 = new HashMap<String, Object>();
				map2.put("country_code", arr[0]);
				map2.put("name", arr[1].toString());
				map2.put("url", arr[2]);
//				map2.put("color", arr[3]);
				map2.put("icon", arr[3]);
				re.add(map2);
			}
			
			operGlobal(re);

		}
		catch (FileNotFoundException e) {
			System.out.println("δ�ҵ�ָ��·�����ļ�!");
			e.printStackTrace();
		}
	}
	
	private static void zh(){
		try {
			// �Զ�ȡExcel���������
			InputStream is = new FileInputStream("F:\\zh.xls");
			ExcelImport excelReader = new ExcelImport();
			String[] title = excelReader.readExcelTitle(is);
//			System.out.println("���Excel���ı���:");
			for (String s : title) {
				System.out.print(s + " ");
			}

			List<Map<String, Object>> re = new ArrayList<Map<String, Object>>();
			Map<String, Object> map2 = null;
			
			// �Զ�ȡExcel������ݲ���
			InputStream is2 = new FileInputStream("f:\\zh.xls");
			Map<Integer, String> map = excelReader.readExcelContent(is2);
//			System.out.println("���Excel��������:");
			String[] arr = null;
			for (int i = 1; i <= map.size(); i++) {
				arr = map.get(i).split("@");
				map2 = new HashMap<String, Object>();
				map2.put("name", arr[0].toString());
				map2.put("url", arr[1]);
//				map2.put("color", arr[2]);
				map2.put("icon", arr[2]);
				re.add(map2);
			}
			
			operZh(re);

		}
		catch (FileNotFoundException e) {
			System.out.println("δ�ҵ�ָ��·�����ļ�!");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		global();
//		zh();
	}
	
}
