

import java.sql.*;

public class MysqlConn {
	private Connection conn;
	private String url;
	
	public MysqlConn(){
		url = "jdbc:mysql://localhost:3306/test_ljj?"
	                + "user=root&password=123456&useUnicode=true&characterEncoding=UTF8";
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Connection getConn() {
		try {
			if(conn == null || conn.isClosed()){
				conn = DriverManager.getConnection(url);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
}
