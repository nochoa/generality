package com.oneguysolutions.generality.editors;


import java.io.StringWriter;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.*;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.ide.IDE;
import org.jboss.ide.eclipse.freemarker.editor.Editor;

import com.oneguysolutions.composites.OptionsPageComposite;
import com.oneguysolutions.dialogs.NewValueItem;
import com.oneguysolutions.generality.Activator;

/**
 * Options editor for Generality.
 * <ul>
 * <li>page 0 contains a freemarker text editor.
 * <li>page 1 allows you to change the options in a visual manner.
 * </ul>
 */
public class GeneralityOptionsMultiPageEditor extends MultiPageEditorPart implements IResourceChangeListener{
	
	//TreeItem types for the Editor Tree
	private final static String HASH_ITEM = "hash_item";
	private final static String SEQUENCE_ITEM = "sequence_item";
	private final static String SEQUENCE_HOLDER = "sequence_holder";
	private final static String ASSING_ITEM = "assign_item";
	//TreeItem type property name
	private final static String ITEM_TYPE_DATA = "type";
	
	//Start and End of assign property name for the setData(...) of the assign tree item type
	private final static String ASSIGN_OFFSET = "assign_offset";
	private final static String ASSIGN_LENGTH = "assign_length";

	/** The text editor used in page 0. */
	private Editor editor;
	
	private OptionsPageComposite optPageComposite;
	
	private static Image logoImage;
	private static Image sharpImage;
	private static Image equalImage;
	private static Image sequenceImage;
	private static Image itemImage;
	private static Image hashItemImage;
	
