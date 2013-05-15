package com.oneguysolutions.composites;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TreeItem;

public class DatabaseMetadataComposite extends Composite {

	private Tree metadataTree = null;

	private Button btnRefreshMetadataTree = null;

	private Composite compositeButtons = null;

	private Button btnRemove = null;

	private Button btnAdd = null;

	private Tree selectedMetadataTree = null;

	public DatabaseMetadataComposite(Composite parent, int style, int selectionTreeStyle) {
		super(parent, style);
		initialize(selectionTreeStyle);
	}

	public Button getBtnRemove() {
		return btnRemove;
	}

	public void setBtnRemove(Button btnRemove) {
		this.btnRemove = btnRemove;
	}

	public Button getBtnAdd() {
		return btnAdd;
	}

	public void setBtnAdd(Button btnAdd) {
		this.btnAdd = btnAdd;
	}

	public Tree getSelectedMetadataTree() {
		return selectedMetadataTree;
	}

	public void setSelectedMetadataTree(Tree selectedMetadataTree) {
		this.selectedMetadataTree = selectedMetadataTree;
	}

	private void initialize(int selectionTreeStyle) {
		GridData gridData31 = new GridData();
		gridData31.verticalAlignment = GridData.FILL;
		gridData31.grabExcessHorizontalSpace = true;
		gridData31.grabExcessVerticalSpace = true;
		gridData31.horizontalAlignment = GridData.FILL;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.makeColumnsEqualWidth = false;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		gridLayout.numColumns = 3;
		metadataTree = new Tree(this, SWT.NONE);
		metadataTree.setLayoutData(gridData);
		createCompositeButtons();
		selectedMetadataTree = new Tree(this, selectionTreeStyle);
		selectedMetadataTree.setLayoutData(gridData31);
		btnRefreshMetadataTree = new Button(this, SWT.NONE);
		btnRefreshMetadataTree.setText("&Refresh");
		this.setLayout(gridLayout);
		setSize(new Point(300, 200));
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
	}
	
	public Tree getMetadataTree() {
		return metadataTree;
	}

	public void setMetadataTree(Tree metadataTree) {
		this.metadataTree = metadataTree;
	}
	
	public Button getBtnRefreshMetadataTree() {
		return btnRefreshMetadataTree;
	}

	public void setBtnRefreshMetadataTree(Button btnRefreshMetadataTree) {
		this.btnRefreshMetadataTree = btnRefreshMetadataTree;
	}

	/**
	 * This method initializes compositeButtons	
	 *
	 */
	private void createCompositeButtons() {
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = GridData.FILL;
		gridData4.grabExcessHorizontalSpace = false;
		gridData4.grabExcessVerticalSpace = false;
		gridData4.verticalAlignment = GridData.FILL;
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.FILL;
		gridData2.grabExcessHorizontalSpace = false;
		gridData2.verticalAlignment = GridData.FILL;
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = GridData.CENTER;
		gridData3.grabExcessHorizontalSpace = false;
		gridData3.grabExcessVerticalSpace = false;
		gridData3.widthHint = 66;
		gridData3.verticalAlignment = GridData.CENTER;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.makeColumnsEqualWidth = true;
		gridLayout1.verticalSpacing = 5;
		gridLayout1.marginWidth = 5;
		gridLayout1.marginHeight = 5;
		gridLayout1.horizontalSpacing = 5;
		compositeButtons = new Composite(this, SWT.NONE);
		compositeButtons.setLayout(gridLayout1);
		compositeButtons.setLayoutData(gridData3);
		btnAdd = new Button(compositeButtons, SWT.NONE);
		btnAdd.setText("&Add");
		btnAdd.setLayoutData(gridData2);
		btnRemove = new Button(compositeButtons, SWT.NONE);
		btnRemove.setText("R&emove");
		btnRemove.setLayoutData(gridData4);
	}

}  //  @jve:decl-index=0:visual-constraint="-19,11"
