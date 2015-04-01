package uk.ac.ncl.eventb.why3.gen.ui;

import static org.eclipse.sapphire.modeling.util.MiscUtil.EMPTY_STRING;
import static org.eclipse.sapphire.modeling.util.MiscUtil.equal;

import org.eclipse.sapphire.Value;
import org.eclipse.sapphire.services.ValueNormalizationService;
import org.eclipse.sapphire.ui.DelayedTasksExecutor;
import org.eclipse.sapphire.ui.forms.swt.AbstractBinding;
import org.eclipse.sapphire.ui.forms.swt.PropertyEditorPresentation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Control;

public class StyledTextFieldBinding extends AbstractBinding
{
    private StyledText text;
    private String textContent;
    private ValueNormalizationService valueNormalizationService;
    private DelayedTasksExecutor.Task onTextContentModifyTask;
    
    public StyledTextFieldBinding( final PropertyEditorPresentation propertyEditorPresentation,
                             final StyledText text )
    {
        super( propertyEditorPresentation, text );
    }

    @Override
    protected void initialize( final PropertyEditorPresentation propertyEditorPresentation,
                               final Control control )
    {
        super.initialize( propertyEditorPresentation, control );

        this.onTextContentModifyTask = new DelayedTasksExecutor.Task()
        {
            @Override
			public int getPriority()
            {
                return 100;
            }
            
            @Override
			public void run()
            {
                updateModel();
                updateTargetAttributes();
            }
        };
        
        this.text = (StyledText) control;
        
        this.text.addModifyListener
        (
            new ModifyListener()
            {
                @Override
				public void modifyText( final ModifyEvent event )
                {
                	if (!text.isDisposed())
                		updateTextContent( StyledTextFieldBinding.this.text.getText() );
                }
            }
        );
        
        this.valueNormalizationService = property().service( ValueNormalizationService.class );
    }
    
    @Override
    public Value<?> property()
    {
        return (Value<?>) super.property();
    }
    
    protected void updateTextContent( final String textContent )
    {
        this.textContent = textContent;
        DelayedTasksExecutor.schedule( this.onTextContentModifyTask );
    }

    @Override
    protected void doUpdateModel()
    {
        if( ! this.text.isDisposed() && ( this.text.getStyle() & SWT.READ_ONLY ) == 0 ) 
        {
            property().write( this.textContent, true );
        }
    }
    
    @Override
    protected void doUpdateTarget()
    {
        final String oldValue = this.valueNormalizationService.normalize( this.text.getText() );
        final String newValue = this.valueNormalizationService.normalize( ( (Value<?>) property() ).text( false ) );
        
        if( ! equal( oldValue, newValue ) )
        {
            this.text.setText( newValue == null ? EMPTY_STRING : newValue );
        }
    }
    
}