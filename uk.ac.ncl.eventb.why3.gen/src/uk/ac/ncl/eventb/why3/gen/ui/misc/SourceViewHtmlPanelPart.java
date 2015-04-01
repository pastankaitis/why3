package uk.ac.ncl.eventb.why3.gen.ui.misc;

import org.eclipse.sapphire.ui.forms.FormComponentPart;
import org.eclipse.sapphire.ui.forms.swt.FormComponentPresentation;
import org.eclipse.sapphire.ui.forms.swt.SwtPresentation;
import org.eclipse.swt.widgets.Composite;


public final class SourceViewHtmlPanelPart extends FormComponentPart
{

    @Override
    public FormComponentPresentation createPresentation( final SwtPresentation parent, final Composite composite )
    {
        return new SourceViewHtmlPanelPresentation( this, parent, composite );
    }

}
