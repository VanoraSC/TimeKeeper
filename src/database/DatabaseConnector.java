package database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {

	private static Connection dbcon;
	private static String USERNAME;
	private static String PASSWORD;
	private static String DATABASE;

	private final static boolean prodDB = true;
	// private final static boolean prodDB = false;
	private static Properties props;
	private static String url;

	public static boolean isProduction() {
		return prodDB;
	}

	static {

		try {
			getPropValues();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(-1);
		}
		props = new Properties();

		props.setProperty("user", USERNAME);
		props.setProperty("password", PASSWORD);

		if (prodDB)
			url = DATABASE;
		else
			url = DATABASE + "_DEV";
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

	public static void main(String[] args) throws SQLException, IOException {

		System.out.println(dbcon);
		String statement = "select id, name from time_card.tasks";
		PreparedStatement ps = dbcon.prepareStatement(statement);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			System.out.println(rs.getInt("id"));
			System.out.println(rs.getString("name"));
		}
	}

	private static String getPropValues() throws IOException {

		InputStream inputStream = null;
		String result = null;
		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";

			inputStream = DatabaseConnector.class.getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

			// get the property value and print it out
			USERNAME = prop.getProperty("username");
			DATABASE = prop.getProperty("database");
			PASSWORD = prop.getProperty("password");

		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		return result;
	}
}
