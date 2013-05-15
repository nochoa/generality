package com.oneguysolutions.composites;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;

public class OptionsPageComposite extends Composite {
	private Tree tree;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public OptionsPageComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		tree = new Tree(this, SWT.BORDER);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public Tree getTree() {
		return tree;
	}
}
