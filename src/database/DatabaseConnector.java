package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {

	private static Connection dbcon;

//	private final static boolean prodDB = true;
	 private final static boolean prodDB = false;
	private static Properties props;
	private static String url;

	public static boolean isProduction() {
		return prodDB;
	}

	static {

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(-1);
		}
		props = new Properties();

		props.setProperty("user", "pete");
		props.setProperty("password", "asshole17");

		if (prodDB)
			url = "jdbc:postgresql://192.168.1.112/WorkTracking";
		else
			url = "jdbc:postgresql://192.168.1.112/WorkTracking_DEV";
		try {
			dbcon = DriverManager.getConnection(url, props);
		} catch (SQLException e) {
			dbcon = null;
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public static Connection getConnection() {
		return dbcon;
	}

	public static void main(String[] args) throws SQLException {
		System.out.println(dbcon);
		String statement = "select id, name from time_card.tasks";
		PreparedStatement ps = dbcon.prepareStatement(statement);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			System.out.println(rs.getInt("id"));
			System.out.println(rs.getString("name"));
		}
	}

}
