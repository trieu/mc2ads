package com.mc2ads.tests;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.jdbc.OracleTypes;

import com.mc2ads.db.SqlDbConfigs;

public class OracleDbConnector {

	

	public static void testConnnection(String[] args) throws SQLException {
		final String DBURL = "jdbc:oracle:thin:@10.254.53.220:1521:ORADEV1";
		final String DBUSER = "LA";
		final String DBPASS = "123456";

		// Load Oracle JDBC Driver
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

		// Connect to Oracle Database
		Connection con = DriverManager.getConnection(DBURL, DBUSER, DBPASS);

		Statement statement = con.createStatement();

		// Execute a SELECT query on Oracle Dummy DUAL Table. Useful for
		// retrieving system values
		// Enables us to retrieve values as if querying from a table
		ResultSet rs = statement.executeQuery("SELECT SYSDATE FROM DUAL");

		if (rs.next()) {
			Date currentDate = rs.getDate(1); // get first column returned
			System.out.println("Current Date from Oracle is : " + currentDate);
		}
		rs.close();
		statement.close();
		con.close();
	}

	public static String getRelatedVideoIds(int dbid, long id) throws SQLException {
		Connection dbConnection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String query = "{call GET_RESULT_ASSOCIATION(?,?,?)}";
		//String query = "begin ? := LA.GET_RESULT_ASSOCIATION(?,?); end;";
		StringBuilder sb = new StringBuilder();
		try {
			// Load Oracle JDBC Driver
			//DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			
			
			SqlDbConfigs dbConfigs = SqlDbConfigs.load("db_oracle_video");

			// Connect to Oracle Database
			dbConnection = dbConfigs.getConnection();
			cs = dbConnection.prepareCall(query);
			
			cs.setInt(1, dbid);
			cs.setLong(2, id);
			cs.registerOutParameter(3, OracleTypes.CURSOR);

			// execute store procedure
			cs.execute();
			rs = (ResultSet)cs.getObject(3);
			
			while (rs.next()) {
				// processing result set, never get here
				System.out.println(rs.getString(1));
				sb.append(rs.getString(1)).append(",");
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			if (cs != null) {
				cs.close();
			}
			if (dbConnection != null) {
				dbConnection.close();
			}
		}
		String ids = sb.toString();
		ids = ids.substring(0, ids.lastIndexOf(","));
		return ids;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(getRelatedVideoIds(2,1002725));
	}
}
