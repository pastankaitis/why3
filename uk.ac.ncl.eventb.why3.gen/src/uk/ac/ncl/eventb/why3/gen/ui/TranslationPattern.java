package uk.ac.ncl.eventb.why3.gen.ui;

import org.eclipse.sapphire.ElementList;
import org.eclipse.sapphire.ElementType;
import org.eclipse.sapphire.ListProperty;
import org.eclipse.sapphire.Unique;
import org.eclipse.sapphire.Value;
import org.eclipse.sapphire.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.LongString;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;
import org.eclipse.sapphire.modeling.xml.annotations.XmlListBinding;

public interface TranslationPattern extends ITranslationElement {
	ElementType TYPE = new ElementType(TranslationPattern.class);
	@XmlBinding(path = "id")
	@Required
	@Unique
	ValueProperty PROP_ID = new ValueProperty(TYPE, "Id");
	Value<String> getId();
	void setId(String value);

    @Type( base = PatternParameter.class )
    @XmlListBinding( mappings = { @XmlListBinding.Mapping( element = "type", type = PatternParameter.class ) } )
    ListProperty PROP_TYPES = new ListProperty( TYPE, "Types" );
    ElementList<PatternParameter> getTypes();  		
	
	@XmlBinding(path = "pattern")
	@Required
	@LongString
	ValueProperty PROP_PATTERN = new ValueProperty(TYPE, "Pattern");
	Value<String> getPattern();
	void setPattern(String source);
	
    @Type( base = TranslationCondition.class )
    @XmlListBinding( mappings = { @XmlListBinding.Mapping( element = "conditions", type = TranslationCondition.class ) } )
    ListProperty PROP_CONDITIONS = new ListProperty( TYPE, "Conditions" );
    ElementList<TranslationCondition> getConditions(); 	
}
