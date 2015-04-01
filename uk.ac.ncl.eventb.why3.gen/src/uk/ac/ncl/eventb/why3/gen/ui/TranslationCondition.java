package uk.ac.ncl.eventb.why3.gen.ui;

import org.eclipse.sapphire.ElementType;
import org.eclipse.sapphire.Unique;
import org.eclipse.sapphire.Value;
import org.eclipse.sapphire.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.DefaultValue;
import org.eclipse.sapphire.modeling.annotations.LongString;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;

public interface TranslationCondition extends ITranslationElement {
	ElementType TYPE = new ElementType(TranslationCondition.class);

	@XmlBinding(path = "id")
	@Required
	@Unique
	ValueProperty PROP_ID = new ValueProperty(TYPE, "Id");
	Value<String> getId();
	void setId(String value);	
	
	@XmlBinding(path = "kind")
	@Type( base = ConditionKind.class )
    @DefaultValue( text = "axiom" )
	@Required
	ValueProperty PROP_KIND = new ValueProperty(TYPE, "Kind");
	Value<ConditionKind> getKind();
	void setKind(ConditionKind value);	
	
	@Type( base = Integer.class )    
	ValueProperty PROP_STATUS = new ValueProperty(TYPE, "Status");
	Value<Integer> getStatus();
	void setStatus( Integer value );
	
	@XmlBinding(path = "body")
	@LongString
	@Required
	ValueProperty PROP_BODY = new ValueProperty(TYPE, "Body");
	@Override
	Value<String> getBody();
	@Override
	void setBody(String body);
}
