package co.instarecipe.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresHelper {

	private static Connection conn;
	
	public static boolean connect(String host, String dbName, String user, String pass) throws SQLException, ClassNotFoundException {
		if (host.isEmpty() || dbName.isEmpty() || user.isEmpty()) {
			throw new SQLException("Database credentials missing");
		}

		Class.forName("org.postgresql.Driver");
		PostgresHelper.conn = DriverManager.getConnection(
				host +  dbName,
				user,
				pass
		);

		return true;
	}
	
	public static Connection getConnection() {
		return PostgresHelper.conn;
	}
}
