package com.oneguysolutions.generality.connection;

public class MySQLDefaultConnectionValues extends DefacultConnectionValues{
	public static final String TITLE = "MySQL";
	public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
	public static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/database";
	public static final String DEFAULT_USER = "root";
	public static final String DEFAULT_PASSWORD = "";
	
	@Override
	public String getTitle() {
		return TITLE;
	}
	@Override
	public String getDriverClass() {
		return DRIVER_CLASS;
	}
	@Override
	public String getDefaultUrl() {
		return DEFAULT_URL;
	}
	@Override
	public String getDefaultUser() {
		return DEFAULT_USER;
	}
	@Override
	public String getDefaultPassword() {
		return DEFAULT_PASSWORD;
	}
}
