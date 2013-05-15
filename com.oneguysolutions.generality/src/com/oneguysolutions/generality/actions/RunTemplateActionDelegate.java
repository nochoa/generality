package com.oneguysolutions.generality.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IViewPart;

import com.oneguysolutions.generality.wizards.RunTemplateWizard;

public class RunTemplateActionDelegate implements org.eclipse.ui.IViewActionDelegate{

	IProject selectedProject;
	IViewPart theView;
	
	public RunTemplateActionDelegate() {
		
	}

	@Override
	public void init(IViewPart vp) {
		theView = vp;
	}

	@Override
	public void run(IAction arg0) {
		String templateLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString() + selectedProject.getFullPath().toString();
		RunTemplateWizard wizard = new RunTemplateWizard(templateLocation);
		WizardDialog dialog = new WizardDialog(theView.getSite().getShell(), wizard);
		dialog.open();
	}

	@Override
	public void selectionChanged(IAction act, ISelection sel) {
		if(((TreeSelection)sel).getFirstElement() instanceof IProject){
			this.selectedProject = (IProject)((TreeSelection)sel).getFirstElement();
		}
	}

}
