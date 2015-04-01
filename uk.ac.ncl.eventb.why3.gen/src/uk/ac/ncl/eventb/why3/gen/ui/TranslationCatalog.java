package uk.ac.ncl.eventb.why3.gen.ui;

import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.ElementList;
import org.eclipse.sapphire.ElementType;
import org.eclipse.sapphire.ListProperty;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;
import org.eclipse.sapphire.modeling.xml.annotations.XmlListBinding;

@XmlBinding( path = "translationcatalog" )
public interface TranslationCatalog extends Element
{
    ElementType TYPE = new ElementType( TranslationCatalog.class );

    @Type( base = TranslationLink.class )
    @XmlListBinding( mappings = { @XmlListBinding.Mapping( element = "import", type = TranslationLink.class ) } )
    ListProperty PROP_IMPORTS = new ListProperty( TYPE, "Imports" );
    ElementList<TranslationLink> getImports();      
      
    @Type( base = TranslationRule.class )
    @XmlListBinding( mappings = { @XmlListBinding.Mapping( element = "rule", type = TranslationRule.class ) } )
    ListProperty PROP_RULES = new ListProperty( TYPE, "Rules" );
    ElementList<TranslationRule> getRules();  

    @Type( base = TranslationPattern.class )
    @XmlListBinding( mappings = { @XmlListBinding.Mapping( element = "pattern", type = TranslationPattern.class ) } )
    ListProperty PROP_PATTERNS = new ListProperty( TYPE, "Patterns" );
    ElementList<TranslationPattern> getPatterns();   

    @Type( base = TranslationCondition.class )
    @XmlListBinding( mappings = { @XmlListBinding.Mapping( element = "condition", type = TranslationCondition.class ) } )
    ListProperty PROP_CONDITIONS = new ListProperty( TYPE, "Conditions" );
    ElementList<TranslationCondition> getConditions();  	
    
    

}
