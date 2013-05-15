package com.oneguysolutions.generality.wizards;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.oneguysolutions.composites.ConnectionInformationComposite;
import com.oneguysolutions.dialogs.NewValueItem;
import com.oneguysolutions.dialogs.ShowErrorDialog;
import com.oneguysolutions.generality.connection.HSQLDBDefaultConnectionValues;
import com.oneguysolutions.generality.datamodel.ColumnDataModel;
import com.oneguysolutions.generality.datamodel.ForeignKeyDataModel;
import com.oneguysolutions.generality.datamodel.TableDataModel;
import com.oneguysolutions.generality.freemarker.FreeMarkerProcessor;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class RunTemplateWizard extends Wizard {

	private RunTemplateWizardPage1 one;
	private RunTemplateWizardPage2 two;
	private RunTemplateWizardPage3 three;
	private String templateLocation;
	private String log = "";
	
	//Array containing processed templates
	private List<String> processed = new Vector<String>(10);

	public RunTemplateWizard(String templatelocation) {
		super();
		setNeedsProgressMonitor(true);
		setWindowTitle("Run Template Wizard");
		this.templateLocation = templatelocation; 
	}
	
	@Override
	public void addPages() {
		one = new RunTemplateWizardPage1();
		two = new RunTemplateWizardPage2(SWT.NONE);
		three = new RunTemplateWizardPage3();
		addPage(one);
		addPage(two);
		addPage(three);
	}
	
	@Override
	public boolean performFinish() {
		Configuration cfg = new Configuration();
		
		try {
			cfg.setDirectoryForTemplateLoading(new File(this.templateLocation));
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		
		FreeMarkerProcessor.setConfig(cfg);
		
		FreeMarkerProcessor.getConfig().setObjectWrapper(new DefaultObjectWrapper());
		Map<String,Object> root = generaterootMap();
		String outputDirStr = ((RunTemplateWizardPage3)getPage("Third Page")).getOutputDir();
		try {
			File outputDir = new File(outputDirStr);
			
			List<TableDataModel> tables = (List<TableDataModel>)root.get("tables");
			
			for(TableDataModel tdm : tables ){
				process(new File(templateLocation), outputDir, tdm);	
			}
		} catch (IOException e) {
			e.printStackTrace();
			ShowErrorDialog errDialog = new ShowErrorDialog(getShell(), log + " " + e.getMessage() + " " + e.getCause());
			errDialog.open();
			return false;
		} catch (TemplateException e) {
			e.printStackTrace();
			ShowErrorDialog errDialog = new ShowErrorDialog(getShell(), log + "ERROR." + e.getMessage() + " " + e.getCause() + "\nStackTrace:\n" + e.getFTLInstructionStack());
			errDialog.open();
			return false;
		}
		
		finishHookUp();

		return true;
	}

	private void finishHookUp() {
		//Allocate here extra logic for some dbms before the conection is closed.
		RunTemplateWizardPage1 page1 = ((RunTemplateWizardPage1)getPage("First Page"));
		ConnectionInformationComposite comp = ((ConnectionInformationComposite)page1.getControl());
		
		//HSQLDB
		if(HSQLDBDefaultConnectionValues.TITLE.equals(comp.getDataBaseDriverCombo().getText())){
			Connection c = null;
			Statement st = null;
			try{
				c = page1.getConnection();
				st = c.createStatement();
				st.execute("SHUTDOWN");
				st.close();
				c.close();
			}catch(Exception ex){
				System.err.println(ex.getMessage());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> generaterootMap() {
		Tree tree = ((RunTemplateWizardPage2)getPage("Second Page")).getSelectedMetadataTree();
		Map<String, Object> root = new HashMap<String, Object>();
		List<TableDataModel> tables = new Vector<TableDataModel>();
		for(TreeItem tiTable : tree.getItems()){
			if(tiTable.getData("ItemType").equals("TABLE")){
				TableDataModel table = new TableDataModel((String)tiTable.getData("TableSchema"), tiTable.getText(),new Vector<ColumnDataModel>(),(List<ForeignKeyDataModel>)tiTable.getData("ForeignKeys"));
				for(TreeItem tiCol : tiTable.getItems()){
					if(tiCol.getData("ItemType").equals("COLUMN")){
						table.getColumns().add(new ColumnDataModel(tiCol.getText(),(Integer)tiCol.getData("ColumnDataType"),(Integer)tiCol.getData("ColumnSize"),(Boolean)tiCol.getData("IsPrimaryKey"),(Boolean)tiCol.getData("Nullable")));
					}
				}
				tables.add(table);
				table.setTables(tables);
			}
		}
        root.put("tables", tables);
		return root;
	}
	
	// If targetLocation does not exist, it will be created.
    public void copyDirectoriesOnly(File sourceLocation , File targetLocation)
    throws IOException {
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            
            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copyDirectoriesOnly(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        }
    }
    
    //If targetLocation does not exist, it will be created.
    public void process(File sourceLocation , File targetLocation, TableDataModel table)
    throws IOException, TemplateException {
        
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            
            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                process(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]),
                        table);
            }
        } else {
            
        	if(sourceLocation.getName().endsWith(".ftl")){
        		
        		log("\nProcessing " + sourceLocation.getName() + " for table " + table.getTableName() + " ...");
        		
        		if(processed.contains(sourceLocation.getName())) return;
        		
    			Template temp = FreeMarkerProcessor.getConfig().getTemplate(new File(this.templateLocation).toURI().relativize(sourceLocation.toURI()).getPath());
    			String targetLocationString = targetLocation.getAbsolutePath().substring(0,targetLocation.getAbsolutePath().lastIndexOf("."));
    			
    			if(targetLocationString.contains("TableNameCamelCase")){
    				String name = "";
    				for(String word : table.getTableName().split("_"))
    					name += word.substring(0,1).toUpperCase() + word.substring(1).toLowerCase();
    				targetLocationString = targetLocationString.replaceAll("TableNameCamelCase", name);
    			}
    			if(targetLocationString.contains("tableName"))
    				targetLocationString = targetLocationString.replaceAll("tableName", table.getTableName());
    			if(targetLocationString.contains("TableName"))
    				targetLocationString = targetLocationString.replaceAll("TableName", table.getTableName().substring(0,1).toUpperCase() + table.getTableName().substring(1));
    			
    			if(!sourceLocation.getName().contains("TableName") 
    					&& !sourceLocation.getName().contains("tableName") 
    					&& !sourceLocation.getName().contains("TableNameCamelCase"))
    				processed.add(sourceLocation.getName());
    			
    			File targetFile = new File(targetLocationString);
    			FileOutputStream fos = new FileOutputStream(targetFile);
    	        Writer out = new OutputStreamWriter(fos);
    	        
    			temp.process(table, out);
    			
    	        out.flush();
        		out.close();
        		
        		log("DONE.");
        	}else{
	            InputStream in = new FileInputStream(sourceLocation);
	            OutputStream out = new FileOutputStream(targetLocation);
	            
	            // Copy the bits from instream to outstream
	            byte[] buf = new byte[1024];
	            int len;
	            while ((len = in.read(buf)) > 0) {
	                out.write(buf, 0, len);
	            }
	            in.close();
	            out.close();
        	}
        }
    }

	private void log(String message) {
		System.out.print(message);
		log += message;
	}

}
