package uk.ac.ncl.eventb.why3.gen.ui.misc;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

public class TGenLabelProvider implements ILabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {
		// ignore
	}

	@Override
	public void dispose() {
		// ignore
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// ignore	
	}

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof RawTGenFile) {
			RawTGenFile rawfile = (RawTGenFile) element;
			return rawfile.getFile().getName();
		}
		return "?";
	}

}
