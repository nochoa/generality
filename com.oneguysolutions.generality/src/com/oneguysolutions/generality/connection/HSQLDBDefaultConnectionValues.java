package com.oneguysolutions.generality.connection;

public class HSQLDBDefaultConnectionValues extends DefacultConnectionValues{
	public static final String TITLE = "HSQLDB";
	public static final String DRIVER_CLASS = "org.hsqldb.jdbcDriver";
	public static final String DEFAULT_URL = "jdbc:hsqldb:database";
	public static final String DEFAULT_USER = "sa";
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
