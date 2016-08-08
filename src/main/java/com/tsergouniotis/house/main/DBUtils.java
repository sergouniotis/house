package com.tsergouniotis.house.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public final class DBUtils {

	private static final String PG_PREFIX = "jdbc:postgresql://";
	private static final String DB_PORT_ENV = "HOUSE_DB_PORT";
	private static final String DB_HOST_ENV = "HOUSE_DB_HOST";

	private static String JDBC_URL = null;
	private static final String DB_USER = "house";
	private static final String DB_PASSWORD = "house";

	private DBUtils() {

	}

	protected static Connection getConnection() throws Exception {
		Class.forName("org.postgresql.Driver");
		return DriverManager.getConnection(JDBC_URL, jdbcProps());
	}

	private static Properties jdbcProps() {
		Properties props = new Properties();
		props.setProperty("user", DB_USER);
		props.setProperty("password", DB_PASSWORD);
		return props;
	}

	public static String url() {
		if (null == JDBC_URL) {
			StringBuilder b = new StringBuilder(PG_PREFIX).append(System.getenv(DB_HOST_ENV)).append(":").append(System.getenv(DB_PORT_ENV)).append("/").append(DB_USER);
			JDBC_URL = b.toString();
		}
		return JDBC_URL;
	}

	public static String user() {
		return DB_USER;
	}

	public static String pass() {
		return DB_PASSWORD;
	}

}
