package com.oneguysolutions.generality.wizards;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.oneguysolutions.composites.DatabaseMetadataComposite;
import com.oneguysolutions.generality.Activator;
import com.oneguysolutions.generality.datamodel.ForeignKeyDataModel;

public class RunTemplateWizardPage2 extends WizardPage {
	private DatabaseMetadataComposite container;
	private DatabaseMetaData dbMetaData;
	private int idItemGenerator = 0;
	private int selectionTreeStyle = SWT.NONE;

	public RunTemplateWizardPage2(int selectionTreeStyle) {
		super("Second Page");
		setTitle("Select Table");
		setDescription("Select the tables that will serve as datamodel for the generated files.");
		this.selectionTreeStyle = selectionTreeStyle;
	}

	private static Image catalogImage; 
	private static Image schemaImage;
	private static Image tableImage;
	private static Image columnsImage;
	
	
	private Image createImage(String entry) {
		ImageDescriptor descriptor = Activator.getImageDescriptor(entry);
		return descriptor.createImage();
	}
	
	@Override
	public void createControl(Composite parent) {
		
		catalogImage = createImage("icons/window_16.gif");
		schemaImage = createImage("icons/schema.gif");
		tableImage = createImage("icons/table.gif");
		columnsImage = createImage("icons/columns.gif");
		
		container = new DatabaseMetadataComposite(parent, SWT.FILL, selectionTreeStyle);
		container.getBtnRefreshMetadataTree().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fillMetadataTree();
				container.getMetadataTree().setFocus();
			}
		});
		container.getMetadataTree().addListener (SWT.Expand, new Listener () {
			public void handleEvent (final Event event) {
				final TreeItem root = (TreeItem) event.item;
				TreeItem [] items = root.getItems ();
				for (int i= 0; i<items.length; i++) {
					if (items [i].getData () != null) return;
					items [i].dispose ();
				}
				ResultSet rs = null;
				if(root.getData("ItemType").equals("CATALOG")){
					try {
						try{
							rs = dbMetaData.getSchemas(root.getText(),null);
						}catch(Exception ex){
							System.out.println("Cannot fetch schema with narrow search. Fetching all.");
							rs = dbMetaData.getSchemas();
						}
						
						while(rs.next()){
							TreeItem tiSchema = new TreeItem(root,0);
							tiSchema.setText(rs.getString("TABLE_SCHEM"));
							//tiSchema.setImage(new Image(container.getDisplay(), "icons/schema.gif"));
							tiSchema.setData(idItemGenerator++);
							tiSchema.setData("ItemType", "SCHEMA");
							tiSchema.setImage(schemaImage);
							new TreeItem(tiSchema,0);
						}
					} catch (Exception e) {
						try {
							//If we reach here its because the database driver doesnt support the getSchemas method with catalog filter
							rs = dbMetaData.getSchemas();
							while(rs.next()){
								TreeItem tiSchema = new TreeItem(root,0);
								tiSchema.setText(rs.getString("TABLE_SCHEM"));
								tiSchema.setData(idItemGenerator++);
								tiSchema.setData("ItemType", "SCHEMA");
								tiSchema.setImage(schemaImage);
								new TreeItem(tiSchema,0);
							}
						} catch (Exception e1) {
							TreeItem ti = new TreeItem(root,0);
							ti.setText("<Error al obtener schemas>" + e1.getMessage());
							e1.printStackTrace();
						}
					}
				}else if(root.getData("ItemType").equals("SCHEMA")){
					try {
						String[] tableTypes = {"TABLE"};
						if(dbMetaData.getSchemaTerm().equals(""))
							rs = dbMetaData.getTables(root.getText(), null, null, tableTypes);
						else
							rs = dbMetaData.getTables(null, root.getText(), null, tableTypes);
						while(rs.next()){
							System.out.println("TABLE_CAT:::" + rs.getString("TABLE_CAT") + " TABLE_SCHEM:::" + rs.getString("TABLE_SCHEM"));
							TreeItem tiTable = new TreeItem(root,0);
							tiTable.setText(rs.getString("TABLE_NAME"));
							//TODO: OPTIMIZAR LA CARGA DE IMAGENES (ver dispose() y cia)
//							System.out.println(new File("icons/table.gif").getAbsolutePath());
//							System.out.println(new File("icons/table.gif").exists());
//							tiTable.setImage(new Image(container.getDisplay(), new File("icons/table.gif").getAbsolutePath()));
							tiTable.setData(idItemGenerator++);
							tiTable.setData("ItemType", "TABLE");
							tiTable.setData("TableSchema", rs.getString("TABLE_SCHEM"));
							tiTable.setImage(tableImage);
							new TreeItem(tiTable,0);
						}
					} catch (Exception e1) {
						TreeItem ti = new TreeItem(root,0);
						ti.setText("<Error al obtener schemas>" + e1.getMessage());
						e1.printStackTrace();
					}
				}else if(root.getData("ItemType").equals("TABLE")){
					obtainTableInformation(root);
				}
			}
		});
		/**
		 * Its VERY IMPORTANT to dispose the images that will be holding resources from the OS.
		 */
		container.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				catalogImage.dispose();
				schemaImage.dispose();
				tableImage.dispose();
				columnsImage.dispose();
				System.out.println("I disposed the images ;)");
			}
		});
		container.getBtnAdd().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				copyMetadataTree();
			}
		});
		container.getBtnRemove().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeSubTree();
			}
		});
		setControl(container);
		setPageComplete(false);
	}
	
	private void removeSubTree() {
		TreeItem[] selectedItems = container.getSelectedMetadataTree().getSelection();
		for(TreeItem tiOrig : selectedItems)
			tiOrig.dispose();
	}
	
	private void copyMetadataTree() {
		TreeItem[] selectedItems = container.getMetadataTree().getSelection();
		for(TreeItem tiOrig : selectedItems){
			if(tiOrig.getData("ItemType").equals("COLUMN")){
				TreeItem tiTabla = null;
				for(TreeItem ti : container.getSelectedMetadataTree().getItems()){
					if(ti.getData().equals(tiOrig.getParentItem().getData())){
						tiTabla = ti;
						break;
					}
				}
				if(tiTabla == null){
					tiTabla = new TreeItem(container.getSelectedMetadataTree(), 0);
					tiTabla.setData(tiOrig.getParentItem().getData());
					tiTabla.setData("ItemType",tiOrig.getParentItem().getData("ItemType"));
					tiTabla.setData("TableSchema",tiOrig.getParentItem().getData("TableSchema"));
					tiTabla.setData("ForeignKeys",tiOrig.getParentItem().getData("ForeignKeys"));
					tiTabla.setImage(tableImage);
					tiTabla.setText(tiOrig.getParentItem().getText());
				}
				TreeItem tiCol = new TreeItem(tiTabla, 0);
				tiCol.setText(tiOrig.getText());
				tiCol.setData(tiOrig.getData());
				tiCol.setData("ItemType",tiOrig.getData("ItemType"));
				tiCol.setImage(columnsImage);
				tiCol.setData("ColumnDataType",tiOrig.getData("ColumnDataType"));
				tiCol.setData("ColumnSize",tiOrig.getData("ColumnSize"));
				tiCol.setData("IsPrimaryKey",(Boolean)tiOrig.getData("IsPrimaryKey"));
				tiCol.setData("Nullable",(Boolean)tiOrig.getData("Nullable"));
			}else if(tiOrig.getData("ItemType").equals("TABLE")){
				copyTableMetadata(tiOrig);
			}else if(tiOrig.getItems().length > 0){
				if(tiOrig.getItem(0).getData("ItemType").equals("TABLE")){
					for(TreeItem tiTable : tiOrig.getItems())
						copyTableMetadata(tiTable);
				}
			}
		}
		if(container.getSelectedMetadataTree().getItemCount() > 0)
			setPageComplete(true);
	}
	
	private void copyTableMetadata(TreeItem tiTableOrig){
		TreeItem tiTabla = null;
		for(TreeItem ti : container.getSelectedMetadataTree().getItems()){
			if(ti.getData().equals(tiTableOrig.getData())){
				tiTabla = ti;
				break;
			}
		}
		if(tiTabla == null){
			tiTabla = new TreeItem(container.getSelectedMetadataTree(), 0);
			tiTabla.setData(tiTableOrig.getData());
			tiTabla.setData("ItemType",tiTableOrig.getData("ItemType"));
			tiTabla.setData("TableSchema",tiTableOrig.getData("TableSchema"));
			tiTabla.setData("ForeignKeys",tiTableOrig.getData("ForeignKeys"));
			tiTabla.setImage(tableImage);
			tiTabla.setText(tiTableOrig.getText());
			obtainTableInformation(tiTabla);
		}
	}
	
	private void obtainTableInformation(TreeItem root) {
		ResultSet rs = null;
		try {
			//TODO: CATALOG/SCHEMA DATA INFORMATION NEEDS TO BE SETTED HERE
			rs = dbMetaData.getColumns(null, (String)root.getData("TableSchema"), root.getText(), null);
			
			ResultSet rsPks = null;
			if(dbMetaData.getSchemaTerm().equals(""))
				rsPks = dbMetaData.getPrimaryKeys(null, null, root.getText());
			else
				rsPks = dbMetaData.getPrimaryKeys(null, (String)root.getData("TableSchema"), root.getText());
			List<String> primaryKeys = new Vector<String>();
			//PRIMARY KEYS INFO
			while(rsPks.next()){
				System.out.println("=============Primary Key Info=================");
				System.out.println("COLUMN_NAME:" + rsPks.getString("COLUMN_NAME"));
				primaryKeys.add(rsPks.getString("COLUMN_NAME"));
			}
			//FOREIGN KEYS INFO
			ResultSet rsFks = null;
			if(dbMetaData.getSchemaTerm().equals(""))
				rsFks = dbMetaData.getImportedKeys(null, null, root.getText());
			else
				rsFks = dbMetaData.getImportedKeys(null, (String)root.getData("TableSchema"), root.getText());
			List<ForeignKeyDataModel> foreignKeys = new Vector<ForeignKeyDataModel>();
			while(rsFks.next()){
				ForeignKeyDataModel fkdm = new ForeignKeyDataModel();
				System.out.println("=============Foreign Key Info=================");
				System.out.println("PKTABLE_CAT:" + rsFks.getString("PKTABLE_CAT"));
				fkdm.setPktableCat(rsFks.getString("PKTABLE_CAT"));
				System.out.println("PKTABLE_SCHEM:" + rsFks.getString("PKTABLE_SCHEM"));
				fkdm.setPktableSchem(rsFks.getString("PKTABLE_SCHEM"));
				System.out.println("PKTABLE_NAME:" + rsFks.getString("PKTABLE_NAME"));
				fkdm.setPktableName(rsFks.getString("PKTABLE_NAME"));
				System.out.println("PKCOLUMN_NAME:" + rsFks.getString("PKCOLUMN_NAME"));
				fkdm.setPkcolumnName(rsFks.getString("PKCOLUMN_NAME"));
				System.out.println("FKTABLE_CAT:" + rsFks.getString("FKTABLE_CAT"));
				fkdm.setFktableCat(rsFks.getString("FKTABLE_CAT"));
				System.out.println("FKTABLE_SCHEM:" + rsFks.getString("FKTABLE_SCHEM"));
				fkdm.setFktableSchem(rsFks.getString("FKTABLE_SCHEM"));
				System.out.println("FKTABLE_NAME:" + rsFks.getString("FKTABLE_NAME"));
				fkdm.setFktableName(rsFks.getString("FKTABLE_NAME"));
				System.out.println("FKCOLUMN_NAME:" + rsFks.getString("FKCOLUMN_NAME"));
				fkdm.setFkcolumnName(rsFks.getString("FKCOLUMN_NAME"));
				System.out.println("KEY_SEQ:" + rsFks.getShort("KEY_SEQ"));
				fkdm.setKeySeq(rsFks.getShort("KEY_SEQ"));
				System.out.println("UPDATE_RULE:" + rsFks.getShort("UPDATE_RULE"));
				fkdm.setUpdateRule(rsFks.getShort("UPDATE_RULE"));
				System.out.println("DELETE_RULE:" + rsFks.getShort("DELETE_RULE"));
				fkdm.setDeleteRule(rsFks.getShort("DELETE_RULE"));
				System.out.println("FK_NAME:" + rsFks.getString("FK_NAME"));
				fkdm.setFkName(rsFks.getString("FK_NAME"));
				System.out.println("PK_NAME:" + rsFks.getString("PK_NAME"));
				fkdm.setPkName(rsFks.getString("PK_NAME"));
				System.out.println("DEFERRABILITY:" + rsFks.getShort("DEFERRABILITY"));
				fkdm.setDeferrability(rsFks.getShort("DEFERRABILITY"));
				System.out.println("==============================================");
				foreignKeys.add(fkdm);
			}
			root.setData("ForeignKeys", foreignKeys);
			//INDEXES INFO
			ResultSet rsIdx = dbMetaData.getIndexInfo(null, null, root.getText(), false, true);
			//List<ForeignKeyDataModel> indexes = new Vector<ForeignKeyDataModel>();
			
			while(rsIdx.next()){
				System.out.println("=============Indexes Info=================");
				System.out.println("TABLE_CAT:" + rsIdx.getString("TABLE_CAT"));
				System.out.println("TABLE_SCHEM:" + rsIdx.getString("TABLE_SCHEM"));
				System.out.println("TABLE_NAME:" + rsIdx.getString("TABLE_NAME"));
				System.out.println("NON_UNIQUE boolean:" + rsIdx.getBoolean("NON_UNIQUE"));
				System.out.println("INDEX_QUALIFIER:" + rsIdx.getString("INDEX_QUALIFIER"));
				System.out.println("INDEX_NAME:" + rsIdx.getString("INDEX_NAME"));
				System.out.println("TYPE short:" + rsIdx.getShort("TYPE"));
				System.out.println("ORDINAL_POSITION short:" + rsIdx.getShort("ORDINAL_POSITION"));
				System.out.println("COLUMN_NAME:" + rsIdx.getString("COLUMN_NAME"));
				System.out.println("ASC_OR_DESC:" + rsIdx.getString("ASC_OR_DESC"));
				System.out.println("CARDINALITY int:" + rsIdx.getInt("CARDINALITY")); 
				System.out.println("PAGES int:" + rsIdx.getInt("PAGES"));
				System.out.println("FILTER_CONDITION:" + rsIdx.getString("FILTER_CONDITION"));
				System.out.println("==============================================");
			}
			
			//COLUMNS INFO
			while(rs.next()){
				TreeItem tiColumn = new TreeItem(root,0);
				tiColumn.setText(rs.getString("COLUMN_NAME"));
				tiColumn.setData(idItemGenerator++);
				tiColumn.setData("ItemType", "COLUMN");
				tiColumn.setImage(columnsImage);
				tiColumn.setData("ColumnDataType",rs.getInt("DATA_TYPE"));
				tiColumn.setData("ColumnSize",rs.getInt("COLUMN_SIZE"));
				tiColumn.setData("IsPrimaryKey",primaryKeys.contains(tiColumn.getText()));
				tiColumn.setData("Nullable",rs.getObject("IS_NULLABLE").equals("YES"));
			}
		} catch (Exception e1) {
			TreeItem ti = new TreeItem(root,0);
			ti.setText("<Error al obtener schemas>" + e1.getMessage());
			e1.printStackTrace();
		}
	}

	private void fillMetadataTree() {
		try {
			container.getMetadataTree().removeAll();
			dbMetaData = ((RunTemplateWizardPage1)this.getPreviousPage()).getConnection().getMetaData();
			ResultSet catalogs = null;
			String itemName = "TABLE_CAT";
			String itemType = "CATALOG";
			if(!dbMetaData.getSchemaTerm().equals("") && !dbMetaData.getCatalogTerm().equals(""))
				catalogs = dbMetaData.getCatalogs();
			else if(dbMetaData.getSchemaTerm().equals("")){
				catalogs = dbMetaData.getCatalogs();
				itemName = "TABLE_CAT";
				itemType = "SCHEMA";
			}
			else if(dbMetaData.getCatalogTerm().equals("")){
				catalogs = dbMetaData.getSchemas();
				itemName = "TABLE_SCHEM";
				itemType = "SCHEMA";
			}
			while(catalogs != null && catalogs.next()){
				TreeItem ti = new TreeItem(container.getMetadataTree(), 0);
				ti.setText(catalogs.getString(itemName));
				ti.setData("ItemName", itemName);
				ti.setData("ItemType", itemType);
				ti.setImage(catalogImage);
				new TreeItem(ti, 0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Tree getSelectedMetadataTree() {
		return container.getSelectedMetadataTree();
	}

}
