package waitdb;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class WaitDB {

	public static boolean getConnection(String jdbcUrl, String user, String pass) {
	    Connection conn = null;
	    try {
		    Properties connectionProps = new Properties();
		    connectionProps.put("user", user);
		    connectionProps.put("password", pass);
		    conn = DriverManager.getConnection(jdbcUrl,connectionProps);
		    return (conn!=null);
	    } catch (SQLException sqle) {
	    	System.out.println("Not ready: "+sqle.getMessage());
	    	return false;
	    } finally {
	    	try {
	    		if (conn!=null) conn.close();
	    	} catch (SQLException sqlc) {
	    		return false;
	    	}
	    }
	}
	
	public static void main(String[] args) {
		int loops = 1;
		int timeout = 10;
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("/usr/local/tomcat-base/sakai/local.properties"));
			String driverName = prop.getProperty("driverClassName@javax.sql.BaseDataSource");
			Class.forName(driverName);
			String dbUrl = prop.getProperty("url@javax.sql.BaseDataSource");
			String dbUser = prop.getProperty("username@javax.sql.BaseDataSource");
			String dbPass = prop.getProperty("password@javax.sql.BaseDataSource");
			do {
				Thread.sleep(timeout*1000);
				System.out.println("Testing "+dbUrl+" alive... after waiting "+(timeout*loops++)+" seconds.");
			} while (!getConnection(dbUrl,dbUser,dbPass) && loops < 180); // Max wait for 30 mins
		} catch (Exception exx) {
			System.err.println("Error loading db properties: "+exx.getMessage());
		}
	}

}
