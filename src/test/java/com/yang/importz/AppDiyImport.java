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
	 * 读取Excel表格表头的内容
	 * 
	 * @param InputStream
	 * @return String 表头内容的数组
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
		// 标题总列数
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
	 * 读取Excel数据内容
	 * 
	 * @param InputStream
	 * @return Map 包含单元格数据内容的Map对象
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
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		row = sheet.getRow(0);
		int colNum = row.getPhysicalNumberOfCells();
		// 正文内容应该从第二行开始,第一行为表头的标题
		for (int i = 1; i <= rowNum; i++) {
			row = sheet.getRow(i);
			int j = 0;
			while (j < colNum) {
				// 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
				// 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
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
	 * 根据HSSFCell类型设置数据
	 * 
	 * @param cell
	 * @return
	 */
	private String getCellFormatValue(HSSFCell cell) {
		String cellvalue = "";
		if (cell != null) {
			// 判断当前Cell的Type
			switch (cell.getCellType()) {
			// 如果当前Cell的Type为NUMERIC
				case HSSFCell.CELL_TYPE_NUMERIC:
				case HSSFCell.CELL_TYPE_FORMULA: {
					// 判断当前的cell是否为Date
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						// 如果是Date类型则，转化为Data格式

						// 方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
						// cellvalue = cell.getDateCellValue().toLocaleString();

						// 方法2：这样子的data格式是不带带时分秒的：2011-10-12
						Date date = cell.getDateCellValue();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						cellvalue = sdf.format(date);

					}
					// 如果是纯数字
					else {
						// 取得当前Cell的数值
						cellvalue = String.valueOf(cell.getNumericCellValue());
					}
					break;
				}
				// 如果当前Cell的Type为STRIN
				case HSSFCell.CELL_TYPE_STRING:
					// 取得当前的Cell字符串
					cellvalue = cell.getRichStringCellValue().getString();
					break;
				// 默认的Cell值
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
		// 驱动程序名
		String driver = "com.mysql.jdbc.Driver";
		// URL指向要访问的数据库名scutcs
		String url = "jdbc:mysql://127.0.0.1:3306/haloglobal2?useUnicode=true&characterEncoding=utf-8";
		// MySQL配置时的用户名
		String user = "root";
		// MySQL配置时的密码
		String password = "123456";

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

	public static void operZh(List<Map<String, Object>> re, List<String> NotExsitInExcelOfIconList) {
		// 驱动程序名
		String driver = "com.mysql.jdbc.Driver";
		// URL指向要访问的数据库名scutcs
		String url = "jdbc:mysql://127.0.0.1:3306/halo?useUnicode=true&characterEncoding=utf-8";
		// MySQL配置时的用户名
		String user = "root";
		// MySQL配置时的密码
		String password = "123456";

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

	private static void global() {
		try {
			// 对读取Excel表格标题测试
			InputStream is = new FileInputStream("F:/excel/global.xls");
			AppDiyImport excelReader = new AppDiyImport();
			String[] title = excelReader.readExcelTitle(is);
			// System.out.println("获得Excel表格的标题:");
			for (String s : title) {
				System.out.print(s + " ");
			}

			List<Map<String, Object>> re = new ArrayList<Map<String, Object>>();
			Map<String, Object> map2 = null;

			// 对读取Excel表格内容测试
			InputStream is2 = new FileInputStream("F:/excel/global.xls");
			Map<Integer, String> map = excelReader.readExcelContent(is2);
			// System.out.println("获得Excel表格的内容:");
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
			System.out.println("未找到指定路径的文件!");
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
				// System.out.println("Excel中找不到对应的文件名为：" + file2.getName());
				err.add(file2.getName());
			}
			// 文件夹中缺少的icon文件列表
			temp.remove(file2.getName());
		}
		System.out.println("Excel中找不到对应的文件名列表：" + err);
		System.out.println("文件夹中缺少或者重复的icon文件列表：" + temp);

		result.addAll(err);
		result.addAll(temp);
		return result;
	}

	private static void zh() {
		try {
			// 对读取Excel表格标题测试
			InputStream is = new FileInputStream("F:/excel/zh.xls");
			AppDiyImport excelReader = new AppDiyImport();
			String[] title = excelReader.readExcelTitle(is);
			// System.out.println("获得Excel表格的标题:");
			for (String s : title) {
				System.out.print(s + " ");
			}

			List<Map<String, Object>> re = new ArrayList<Map<String, Object>>();
			Map<String, Object> map2 = null;

			// 对读取Excel表格内容测试
			InputStream is2 = new FileInputStream("F:/excel/zh.xls");
			Map<Integer, String> map = excelReader.readExcelContent(is2);
			// System.out.println("获得Excel表格的内容:");
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
			System.out.println("未找到指定路径的文件!");
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
