package com.oneguysolutions.generality.connection;

public class SqlServerDefaultConnectionValues extends DefacultConnectionValues{
	public static final String TITLE = "SqlServer";
	public static final String DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	public static final String DEFAULT_URL = "jdbc:sqlserver://localhost\\database:1433;databaseName=database";
	public static final String DEFAULT_USER = "sa";
	public static final String DEFAULT_PASSWORD = "sa";
	
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
