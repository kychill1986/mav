package com.yang.importz;

import java.io.File;
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

public class AppDiyImport {

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
		// System.out.println("colNum:" + colNum);
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

	public static void operGlobal(List<Map<String, Object>> re, List<String> NotExsitInExcelOfIconList) {
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
			ps = conn.prepareStatement("insert into app_diy_icon(app_name, pkg, app_icon, enabled, create_time, update_time) " + "values(?,?,?,?,?,?)");

			int index = 1;
			for (int i = 0; i < re.size(); i++) {
				reMap = re.get(i);
				if (NotExsitInExcelOfIconList.contains(reMap.get("icon").toString())) {
					continue;
				}
				ps.setString(index++, reMap.get("name").toString());
				ps.setString(index++, reMap.get("pkg").toString());
				ps.setString(index++, "http://res.holalauncher.com/holalauncher/app/diy/" + reMap.get("icon").toString());
				ps.setString(index++, "1");
				ps.setDate(index++, new java.sql.Date(new Date().getTime()));
				ps.setDate(index++, new java.sql.Date(new Date().getTime()));
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

	public static void operZh(List<Map<String, Object>> re, List<String> NotExsitInExcelOfIconList) {
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
			ps = conn.prepareStatement("insert into app_diy_icon(app_name, pkg, app_icon, enabled, create_time, update_time) " + "values(?,?,?,?,?,?)");

			int index = 1;
			for (int i = 0; i < re.size(); i++) {
				reMap = re.get(i);
				if (NotExsitInExcelOfIconList.contains(reMap.get("icon").toString())) {
					continue;
				}
				ps.setString(index++, reMap.get("name").toString());
				ps.setString(index++, reMap.get("pkg").toString());
				ps.setString(index++, "http://res.holaworld.cn/hola/app/diy/" + reMap.get("icon").toString());
				ps.setString(index++, "1");
				ps.setDate(index++, new java.sql.Date(new Date().getTime()));
				ps.setDate(index++, new java.sql.Date(new Date().getTime()));
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

	private static void global() {
		try {
			// �Զ�ȡExcel���������
			InputStream is = new FileInputStream("F:/excel/global.xls");
			AppDiyImport excelReader = new AppDiyImport();
			String[] title = excelReader.readExcelTitle(is);
			// System.out.println("���Excel���ı���:");
			for (String s : title) {
				System.out.print(s + " ");
			}

			List<Map<String, Object>> re = new ArrayList<Map<String, Object>>();
			Map<String, Object> map2 = null;

			// �Զ�ȡExcel������ݲ���
			InputStream is2 = new FileInputStream("F:/excel/global.xls");
			Map<Integer, String> map = excelReader.readExcelContent(is2);
			// System.out.println("���Excel��������:");
			String[] arr = null;
			List<String> iconList = new ArrayList<String>();
			for (int i = 1; i <= map.size(); i++) {
				arr = map.get(i).split("@");
				map2 = new HashMap<String, Object>();
				map2.put("name", arr[0]);
				map2.put("pkg", arr[1]);
				map2.put("icon", arr[2]);
				iconList.add(arr[2].trim());
				re.add(map2);
			}

			operGlobal(re, compareFileName(iconList, "F:/excel/global"));

		}
		catch (FileNotFoundException e) {
			System.out.println("δ�ҵ�ָ��·�����ļ�!");
			e.printStackTrace();
		}
	}

	private static List<String> compareFileName(List<String> iconList, String path) {
		File file = new File(path);
		File[] fileList = file.listFiles();
		List<String> err = new ArrayList<String>();
		List<String> temp = iconList;
		List<String> result = new ArrayList<String>();
		for (File file2 : fileList) {
			if (!iconList.contains(file2.getName())) {
				// System.out.println("Excel���Ҳ�����Ӧ���ļ���Ϊ��" + file2.getName());
				err.add(file2.getName());
			}
			// �ļ�����ȱ�ٵ�icon�ļ��б�
			temp.remove(file2.getName());
		}
		System.out.println("Excel���Ҳ�����Ӧ���ļ����б�" + err);
		System.out.println("�ļ�����ȱ�ٻ����ظ���icon�ļ��б�" + temp);

		result.addAll(err);
		result.addAll(temp);
		return result;
	}

	private static void zh() {
		try {
			// �Զ�ȡExcel���������
			InputStream is = new FileInputStream("F:/excel/zh.xls");
			AppDiyImport excelReader = new AppDiyImport();
			String[] title = excelReader.readExcelTitle(is);
			// System.out.println("���Excel���ı���:");
			for (String s : title) {
				System.out.print(s + " ");
			}

			List<Map<String, Object>> re = new ArrayList<Map<String, Object>>();
			Map<String, Object> map2 = null;

			// �Զ�ȡExcel������ݲ���
			InputStream is2 = new FileInputStream("F:/excel/zh.xls");
			Map<Integer, String> map = excelReader.readExcelContent(is2);
			// System.out.println("���Excel��������:");
			String[] arr = null;
			List<String> iconList = new ArrayList<String>();
			for (int i = 1; i <= map.size(); i++) {
				arr = map.get(i).split("@");
				map2 = new HashMap<String, Object>();
				map2.put("name", arr[0]);
				map2.put("pkg", arr[1]);
				map2.put("icon", arr[2]);
				iconList.add(arr[2]);
				re.add(map2);
			}

			operZh(re, compareFileName(iconList, "F:/excel/zh"));

		}
		catch (FileNotFoundException e) {
			System.out.println("δ�ҵ�ָ��·�����ļ�!");
			e.printStackTrace();
		}
	}

	// select * from (select pkg, count(1) c from app_diy_icon group by pkg) t
	// where t.c > 1
	public static void main(String[] args) {
//		global();
		 zh();
	}

}
