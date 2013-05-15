package com.oneguysolutions.generality.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;

import com.oneguysolutions.composites.flex.SimpleDatagridComposite;

public class RunTemplateWizardPage3 extends WizardPage {
	private static String selectedDir; 
	private SimpleDatagridComposite container;
	
	public String getOutputDir(){
		return container.getTxtDir().getText();
	}

	public RunTemplateWizardPage3() {
		super("Third Page");
		setTitle("Select target directory");
		setDescription("Select the target directory for creation of the generated output.");
	}

	@Override
	public void createControl(Composite parent) {
		container = new SimpleDatagridComposite(parent, SWT.NULL);
		if(RunTemplateWizardPage3.selectedDir != null)
			container.getTxtDir().setText(RunTemplateWizardPage3.selectedDir);
		container.getBtnSearchDir().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dirDialog = new DirectoryDialog(getShell());
				dirDialog.setText("Select target directory");
				String selectedDir = dirDialog.open();
				if(selectedDir != null){
					container.getTxtDir().setText(selectedDir);
				}
				if(!container.getTxtDir().getText().equals("")){
					RunTemplateWizardPage3.selectedDir = container.getTxtDir().getText(); 
					setPageComplete(true);
				}else
					setPageComplete(false);
			}
		});
		setControl(container);
		if(!container.getTxtDir().getText().equals("")) 
			setPageComplete(true);
		else
			setPageComplete(false);
	}

}
