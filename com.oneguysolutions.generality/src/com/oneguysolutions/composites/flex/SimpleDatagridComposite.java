package com.oneguysolutions.composites.flex;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class SimpleDatagridComposite extends Composite {

	private Label lblDir = null;
	private Text txtDir = null;
	private Button btnSearchDir = null;
	
	public Text getTxtDir() {
		return txtDir;
	}

	public void setTxtDir(Text txtDir) {
		this.txtDir = txtDir;
	}

	public Button getBtnSearchDir() {
		return btnSearchDir;
	}

	public void setBtnSearchDir(Button btnSearchDir) {
		this.btnSearchDir = btnSearchDir;
	}

	public SimpleDatagridComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridData gridData2 = new GridData();
		gridData2.widthHint = 200;
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.BEGINNING;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = GridData.CENTER;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.END;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.CENTER;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.makeColumnsEqualWidth = false;
		lblDir = new Label(this, SWT.NONE);
		lblDir.setText("Target directory");
		lblDir.setLayoutData(gridData);
		txtDir = new Text(this, SWT.BORDER);
		txtDir.setLayoutData(gridData2);
		btnSearchDir = new Button(this, SWT.NONE);
		btnSearchDir.setText("B&rowse");
		btnSearchDir.setLayoutData(gridData1);
		this.setLayout(gridLayout);
		setSize(new Point(366, 35));
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
