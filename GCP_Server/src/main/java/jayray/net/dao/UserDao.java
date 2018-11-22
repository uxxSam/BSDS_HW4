package jayray.net.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jayray.net.database.DatabaseConnection;
import jayray.net.model.User;

public class UserDao {
	public static String getCurrentDay(int userID) throws SQLException{
		int res = -1;
		
		Connection conn = DatabaseConnection.getConnection();
		PreparedStatement stmt = null;
		try {
			String sql = "SELECT SUM(stepCount) AS REC_CNT, dayNumber FROM Record WHERE userID = " + userID + " GROUP By dayNumber ORDER By dayNumber DESC LIMIT 1;";
			stmt = conn.prepareStatement(sql);					
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				res = rs.getInt("REC_CNT");
			}
			
			rs.close();
			stmt.close();
			conn.close();			
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
			
			return String.valueOf(res);
		} // end try
	}
	
	public static String getSingleDay(int userID, int day) throws SQLException{
		int res = -1;
		
		Connection conn = DatabaseConnection.getConnection();
		PreparedStatement stmt = null;
		try {
			String sql = "SELECT SUM(stepCount) AS REC_CNT, dayNumber FROM Record WHERE userID = " + userID + " and dayNumber = " + day + " GROUP By dayNumber ORDER By dayNumber DESC;";
			stmt = conn.prepareStatement(sql);					
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				res = rs.getInt("REC_CNT");
			}
			
			rs.close();
			stmt.close();
			conn.close();			
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
			
			return String.valueOf(res);
		} // end try
	}
	
	public static String getRangeDay(int userID, int startDay, int numDays) throws SQLException{
		StringBuilder res = new StringBuilder();
		
		Connection conn = DatabaseConnection.getConnection();
		PreparedStatement stmt = null;
		try {
			String sql = "SELECT SUM(stepCount) AS REC_CNT, dayNumber FROM Record WHERE userID = " + userID + " and dayNumber >= " + startDay + " GROUP By dayNumber ORDER By dayNumber ASC LIMIT " + numDays + " ;";
			stmt = conn.prepareStatement(sql);					
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				res.append(rs.getInt("REC_CNT")).append(',');
			}
			
			rs.close();
			stmt.close();
			conn.close();			
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
			
			return res.toString();
		} // end try
	}
	// (userID, day, timeInterval, stepCount);
	public static String postStepCount(int userID, int day, int timeInterval, int stepCount) throws SQLException{
		int rs = -1;
		
		Connection conn = DatabaseConnection.getConnection();
		PreparedStatement stmt = null;
		try {
			String sql = "INSERT IGNORE INTO Record(userID, dayNumber, timeInterval, stepCount) VALUES(" + userID + "," + day + "," + timeInterval + "," + stepCount + ");";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeUpdate();

			stmt.close();
			conn.close();			
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
			
			return String.valueOf(rs);
		} // end try
	}
	
	public static String dropSchema() throws SQLException{
		int rs = -1;
		
		Connection conn = DatabaseConnection.getConnection();
		Statement stmt = null;
		try {
			String sql1 = "DROP TABLE IF EXISTS Record;";
			stmt = conn.createStatement();
			stmt.executeUpdate(sql1);
			
			String sql = "CREATE TABLE Record (recordId INT AUTO_INCREMENT, userID INT, dayNumber INT DEFAULT 1, timeInterval INT, stepCount INT, CONSTRAINT pk_Record_RecordID PRIMARY KEY (recordId), CONSTRAINT uk_one UNIQUE(userID, dayNumber, timeInterval));";
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);

			stmt.close();
			conn.close();			
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
			
			return String.valueOf(rs);
		} // end try
	}
}
