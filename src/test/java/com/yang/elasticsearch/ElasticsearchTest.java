package com.yang.elasticsearch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yang.elasticsearch.model.AppInfo;
import com.yang.elasticsearch.repository.AppInfoRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml", "/applicationContext-elasticsearch.xml" })
public class ElasticsearchTest {

	@Resource
	private AppInfoRepository appInfoRepository;

//	 @Test
	public void saveApp() {
		String documentId = "1";
		AppInfo app1 = new AppInfo();
		app1.setId(documentId);
		app1.setCate("Games Casual");
		app1.setCountry("United States");
		app1.setAppName("Candy Crush Soda Saga");
		app1.setPackageName("com.king.candycrushsodasaga");

		String documentId2 = "2";
		AppInfo app2 = new AppInfo();
		app2.setId(documentId2);
		app2.setCate("Games Racing");
		app2.setCountry("United States");
		app2.setAppName("2XL Racing");
		app2.setPackageName("com.twoxlgames.street");

		appInfoRepository.save(app1);
		appInfoRepository.save(app2);
	}

//	 @Test
	public void batchSaveApp() {
		oper();
	}

//	@Test
	public void queryApp() {
		Sort sort = new Sort(Direction.DESC, "id");
		Pageable page = new PageRequest(0, 120, sort);
		Page<AppInfo> appInfoPage = appInfoRepository.findByAppName("Cradle", page);
		List<AppInfo> app = appInfoPage.getContent();
		for (AppInfo appInfo : app) {
			System.out.println(appInfo.getAppName());
		}
	}

	@Test
	public void queryApp2() {
		SortBuilder sortBuilder = new FieldSortBuilder("id").order(SortOrder.DESC);
		
		QueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder("Cradle s").field("appName");
		QueryBuilder rangeQueryBuilder = new RangeQueryBuilder("minTerminalVersion").lte(10);
		BoolQueryBuilder bool = new BoolQueryBuilder();
		bool.must(queryStringQueryBuilder);
		bool.must(rangeQueryBuilder);
		
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
		 .withQuery(bool)
         .withPageable(new PageRequest(0,10))
         .withSort(sortBuilder)
         .build();
		
		Page<AppInfo> appInfoPage = appInfoRepository.search(searchQuery);
		List<AppInfo> app = appInfoPage.getContent();
		for (AppInfo appInfo : app) {
			System.out.println(appInfo.getAppName()+"-------------"+appInfo.getMinTerminalVersion());
		}
	}

	public void oper() {
		// 驱动程序名
		String driver = "com.mysql.jdbc.Driver";
		// URL指向要访问的数据库名scutcs
		String url = "jdbc:mysql://192.168.1.207:3306/test?useUnicode=true&characterEncoding=utf-8";
		// MySQL配置时的用户名
		String user = "root";

		// MySQL配置时的密码
		String password = "admin";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// 加载驱动程序
			Class.forName(driver);

			// 连续数据库
			conn = DriverManager.getConnection(url, user, password);
			ps = conn.prepareStatement("select id, cate, country, appName, packageName, min_terminal_version  from top_apps");
			rs = ps.executeQuery();
			List<AppInfo> list = new ArrayList<AppInfo>();
			int cnt = 0;
			while (rs.next()) {
				AppInfo app = new AppInfo();
				app.setId(rs.getString("id"));
				app.setCate(rs.getString("cate"));
				app.setCountry(rs.getString("country"));
				app.setAppName(rs.getString("appName"));
				app.setPackageName(rs.getString("packageName"));
				app.setMinTerminalVersion(rs.getInt("min_terminal_version"));
				list.add(app);
				cnt++;
				if (cnt == 1000) {
					appInfoRepository.save(list);
					cnt = 0;
					list.clear();
				}
			}

			appInfoRepository.save(list);

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

}
