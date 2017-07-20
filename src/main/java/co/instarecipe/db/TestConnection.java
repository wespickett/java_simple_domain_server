package co.instarecipe.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
	
	public static void main(String[] args) {
		try {
			Class.forName("org.postgresql.Driver");
			Connection c = DriverManager.getConnection(
					DBContract.HOST +  DBContract.DB_NAME,
					DBContract.USERNAME,
					DBContract.PASSWORD
			);

			System.out.println("DB Connected");

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
