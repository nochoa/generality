package com.oneguysolutions.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.oneguysolutions.generality.connection.DefacultConnectionValues;
import com.oneguysolutions.generality.connection.HSQLDBDefaultConnectionValues;
import com.oneguysolutions.generality.connection.MySQLDefaultConnectionValues;
import com.oneguysolutions.generality.connection.PostgreSQLDefaultConnectionValues;
import com.oneguysolutions.generality.connection.SqlServerDefaultConnectionValues;

public class ConnectionInformationComposite extends Composite {

	
	private Label url = null;
	private Label lblUsuario = null;
	private Label lblPassword = null;
	public Text getTxtUrl() {
		return txtUrl;
	}

	public void setTxtUrl(Text txtUrl) {
		this.txtUrl = txtUrl;
	}

	public Text getTxtUsuario() {
		return txtUsuario;
	}

	public void setTxtUsuario(Text txtUsuario) {
		this.txtUsuario = txtUsuario;
	}

	public Text getTxtPassword() {
		return txtPassword;
	}

	public void setTxtPassword(Text txtPassword) {
		this.txtPassword = txtPassword;
	}
	public Button getBtnConnectToDatabase() {
		return btnConnectToDatabase;
	}

	public void setBtnConnectToDatabase(Button btnConnectToDatabase) {
		this.btnConnectToDatabase = btnConnectToDatabase;
	}
	
	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}
	
	public Combo getDataBaseDriverCombo() {
		return dataBaseDriverCombo;
	}

	public void setDataBaseDriverCombo(Combo dataBaseDriverCombo) {
		this.dataBaseDriverCombo = dataBaseDriverCombo;
	}

	private Text txtUrl = null;
	private Text txtUsuario = null;
	private Text txtPassword = null;
	private Combo dataBaseDriverCombo = null;
	private Label lblDatabase = null;
	private Button btnConnectToDatabase = null;
	private String driverClass = null;

	public ConnectionInformationComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		createDataBaseDriverCombo();
		url = new Label(this, SWT.NONE);
		url.setText("url:");
		url.setBounds(new Rectangle(14, 45, 17, 15));
		txtUrl = new Text(this, SWT.BORDER);
		txtUrl.setBounds(new Rectangle(119, 45, 257, 21));
		lblUsuario = new Label(this, SWT.NONE);
		lblUsuario.setText("user");
		lblUsuario.setBounds(new Rectangle(15, 74, 22, 15));
		txtUsuario = new Text(this, SWT.BORDER);
		txtUsuario.setBounds(new Rectangle(120, 75, 167, 21));
		lblPassword = new Label(this, SWT.NONE);
		lblPassword.setText("password");
		lblPassword.setBounds(new Rectangle(15, 105, 50, 15));
		txtPassword = new Text(this, SWT.PASSWORD | SWT.BORDER);
		txtPassword.setBounds(new Rectangle(119, 105, 167, 21));
		this.setLayout(null);
		this.setSize(new Point(392, 172));
		lblDatabase = new Label(this, SWT.NONE);
		lblDatabase.setBounds(new Rectangle(14, 15, 96, 15));
		lblDatabase.setText("Choose database");
		btnConnectToDatabase = new Button(this, SWT.NONE);
		btnConnectToDatabase.setBounds(new Rectangle(120, 135, 63, 25));
		btnConnectToDatabase.setText("&Connect");
	}

	/**
	 * This method initializes dataBaseDriverCombo	
	 */
	private void createDataBaseDriverCombo() {
		dataBaseDriverCombo = new Combo(this, SWT.NONE);
		dataBaseDriverCombo.setBounds(new Rectangle(120, 15, 166, 23));
		dataBaseDriverCombo.add(MySQLDefaultConnectionValues.TITLE);
		dataBaseDriverCombo.add(PostgreSQLDefaultConnectionValues.TITLE);
		dataBaseDriverCombo.add(HSQLDBDefaultConnectionValues.TITLE);
		dataBaseDriverCombo.add(SqlServerDefaultConnectionValues.TITLE);
		dataBaseDriverCombo.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				DefacultConnectionValues conval = null;
				if(MySQLDefaultConnectionValues.TITLE.equals(((Combo)e.widget).getText()))
					conval = new MySQLDefaultConnectionValues();
				if(PostgreSQLDefaultConnectionValues.TITLE.equals(((Combo)e.widget).getText()))
					conval = new PostgreSQLDefaultConnectionValues();
				if(HSQLDBDefaultConnectionValues.TITLE.equals(((Combo)e.widget).getText()))
					conval = new HSQLDBDefaultConnectionValues();
				if(SqlServerDefaultConnectionValues.TITLE.equals(((Combo)e.widget).getText()))
					conval = new SqlServerDefaultConnectionValues();
				
				txtUrl.setText(conval.getDefaultUrl());
				txtUsuario.setText(conval.getDefaultUser());
				txtPassword.setText(conval.getDefaultPassword());
				driverClass = conval.getDriverClass();
				txtUrl.setFocus();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
