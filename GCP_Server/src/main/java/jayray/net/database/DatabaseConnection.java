package jayray.net.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class DatabaseConnection {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://35.227.169.212:3306/DSHW2?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false&connectTimeout=15000&socketTimeout=15000";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "m2860321";
	static final String dataResourceName = "jdbc/testDB";

	public static Connection getConnection() throws SQLException {
//		DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		
//		try {
//			Class.forName("com.mysql.cj.jdbc.Driver");
//		} catch (ClassNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		PoolProperties p = new PoolProperties();
		p.setUrl("jdbc:mysql://35.227.169.212:3306/DSHW2?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false&connectTimeout=15000&socketTimeout=15000");
		p.setDriverClassName("com.mysql.cj.jdbc.Driver");
		p.setUsername("root");
		p.setPassword("m2860321");
		p.setJmxEnabled(true);
		p.setTestWhileIdle(false);
		p.setTestOnBorrow(true);
		p.setValidationQuery("SELECT 1");
		p.setTestOnReturn(false);
		p.setValidationInterval(10000);
		p.setTimeBetweenEvictionRunsMillis(10000);
		p.setMaxActive(100);
		p.setInitialSize(10);
		p.setMaxWait(10000);
		p.setRemoveAbandonedTimeout(25);
		p.setMinEvictableIdleTimeMillis(10000);
		p.setMinIdle(10);
		p.setLogAbandoned(true);
		p.setRemoveAbandoned(true);
		p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
		  "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
		DataSource datasource = new DataSource();
		datasource.setPoolProperties(p);

		
		Connection con = null;
		try {
			con = datasource.getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}
}
