package com.oneguysolutions.generality.datamodel;


public class ColumnDataModel {

	private String columnName = "";
	
	private Boolean primaryKey = false;
	
	private Integer dataType;
	
	private Integer columnSize;
	
	private Boolean nullable = true;

	public ColumnDataModel(String columnName, Integer dataType, Integer columnSize, Boolean primaryKey, Boolean nullable) {
		super();
		this.columnName = columnName;
		this.dataType = dataType;
		this.columnSize = columnSize; 
		this.primaryKey = primaryKey;
		this.nullable = nullable;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public Boolean getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(Boolean primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	/**
	 * //  (version 1.5 : 49.0, super bit)
public class java.sql.Types {
  
  // Field descriptor #19 I
  public static final int BIT = -7;
  
  // Field descriptor #19 I
  public static final int TINYINT = -6;
  
  // Field descriptor #19 I
  public static final int SMALLINT = 5;
  
  // Field descriptor #19 I
  public static final int INTEGER = 4;
  
  // Field descriptor #19 I
  public static final int BIGINT = -5;
  
  // Field descriptor #19 I
  public static final int FLOAT = 6;
  
  // Field descriptor #19 I
  public static final int REAL = 7;
  
  // Field descriptor #19 I
  public static final int DOUBLE = 8;
  
  // Field descriptor #19 I
  public static final int NUMERIC = 2;
  
  // Field descriptor #19 I
  public static final int DECIMAL = 3;
  
  // Field descriptor #19 I
  public static final int CHAR = 1;
  
  // Field descriptor #19 I
  public static final int VARCHAR = 12;
  
  // Field descriptor #19 I
  public static final int LONGVARCHAR = -1;
  
  // Field descriptor #19 I
  public static final int DATE = 91;
  
  // Field descriptor #19 I
  public static final int TIME = 92;
  
  // Field descriptor #19 I
  public static final int TIMESTAMP = 93;
  
  // Field descriptor #19 I
  public static final int BINARY = -2;
  
  // Field descriptor #19 I
  public static final int VARBINARY = -3;
  
  // Field descriptor #19 I
  public static final int LONGVARBINARY = -4;
  
  // Field descriptor #19 I
  public static final int NULL = 0;
  
  // Field descriptor #19 I
  public static final int OTHER = 1111;
  
  // Field descriptor #19 I
  public static final int JAVA_OBJECT = 2000;
  
  // Field descriptor #19 I
  public static final int DISTINCT = 2001;
  
  // Field descriptor #19 I
  public static final int STRUCT = 2002;
  
  // Field descriptor #19 I
  public static final int ARRAY = 2003;
  
  // Field descriptor #19 I
  public static final int BLOB = 2004;
  
  // Field descriptor #19 I
  public static final int CLOB = 2005;
  
  // Field descriptor #19 I
  public static final int REF = 2006;
  
  // Field descriptor #19 I
  public static final int DATALINK = 70;
  
  // Field descriptor #19 I
  public static final int BOOLEAN = 16;
  
  // Field descriptor #19 I
  public static final int ROWID = -8;
  
  // Field descriptor #19 I
  public static final int NCHAR = -15;
  
  // Field descriptor #19 I
  public static final int NVARCHAR = -9;
  
  // Field descriptor #19 I
  public static final int LONGNVARCHAR = -16;
  
  // Field descriptor #19 I
  public static final int NCLOB = 2011;
  
  // Field descriptor #19 I
  public static final int SQLXML = 2009;
  
  // Method descriptor #1 ()V
  // Stack: 1, Locals: 1
  private Types();
    0  aload_0 [this]
    1  invokespecial java.lang.Object() [83]
    4  return

}
	 * @return
	 */
	public Integer getDataType() {
		return dataType;
	}
	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}
	
	public void setNullable(Boolean nullable) {
		this.nullable = nullable;
	}
	
	public Boolean getNullable() {
		return nullable;
	}
	public Integer getColumnSize() {
		return columnSize;
	}
	public void setColumnSize(Integer columnSize) {
		this.columnSize = columnSize;
	}
	
}
