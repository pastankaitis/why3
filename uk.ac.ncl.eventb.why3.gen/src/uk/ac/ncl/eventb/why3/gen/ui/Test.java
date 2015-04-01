package uk.ac.ncl.eventb.why3.gen.ui;

import org.eclipse.sapphire.ui.forms.FormComponentPart;
import org.eclipse.sapphire.ui.forms.PropertyEditorPart;
import org.eclipse.sapphire.ui.forms.swt.PropertyEditorPresentation;
import org.eclipse.sapphire.ui.forms.swt.PropertyEditorPresentationFactory;
import org.eclipse.sapphire.ui.forms.swt.SwtPresentation;
import org.eclipse.sapphire.ui.forms.swt.TextFieldPropertyEditorPresentation;
import org.eclipse.swt.widgets.Composite;

public class Test extends TextFieldPropertyEditorPresentation {

	public Test(FormComponentPart part, SwtPresentation parent,
			Composite composite) {
		super(part, parent, composite);
	}

    public static final class Factory extends PropertyEditorPresentationFactory
    {
        @Override
        public PropertyEditorPresentation create( final PropertyEditorPart part, final SwtPresentation parent, final Composite composite )
        {
        	return new Test( part, parent, composite );
        }
    }
}
