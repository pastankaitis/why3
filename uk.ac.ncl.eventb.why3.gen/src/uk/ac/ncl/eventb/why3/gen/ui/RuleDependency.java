package uk.ac.ncl.eventb.why3.gen.ui;

import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.ElementReference;
import org.eclipse.sapphire.ElementType;
import org.eclipse.sapphire.ReferenceValue;
import org.eclipse.sapphire.Unique;
import org.eclipse.sapphire.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.Reference;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;

public interface RuleDependency extends Element {
	ElementType TYPE = new ElementType(RuleDependency.class);
	
    @Reference( target = TranslationRule.class )
    @ElementReference( list = "/Rules", key = "Source" )
    @Required
    @Unique
    @XmlBinding( path = "ruledep")
    ValueProperty PROP_RULE_DEPENDENCY = new ValueProperty( TYPE, "RuleDependency" );
    ReferenceValue<String,TranslationRule> getRuleDependency();
    void setRuleDependency( String source );   
}
