package com.oneguysolutions.dialogs;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ShowErrorDialog extends Dialog {

	protected Object result;
	protected Shell shlError;
	private Text errText;
	private String errMessage;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ShowErrorDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}
	
	public ShowErrorDialog(Shell parent, String errMessage) {
		super(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		setText("SWT Dialog");
		this.errMessage = errMessage;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlError.open();
		shlError.layout();
		Display display = getParent().getDisplay();
		while (!shlError.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlError = new Shell(getParent(), getStyle());
		shlError.setSize(450, 300);
		shlError.setText("Error");
		shlError.setLayout(new GridLayout(1, false));
		
		errText = new Text(shlError, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		errText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		errText.setText(errMessage);
		
		Button btnClose = new Button(shlError, SWT.NONE);
		btnClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				closeTheDialog();
			}
		});
		btnClose.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btnClose.setText("&Close");
		btnClose.setFocus();
	}
	
	private void closeTheDialog(){
		shlError.close();
	}
	public Text getErrText() {
		return errText;
	}
}
