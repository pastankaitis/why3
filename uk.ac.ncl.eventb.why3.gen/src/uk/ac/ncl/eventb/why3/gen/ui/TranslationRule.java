package uk.ac.ncl.eventb.why3.gen.ui;

import org.eclipse.sapphire.ElementList;
import org.eclipse.sapphire.ElementType;
import org.eclipse.sapphire.ListProperty;
import org.eclipse.sapphire.Unique;
import org.eclipse.sapphire.Value;
import org.eclipse.sapphire.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.annotations.Service;
import org.eclipse.sapphire.modeling.annotations.Services;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;
import org.eclipse.sapphire.modeling.xml.annotations.XmlListBinding;

public interface TranslationRule extends ITranslationElement {
	ElementType TYPE = new ElementType(TranslationRule.class);

	@XmlBinding(path = "source")
	@Services( {
		@Service( impl = RuleValidationService.class ),	 
		@Service( impl = EventBOperators.class)
	} )
	@Required
	@Unique
	ValueProperty PROP_SOURCE = new ValueProperty(TYPE, "Source");
	Value<String> getSource();
	void setSource(String source);
	
	@XmlBinding(path = "target")
	@Service( impl = RuleValidationService.class )	 
	@Required
	@Unique
	ValueProperty PROP_TARGET = new ValueProperty(TYPE, "Target");
	Value<String> getTarget();
	void setTarget(String source);

    @Type( base = TranslationCondition.class )
    @XmlListBinding( mappings = { @XmlListBinding.Mapping( element = "condition", type = TranslationCondition.class ) } )
    ListProperty PROP_CONDITIONS = new ListProperty( TYPE, "Conditions" );
    ElementList<TranslationCondition> getConditions();  	
    
    
}
