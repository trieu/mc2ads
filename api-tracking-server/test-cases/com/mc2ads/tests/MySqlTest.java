package com.mc2ads.tests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.mc2ads.db.SqlDbConfigs;
import com.mc2ads.utils.DateTimeUtil;

public class MySqlTest {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	public void readDataBase() throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			SqlDbConfigs dbConfigs = SqlDbConfigs.load("db_mysql_video_nhacso");
			connect = dbConfigs.getConnection();

			// Statements allow to issue SQL queries to the database
			//String sql = "CALL sp_plugin_getArticlesByStr(?)";
			
			String sql = "CALL sp_plugin_getVideoByStr(?)";
			preparedStatement = connect.prepareStatement(sql );
			//preparedStatement.setString(1, "1921666,1921664,1921662");
			preparedStatement.setString(1, "1000,2013");
			// Result set get the result of the SQL query
			resultSet = preparedStatement.executeQuery();
			
			writeResultSetNS(resultSet);


		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}

	
//	SP : sp_plugin_getArticlesByStr(p_articleid TEXT)
//	Output: article_id, title, thumbnail_url, pageview, creation_time

	void writeResultSet(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		while (resultSet.next()) {
			
			long article_id = resultSet.getLong("article_id");
			String title = resultSet.getString("title");
			String thumbnail_url = resultSet.getString("thumbnail_url");
			Date date = new Date(resultSet.getLong("creation_time")*1000);
			int pageview = resultSet.getInt("pageview");
			System.out.println("article_id: " + article_id);
			System.out.println("title: " + title);
			System.out.println("thumbnail_url: " + thumbnail_url);
			System.out.println("Date: " + DateTimeUtil.formatDate(date));
			System.out.println("pageview: " + pageview);
			System.out.println("-------------------------------");
		}
	}
	
	void writeResultSetNS(ResultSet rs) throws SQLException {
		// ResultSet is initially before the first data set
		while (rs.next()) {
			
			long article_id = rs.getLong("VideoID");
			String title = rs.getString("VideoName");
			String thumbnail_url = rs.getString("URLThumb")+ "/" +  rs.getString("Thumbnail");
			Date date = new Date(rs.getLong("CreateDate")*1000);
			int pageview = rs.getInt("pageview");
			System.out.println("article_id: " + article_id);
			System.out.println("title: " + title);
			System.out.println("thumbnail_url: " + thumbnail_url);
			System.out.println("Date: " + DateTimeUtil.formatDate(date));
			System.out.println("pageview: " + pageview);
			System.out.println("-------------------------------");
		}
	}

	// You need to close the resultSet
	private void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}

	public static void main(String[] args) {
		MySqlTest mySqlTest = new MySqlTest();
		try {
			mySqlTest.readDataBase();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
