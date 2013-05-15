package com.oneguysolutions.generality.datamodel;

import java.util.List;
import java.util.Vector;

public class TableDataModel {

	private String tableSchema;
	private String tableName;
	private Boolean multipleKey = false;
	private List<ColumnDataModel> columns;
	private List<ForeignKeyDataModel> foreignKeys = new Vector<ForeignKeyDataModel>();
	private List<TableDataModel> tables = new Vector<TableDataModel>();

	public TableDataModel(String tableSchema, String tableName, List<ColumnDataModel> columns, List<ForeignKeyDataModel> foreignKeys) {
		super();
		this.tableSchema = tableSchema;
		this.tableName = tableName;
		this.columns = columns;
		this.foreignKeys = foreignKeys;
	}

	public List<ColumnDataModel> getColumns() {
		return columns;
	}
	public void setColumns(List<ColumnDataModel> columns) {
		this.columns = columns;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Boolean getMultipleKey() {
		return multipleKey;
	}
	public void setMultipleKey(Boolean multipleKey) {
		this.multipleKey = multipleKey;
	}
	public List<ForeignKeyDataModel> getForeignKeys() {
		return foreignKeys;
	}
	public void setForeignKeys(List<ForeignKeyDataModel> foreignKeys) {
		this.foreignKeys = foreignKeys;
	}
	public List<TableDataModel> getTables() {
		return tables;
	}
	public void setTables(List<TableDataModel> tables) {
		this.tables = tables;
	}
	public String getTableSchema() {
		return tableSchema;
	}
	public void setTableSchema(String tableSchema) {
		this.tableSchema = tableSchema;
	}
}
