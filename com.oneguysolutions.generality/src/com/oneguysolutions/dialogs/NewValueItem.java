package com.oneguysolutions.dialogs;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Rectangle;

public class NewValueItem extends Dialog {

	protected Object result;
	protected Shell shlNewSequenceItem;
	private Text text;
	private Button btnOk;
	
	private String texto = "";
	
	public void setTexto(String texto){
		this.texto = texto;
	}
	
	private boolean insertDoubleQuotes = true;
	
	/**
	 * Create the dialog.
	 * @param parent
	 */
	public NewValueItem(Shell parent) {
		super(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		setText("Insert Item Sequence");
	}

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public NewValueItem(Shell parent, int style) {
		super(parent, style);
		setText("Insert Item Sequence");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlNewSequenceItem.open();
		shlNewSequenceItem.layout();
		Display display = getParent().getDisplay();
		while (!shlNewSequenceItem.isDisposed()) {
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
		shlNewSequenceItem = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shlNewSequenceItem.setSize(373, 87);
		shlNewSequenceItem.setText("New Value");
		shlNewSequenceItem.setLayout(new GridLayout(2, false));
		
		Rectangle bounds = getParent().getBounds();
		Rectangle rect = shlNewSequenceItem.getBounds();
		
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		
		shlNewSequenceItem.setLocation (x, y);
		
		Label lblValue = new Label(shlNewSequenceItem, SWT.NONE);
		lblValue.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblValue.setText("value");
		
		text = new Text(shlNewSequenceItem, SWT.BORDER);
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.CR)
					closeAndReturnValue();
				else if(insertDoubleQuotes && "1234567890.".indexOf(e.character) == -1 && text.equals("")){
					text.setText("\"" + text.getText() + "\"");
					text.setSelection(text.getText().length() - 1);
					insertDoubleQuotes = false;
				}
			}
		});
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text.setText(texto);
		new Label(shlNewSequenceItem, SWT.NONE);
		
		btnOk = new Button(shlNewSequenceItem, SWT.NONE);
		btnOk.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.character == SWT.CR)
					closeAndReturnValue();
			}
		});
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				closeAndReturnValue();
			}
		});
		btnOk.setText("Ok");

	}
	public Button getBtnOk() {
		return btnOk;
	}
	private void closeAndReturnValue(){
		result = text.getText() != null? text.getText() : "";
		shlNewSequenceItem.close();
	}
}
