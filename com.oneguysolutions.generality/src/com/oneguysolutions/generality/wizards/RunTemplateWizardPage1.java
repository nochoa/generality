package com.oneguysolutions.generality.wizards;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import com.oneguysolutions.composites.ConnectionInformationComposite;

public class RunTemplateWizardPage1 extends WizardPage {
	
	public static String driverClassStatic = "";
	public static String urlStatic = "";
	public static String usuarioStatic = "";
	public static String passwordStatic = "";
	
	private ConnectionInformationComposite container;
	private Connection connection;  //  @jve:decl-index=0:

	public RunTemplateWizardPage1() {
		super("First Page");
		setTitle("Connection Information");
		setDescription("Select connection information.");
	}

	@Override
	public void createControl(Composite parent) {
		container = new ConnectionInformationComposite(parent, SWT.NULL);
		container.setDriverClass(RunTemplateWizardPage1.driverClassStatic);
	 	container.getTxtUrl().setText(RunTemplateWizardPage1.urlStatic);
 		container.getTxtUsuario().setText(RunTemplateWizardPage1.usuarioStatic);
 		container.getTxtPassword().setText(RunTemplateWizardPage1.passwordStatic);
		container.getBtnConnectToDatabase().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				 try {
					 	RunTemplateWizardPage1.driverClassStatic = container.getDriverClass();
					 	RunTemplateWizardPage1.urlStatic = container.getTxtUrl().getText();
				 		RunTemplateWizardPage1.usuarioStatic = container.getTxtUsuario().getText();
				 		RunTemplateWizardPage1.passwordStatic = container.getTxtPassword().getText();
						Class.forName(container.getDriverClass()).newInstance();
						connection = DriverManager.getConnection(container.getTxtUrl().getText(), container.getTxtUsuario().getText(), container.getTxtPassword().getText());
						System.out.println("connection.getCatalog:" + connection.getCatalog());
						System.out.println("getCatalogTerm:" + connection.getMetaData().getCatalogTerm());
						System.out.println("getSchemaTerm:" + connection.getMetaData().getSchemaTerm());
						setPageComplete(true);
					} catch (Exception ex) {
						setPageComplete(false);
						MessageBox msgb = new MessageBox(container.getShell());
						msgb.setText("Error connecting database");
						msgb.setMessage("" + ex.getMessage());
						msgb.open();
						ex.printStackTrace();
					}
			}
		});
		container.getDataBaseDriverCombo().addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if(!connection.isClosed())
						connection.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				connection = null;
				setPageComplete(false);
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		setControl(container);
		setPageComplete(false);
	}
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}
