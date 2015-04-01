package uk.ac.ncl.eventb.why3.gen;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.sapphire.modeling.ResourceStoreException;
import org.eclipse.sapphire.modeling.xml.RootXmlResource;
import org.eclipse.sapphire.modeling.xml.XmlResourceStore;

import uk.ac.ncl.eventb.why3.gen.ui.TranslationCatalog;

public class TGenRegistry implements IResourceChangeListener {
	private Map<IFile, TranslationCatalog> fileToCatalog;
	private Map<String, TranslationCatalog> nameToCatalog;
	
	public TGenRegistry() {
		fileToCatalog = new HashMap<IFile, TranslationCatalog>();
		nameToCatalog = new HashMap<String, TranslationCatalog>();
		scan();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}
	
	public TranslationCatalog resolve(IFile file) {
		TranslationCatalog ct = fileToCatalog.get(file);
		if (ct == null && file.exists() && "tgen".equals(file.getFileExtension())) {
			refresh(file);
			ct = fileToCatalog.get(file);
		}
		
		return ct;
	}
	
	private void scan() {
		for(IProject p: org.eclipse.core.resources.ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			try {
				if (p.isOpen())
					openProject(p);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void refresh(IFile file) {
		try {
			XmlResourceStore store = new XmlResourceStore(file.getLocation().toFile());
			RootXmlResource xmlres = new RootXmlResource( store );
			TranslationCatalog catalog = TranslationCatalog.TYPE.instantiate(xmlres);
			if (catalog != null) {
				System.out.println("Processed catalog " + file.getName());
				fileToCatalog.put(file, catalog);
				nameToCatalog.put(file.getName(), catalog);
			}
		} catch (ResourceStoreException e) {
			e.printStackTrace();
		}
	}
	
	private void forget(IFile file) {
		TranslationCatalog catalog = fileToCatalog.get(file);
		if (catalog != null) {
			fileToCatalog.remove(file);
			nameToCatalog.remove(file.getName());
			System.out.println("Removed catalog " + file.getName());
		}
	}
	
	private void openProject(IProject project) throws CoreException {
		for(IResource r: project.members()) {
			if ("tgen".equals(r.getFileExtension())) {
				refresh((IFile) r);
			}
		}
	}

	private void closeProject(IProject project) throws CoreException {
		for(IResource r: project.members()) {
			if ("tgen".equals(r.getFileExtension())) {
				forget((IFile) r);
			}
		}		
	}	
	
	
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		for(IFile f: fileToCatalog.keySet()) {
			IResourceDelta rd = event.getDelta().findMember(f.getFullPath());
			
			if (rd != null && rd.getResource() instanceof IFile && rd.getResource().getFileExtension().equals("tgen")) {
				if (rd.getKind() == IResourceDelta.REMOVED) {
					forget((IFile) rd.getResource());
				} else if (rd.getKind() == IResourceDelta.CHANGED || rd.getKind() == IResourceDelta.REPLACED) {
					refresh((IFile) rd.getResource());
				}
			}
		}
		
		// project open
		try {
	        event.getDelta().accept(new IResourceDeltaVisitor() {
	            @Override
				public boolean visit(final IResourceDelta delta) throws CoreException {
	                IResource resource = delta.getResource();
	                if (((resource.getType() & IResource.PROJECT) != 0)
	                        && resource.getProject().isOpen()
	                        && delta.getKind() == IResourceDelta.CHANGED
	                        && (((delta.getFlags() & IResourceDelta.OPEN) != 0) || ((delta.getFlags() & IResourceDelta.ADDED) != 0))) {

	                    IProject project = (IProject)resource;
	                    openProject(project);
	                }
	                return true;
	            }
	        });
	    } catch (CoreException e) {
	        e.printStackTrace();
	    }		
		
		// project close
		try {
	        event.getDelta().accept(new IResourceDeltaVisitor() {
	            @Override
				public boolean visit(final IResourceDelta delta) throws CoreException {
	                IResource resource = delta.getResource();
	                if (((resource.getType() & IResource.PROJECT) != 0)
	                        && resource.getProject().isOpen()
	                        && delta.getKind() == IResourceDelta.CHANGED
	                        && ((delta.getFlags() & IResourceDelta.REMOVED) != 0)) {

	                    IProject project = (IProject)resource;
	                    closeProject(project);
	                }
	                return true;
	            }
	        });
	    } catch (CoreException e) {
	        e.printStackTrace();
	    }			
	}
}
