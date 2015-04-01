package uk.ac.ncl.eventb.why3.gen.ui.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.OpenWithMenu;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionConstants;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonMenuConstants;
import org.eclipse.ui.part.FileEditorInput;
import org.eventb.internal.ui.EventBImage;
import org.eventb.internal.ui.UIUtils;
import org.eventb.internal.ui.YesToAllMessageDialog;
import org.eventb.ui.EventBUIPlugin;
import org.eventb.ui.IEventBSharedImages;
import org.rodinp.core.IRodinElement;
import org.rodinp.core.IRodinFile;
import org.rodinp.core.IRodinProject;
import org.rodinp.core.RodinDBException;

import fr.systerel.internal.explorer.navigator.ExplorerUtils;
import fr.systerel.internal.explorer.navigator.actionProviders.ActionCollection;
import uk.ac.ncl.eventb.why3.gen.GenPlugin;

public class TGenActionProvider extends CommonActionProvider {

	public static String GROUP_MODELLING = "modelling";
	
    StructuredViewer viewer;
    
    ICommonActionExtensionSite site;

    @Override
    public void init(ICommonActionExtensionSite aSite) {
        super.init(aSite);
        site = aSite;
		viewer = aSite.getStructuredViewer();
	}

    /**
     * Builds an Open With menu.
     * 
     * @return the built menu
     */
	MenuManager buildOpenWithMenu() {
		MenuManager menu = new MenuManager("Open With",
				ICommonMenuConstants.GROUP_OPEN_WITH);
		ISelection selection = site.getStructuredViewer().getSelection();
		Object obj = ((IStructuredSelection) selection).getFirstElement();
		
		menu.add(new OpenWithMenu(EventBUIPlugin.getActivePage(), ((RawTGenFile) obj).getFile()));
		return menu;
	}	
	
    @Override
    public void fillActionBars(IActionBars actionBars) {
    	actionBars.clearGlobalActionHandlers();
        actionBars.setGlobalActionHandler(ICommonActionConstants.OPEN, getOpenAction(site));
        actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), getDeleteAction(site));
    	actionBars.updateActionBars();
    }
	
    private static Action getOpenAction(final ICommonActionExtensionSite site) {
		Action doubleClickAction = new Action("Open") {
			@Override
			public void run() {
				ISelection selection = site.getStructuredViewer().getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();

				if (obj instanceof RawTGenFile) {
					try {
						RawTGenFile tgen = (RawTGenFile) obj;
						IEditorPart editor = EventBUIPlugin.getActivePage().openEditor(new FileEditorInput(tgen.getFile()), GenPlugin.EDITOR_ID);
						if (editor == null)
							return;

						final ISelectionProvider sp = editor.getSite().getSelectionProvider();
						if (sp == null)
							return;

						sp.setSelection(new StructuredSelection(obj));
					} catch (PartInitException e) {
						e.printStackTrace();
						return;
					}
				}
			}
		};

		return doubleClickAction;
		
	}
    
	private static Action getDeleteAction(final ICommonActionExtensionSite site) {
		Action deleteAction = new Action() {
			@Override
			public void run() {
				if (!(site.getStructuredViewer().getSelection().isEmpty())) {
		
					// Putting the selection into a set which does not contains any pair
					// of parent and child
					Collection<RawTGenFile> set = new ArrayList<RawTGenFile>();
		
					IStructuredSelection ssel = (IStructuredSelection) site.getStructuredViewer().getSelection();
		
					for (Iterator<?> it = ssel.iterator(); it.hasNext();) {
						final Object obj = it.next();
						if (obj instanceof RawTGenFile)
							set.add((RawTGenFile) obj);
					}
		
					int answer = YesToAllMessageDialog.YES;
					for (RawTGenFile element : set) {
							if (answer != YesToAllMessageDialog.YES_TO_ALL) {
								answer = YesToAllMessageDialog.openYesNoToAllQuestion(
										site.getViewSite().getShell(),
										"Confirm File Delete",
										"Are you sure you want to delete file '"
												+ element.getFile().getName() + "' ?");
							}
							if (answer == YesToAllMessageDialog.NO_TO_ALL)
								break;
		
							if (answer != YesToAllMessageDialog.NO) {
								try {
									closeOpenedEditor(element.getFile());
									element.getFile().delete(true, new NullProgressMonitor());
								} catch (PartInitException e) {
									MessageDialog.openError(null, "Error", "Could not delete file");
								} catch (CoreException e) {
									MessageDialog.openError(null, "Error", "Could not delete file");
								}
							}
						}
		
				}
			}
		};
		deleteAction.setText("&Delete");
		deleteAction.setToolTipText("Delete these elements");
		deleteAction.setImageDescriptor(EventBImage.getImageDescriptor(IEventBSharedImages.IMG_DELETE_PATH));
		
		return deleteAction;
	}    
  
	private static void closeOpenedEditor(IFile file) throws PartInitException {
		IEditorReference[] editorReferences = EventBUIPlugin.getActivePage().getEditorReferences();
		for (int j = 0; j < editorReferences.length; j++) {
			IFile inputFile = (IFile) editorReferences[j].getEditorInput()
					.getAdapter(IFile.class);

			if (file.equals(inputFile)) {
				IEditorPart editor = editorReferences[j].getEditor(true);
				IWorkbenchPage page = EventBUIPlugin.getActivePage();
				page.closeEditor(editor, false);
			}
		}
	}	
	
    @Override
	public void fillContextMenu(IMenuManager menu) {
		// super.fillContextMenu(menu);
    	menu.removeAll();
		menu.add(getOpenAction(site));
		menu.add(getDeleteAction(site));
    }
			
}
