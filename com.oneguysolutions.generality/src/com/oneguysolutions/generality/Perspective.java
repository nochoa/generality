package com.oneguysolutions.generality;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		//layout.setEditorAreaVisible(false);
		layout.addStandaloneView("com.oneguysolutions.generality.views.navigatorView",  true, IPageLayout.LEFT, 1.0f, layout.getEditorArea());
	}
}
