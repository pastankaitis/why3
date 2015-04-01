package uk.ac.ncl.eventb.why3.gen.ui.misc;

import static org.eclipse.sapphire.ui.forms.swt.GridLayoutUtil.gdfill;
import static org.eclipse.sapphire.ui.forms.swt.GridLayoutUtil.gdhindent;
import static org.eclipse.sapphire.ui.forms.swt.GridLayoutUtil.gdhspan;
import static org.eclipse.sapphire.ui.forms.swt.GridLayoutUtil.gdwhint;

import org.eclipse.sapphire.LocalizableText;
import org.eclipse.sapphire.Text;
import org.eclipse.sapphire.ui.forms.FormComponentPart;
import org.eclipse.sapphire.ui.forms.swt.FormComponentPresentation;
import org.eclipse.sapphire.ui.forms.swt.SwtPresentation;
//import org.eclipse.sapphire.ui.forms.swt.internal.text.SapphireFormText;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class SourceViewHtmlPanelPresentation extends FormComponentPresentation
{
    @Text( "Could not initialize embedded browser." )
    private static LocalizableText couldNotInitializeBrowserMessage;

    static 
    {
        LocalizableText.init( SourceViewHtmlPanelPresentation.class );
    }

    public SourceViewHtmlPanelPresentation( final FormComponentPart part, final SwtPresentation parent, final Composite composite )
    {
        super( part, parent, composite );
    }

    @Override
    public SourceViewHtmlPanelPart part()
    {
        return (SourceViewHtmlPanelPart) super.part();
    }
    
    @Override
    public void render()
    {
        GridData gd = gdhindent( gdwhint( gdhspan( gdfill(), 2 ), 100 ), 9 );
        gd.minimumHeight = 400;
        
        try
        {
            final Browser browser = new Browser( composite(), SWT.NONE );
            browser.setLayoutData( gd );
            browser.setText("hello");
            register( browser );
        }
        catch( SWTError e )
        {
            e.printStackTrace();
        }
    }

}