	/**
	 * Creates a multi-page editor.
	 */
	public GeneralityOptionsMultiPageEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}
	/**
	 * Creates page 0 of the multi-page editor,
	 * which contains a text editor.
	 */
	void createPage0() {
		try {
			editor = new Editor();
			int index = addPage(editor, getEditorInput());
			setPageText(index, editor.getTitle());
		} catch (PartInitException e) {
			ErrorDialog.openError(
				getSite().getShell(),
				"Error creating nested text editor",
				null,
				e.getStatus());
		}
	}
	
	void createPage1() {
		optPageComposite = new OptionsPageComposite(getContainer(), SWT.NONE);
		optPageComposite.getTree().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.INSERT)
					addNewSequenceItem(((Tree)e.getSource()).getSelection());
				else if(e.keyCode == SWT.CR)
					addNewSequenceItem(((Tree)e.getSource()).getSelection());
				else if(e.character == SWT.DEL){
					for(TreeItem tiSelected : ((Tree)e.getSource()).getSelection()){
						tiSelected.dispose();
					}
				}
			}

			private void addNewSequenceItem(TreeItem[] selection) {
				for(TreeItem tiSelected : selection ){
					if(tiSelected.getData(ITEM_TYPE_DATA) == null){
						NewValueItem  seqItDialog = new NewValueItem(getContainer().getShell());
						seqItDialog.setTexto(tiSelected.getText());
						String value = (String)seqItDialog.open();
					}else if(tiSelected.getData(ITEM_TYPE_DATA).equals(SEQUENCE_ITEM)){
						NewValueItem  seqItDialog = new NewValueItem(getContainer().getShell());
						String value = (String)seqItDialog.open();
						if(value != null)
							createSequenceTreeItem(tiSelected,value);
					}
				}
			}
			
		});
		int index = addPage(optPageComposite);
		setPageText(index, "Tree Editor");
	}
	
	/**
	 * Creates the pages of the multi-page editor.
	 */
	protected void createPages() {
		createPage0();
		createPage1();
	}
	/**
	 * The <code>MultiPageEditorPart</code> implementation of this 
	 * <code>IWorkbenchPart</code> method disposes all nested editors.
	 * Subclasses may extend.
	 */
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		if(logoImage != null){ logoImage.dispose(); logoImage = null; }
		if(sharpImage != null){ sharpImage.dispose(); sharpImage = null; } 
		if(equalImage != null){ equalImage.dispose(); equalImage = null; }
		if(sequenceImage != null){ sequenceImage.dispose(); sequenceImage = null; }
		if(itemImage != null){ itemImage.dispose(); itemImage = null; }
		if(hashItemImage != null){ hashItemImage.dispose(); hashItemImage = null; }
		
		super.dispose();
	}
	/**
	 * Saves the multi-page editor's document.
	 */
	public void doSave(IProgressMonitor monitor) {
		getEditor(0).doSave(monitor);
	}
	/**
	 * Saves the multi-page editor's document as another file.
	 * Also updates the text for page 0's tab, and updates this multi-page editor's input
	 * to correspond to the nested editor's.
	 */
	public void doSaveAs() {
		IEditorPart editor = getEditor(0);
		editor.doSaveAs();
		setPageText(0, editor.getTitle());
		setInput(editor.getEditorInput());
	}
	/* (non-Javadoc)
	 * Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}
	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	public void init(IEditorSite site, IEditorInput editorInput)
		throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput))
			throw new PartInitException("Invalid Input: Must be IFileEditorInput");
		super.init(site, editorInput);
	}
	/* (non-Javadoc)
	 * Method declared on IEditorPart.
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}
	/**
	 * Calculates the contents of page 2 when the it is activated.
	 */
	protected void pageChange(int newPageIndex) {
		try{
			super.pageChange(newPageIndex);
		}catch(Exception ex){
			System.out.println("" + ex);
		}
		if (newPageIndex == 1) {
			generateProperties();
		}
	}

	/**
	 * Closes all project files on project close.
	 */
	public void resourceChanged(final IResourceChangeEvent event){
		if(event.getType() == IResourceChangeEvent.PRE_CLOSE){
			Display.getDefault().asyncExec(new Runnable(){
				public void run(){
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
					for (int i = 0; i<pages.length; i++){
						if(((FileEditorInput)editor.getEditorInput()).getFile().getProject().equals(event.getResource())){
							IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
							pages[i].closeEditor(editorPart,true);
						}
					}
				}            
			});
		}
	}

	void generateProperties() {
		String editorText = editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
		
		for(TreeItem ti : optPageComposite.getTree().getItems())
			ti.dispose();
		
		int assignOffset = 0;
		int assignLength = 0;
		
		while(true){
			assignOffset = editorText.indexOf("<#assign ", assignOffset);
			
			if(assignOffset == -1) break;
			
			assignLength = searchCorrespondingIndex('<', '>', editorText.substring(assignOffset));
			
			if(editorText.charAt(assignOffset + assignLength - 1) == '/')
				assignLength--;

			int i = editorText.substring(assignOffset, assignOffset + assignLength + 1).indexOf(" = ");
			
			if(i > 0){
				TreeItem t = new TreeItem(optPageComposite.getTree(), SWT.NONE);
				t.setData(ITEM_TYPE_DATA,ASSING_ITEM);
				t.setData(ASSIGN_OFFSET,assignOffset);
				t.setData(ASSIGN_LENGTH,assignLength);
				if(equalImage == null) 
					equalImage = createImage("icons/equal-sign.gif");
				t.setImage(equalImage);
				t.setText(editorText.substring(assignOffset + 9, assignOffset + i));
				t.setExpanded(true);
				try{
					generateProperties(editorText.substring(assignOffset + i + 3, assignOffset + assignLength), t);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				
			}
			
			assignOffset = assignOffset + assignLength + 1;
		}
		
		//Expand al nodes of the tree
		optPageComposite.getTree().setRedraw(false);
		for(TreeItem ti : optPageComposite.getTree().getItems()){
			ti.setExpanded(true);
			expandNodes(ti);
		}
		optPageComposite.getTree().setRedraw(true);
		optPageComposite.getTree().redraw();
	}
	private void expandNodes(TreeItem ti) {
		for(TreeItem tis : ti.getItems()){
			tis.setExpanded(true);
			expandNodes(tis);
		}
	}
	private void generateProperties(String substr, TreeItem parent) {
		String str = substr.trim();
		
		System.out.println("generateProperties(" + substr  + ", " + parent + ")");
		
		switch (str.charAt(0)) {
			case '[':{
				int closeBrackeTindex = searchCorrespondingIndex('[',']',str);
				TreeItem tiSequence = new TreeItem(parent, SWT.NONE);
				if(sequenceImage == null) 
					sequenceImage = createImage("icons/sequence.gif");
				tiSequence.setImage(sequenceImage);
				tiSequence.setData(ITEM_TYPE_DATA,SEQUENCE_ITEM);
				//tiSequence.setText("[]");
				String strSequence = str.substring(1, closeBrackeTindex).trim();
				int indexOfComa = 0;
				
				while(indexOfComa != -1 && strSequence.length() > 0){
					
					switch (strSequence.charAt(0)) {
						case '[':{
							int lastIndex = searchCorrespondingIndex('[',']',strSequence);
							generateProperties(strSequence.substring(0, lastIndex + 1), tiSequence);
							strSequence = strSequence.substring(lastIndex + 1).trim();
							break;
						}
						case '{':{
							int lastIndex = searchCorrespondingIndex('{','}',strSequence);
							generateProperties(strSequence.substring(0, lastIndex + 1), tiSequence);
							strSequence = strSequence.substring(lastIndex + 1).trim();
							break;
						}
						default : {
							indexOfComa = strSequence.indexOf(",");
							generateProperties(indexOfComa != -1 ? strSequence.substring(0,indexOfComa) : strSequence, tiSequence);
							if(indexOfComa != -1)
								strSequence = strSequence.substring(indexOfComa + 1).trim();
							else
								strSequence = "";
							break;
						}
					}
					if(strSequence.length() > 0 && strSequence.charAt(0) == ',')
						strSequence = strSequence.substring(1).trim();
					indexOfComa = strSequence.indexOf(",");
				}
				break;
			}
			case '{':{
				TreeItem tiHash = new TreeItem(parent, SWT.NONE);
				
				if(sharpImage == null) 
					sharpImage = createImage("icons/hash.gif");
				
				tiHash.setImage(sharpImage);
				tiHash.setData(ITEM_TYPE_DATA,HASH_ITEM);
				//tiHash.setText("{}");
				int indexOfComa = 0;
				int indexOfColon = 0;
				String strHash = str.substring(1, str.length() - 1).trim();
				
				while(indexOfColon != -1){
					TreeItem tiItemHash = new TreeItem(tiHash, SWT.NONE);
					
					String strElem = strHash.substring(0,strHash.indexOf(":")).trim();
					strHash = strHash.substring(strHash.indexOf(":") + 1).trim();
					
					if(hashItemImage == null) 
						hashItemImage = createImage("icons/hash_item.gif");
					
					tiItemHash.setImage(hashItemImage);
					tiItemHash.setText(strElem);
					indexOfComa = strHash.indexOf(",");
					
					switch (strHash.charAt(0)) {
						case '[':{
							tiItemHash.setText(tiItemHash.getText() + " : []");
							tiItemHash.setData(ITEM_TYPE_DATA, SEQUENCE_HOLDER);
							int lastIndex = searchCorrespondingIndex('[', ']', strHash);
							generateProperties(strHash.substring(0, lastIndex + 1), tiItemHash);
							strHash = strHash.substring(lastIndex + 1);
							break;
						}
						case '{':{
							tiItemHash.setText(tiItemHash.getText() + " : {}");
							int search = -99;
							int lastIndex = 0;
							while(search > 0 || search == -99){
								if(search == -99) search = 0;
								if((lastIndex = strHash.indexOf("{", lastIndex)) != -1) search++;
								if((lastIndex = strHash.indexOf("}", lastIndex)) != -1) search--;
							}
							generateProperties(strHash.substring(0, lastIndex + 1), tiHash);
							strHash = strHash.substring(0, lastIndex + 1);
							break;
						}
						default : {
							tiItemHash.setText(tiItemHash.getText() + " : " + strHash.substring(0,indexOfComa != -1 ? indexOfComa : indexOfColon));
							strHash = strHash.substring((indexOfComa != -1 ? indexOfComa : indexOfColon) + 1);
							break;
						}
					}
					indexOfComa = strHash.indexOf(",");
					indexOfColon = strHash.indexOf(":");
				}
				
				break;
			}
			default : {
				TreeItem ti = new TreeItem(parent, SWT.NONE);
				if(itemImage == null) 
					itemImage = createImage("icons/item.gif");
				ti.setImage(itemImage);
				ti.setText(str);
				break;
			}
		}
	}
	private int searchCorrespondingIndex(char startC, char endC, String str) {
		int search = 0;
		int i = 0;
		for(i = 0; i < str.length(); i++){
			
			if(str.charAt(i) == startC)
				search++;
			else if(str.charAt(i) == endC)
				search--;
			
			if(search <= 0)
				break;
		}
		
		return i;
	}
	
	private void createSequenceTreeItem(TreeItem parent, String text) {
		TreeItem tiSequence = new TreeItem(parent, SWT.NONE);
		if(itemImage == null) 
			itemImage = createImage("icons/item.gif");
		tiSequence.setImage(itemImage);
		tiSequence.setText(text);
		subTreeChanged(tiSequence);
	}
	
	/**
	 * Generates a text representing the values of this subtree 
	 * and replaces it on the editor on its corresponding index 
	 * @param treeItem. The TreeItem that its subtree has changed.
	 */
	private void subTreeChanged(TreeItem treeItem) {
		TreeItem parent = treeItem.getParentItem();
		while(parent != null){
			if(parent.getData(ITEM_TYPE_DATA) != null && parent.getData(ITEM_TYPE_DATA).equals(ASSING_ITEM))
				break;
			parent = parent.getParentItem();
		}
			
		try {
			editor.getDocument().replace((Integer)parent.getData(ASSIGN_OFFSET),(Integer)parent.getData(ASSIGN_LENGTH) + 1,generateSubTreeString(parent));
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Generates the equivalent freeMarker expression from the subtree.
	 * @param ti The TreeItem root of the subtree.
	 * @return the equivalent freeMarker expression.
	 */
	private String generateSubTreeString(TreeItem ti) {
		String freemarkerExpression = "";
		
		if(ti.getData(ITEM_TYPE_DATA) != null && ti.getData(ITEM_TYPE_DATA).equals(ASSING_ITEM)){
			freemarkerExpression = "<#assign " + ti.getText() + " = ";
			for(TreeItem child : ti.getItems()){
				freemarkerExpression += generateSubTreeString(child);
			}
			freemarkerExpression += ">";
		}else if(ti.getData(ITEM_TYPE_DATA) != null && ti.getData(ITEM_TYPE_DATA).equals(SEQUENCE_ITEM)){
			freemarkerExpression = "[";
			boolean primer = true;
			for(TreeItem child : ti.getItems()){
				if(primer)
					primer = false;
				else
					freemarkerExpression += ",\n";
				freemarkerExpression += generateSubTreeString(child);
			}
			freemarkerExpression += "]";
		}else if(ti.getData(ITEM_TYPE_DATA) != null && ti.getData(ITEM_TYPE_DATA).equals(SEQUENCE_HOLDER)){
			freemarkerExpression = ti.getText().substring(0,ti.getText().length() > 1 ? ti.getText().length() - 2 : 0);
			boolean primer = true;
			for(TreeItem child : ti.getItems()){
				if(primer)
					primer = false;
				else
					freemarkerExpression += ",\n";
				freemarkerExpression += generateSubTreeString(child);
			}
		}else if(ti.getData(ITEM_TYPE_DATA) != null && ti.getData(ITEM_TYPE_DATA).equals(HASH_ITEM)){
			freemarkerExpression = "{";
			boolean primer = true;
			for(TreeItem child : ti.getItems()){
				if(primer)
					primer = false;
				else
					freemarkerExpression += ",";
				freemarkerExpression += generateSubTreeString(child);
			}
			freemarkerExpression += "}";
		}else
			freemarkerExpression += ti.getText();
		
		return freemarkerExpression;
	}
	
	private Image createImage(String entry) {
		ImageDescriptor descriptor = Activator.getImageDescriptor(entry);
		return descriptor.createImage();
	}
	
}
