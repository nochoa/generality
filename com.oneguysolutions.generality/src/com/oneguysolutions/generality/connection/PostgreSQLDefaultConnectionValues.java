package com.oneguysolutions.generality.connection;

public class PostgreSQLDefaultConnectionValues extends DefacultConnectionValues{
	public static final String TITLE = "PostgreSQL";
	public static final String DRIVER_CLASS = "org.postgresql.Driver";
	public static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/database";
	public static final String DEFAULT_USER = "postgres";
	public static final String DEFAULT_PASSWORD = "postgres";
	
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
