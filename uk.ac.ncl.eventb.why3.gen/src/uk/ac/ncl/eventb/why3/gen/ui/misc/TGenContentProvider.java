package uk.ac.ncl.eventb.why3.gen.ui.misc;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.core.resources.IFile;

public class TGenContentProvider implements ITreeContentProvider  {
	private static final Object[] NO_OBJECT = new Object[0];
	
	@Override
	public Object[] getElements(Object element) {
		return getChildren(element);
	}

	@Override
	public Object[] getChildren(Object element) {
		try {
			List<Object> children = new ArrayList<Object>();
			if (element instanceof IProject) {
				IProject project = (IProject) element;
				for(IResource r: project.members()) {
					if ("tgen".equals(r.getFileExtension()))
						children.add(new RawTGenFile((IFile) r));
				}
			}
			return children.toArray();
		} catch (CoreException e) {
			e.printStackTrace();
			return NO_OBJECT;
		}
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		try {
			if (element instanceof IProject) {
				IProject project = (IProject) element;
				if (!project.isOpen())
					return false;
				return project.members().length > 0;
			}
			return false;
		} catch (CoreException e) {
			e.printStackTrace();
			return false;
		}
	}


	@Override
	public void dispose() {
		// ignore
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// ignore
	}


}
