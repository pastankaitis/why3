package uk.ac.ncl.eventb.why3.gen.rules;
import org.eclipse.sapphire.modeling.el.Function;
import org.eclipse.sapphire.modeling.el.FunctionContext;
import org.eclipse.sapphire.modeling.el.FunctionResult;
import org.eclipse.sapphire.ui.PartFunctionContext;

import uk.ac.ncl.eventb.why3.gen.GenException;
import uk.ac.ncl.eventb.why3.gen.translation.CatalogTranslator;
import uk.ac.ncl.eventb.why3.gen.ui.TranslationCatalog;
import uk.ac.ncl.eventb.why3.gen.ui.TranslationRule;
import uk.ac.ncl.eventb.why3.translator.TranslationException;

public final class CatalogTranslationFunction extends Function {
    @Override
    public String name() {
        return "rule:Translate";
    }

    @Override
    public FunctionResult evaluate(final FunctionContext context) {
        return new FunctionResult(this, context) {
        	private TranslationCatalog catalog;
        	private CatalogTranslator translator;
        	
            @Override
			protected void init()
            {
            	translator = new CatalogTranslator();
            	catalog = (TranslationCatalog) ((PartFunctionContext) context()).element();
            }
            
            @Override
            protected Object evaluate() {
            	try {
					if (catalog.validation().ok()) {
						translator.reset();
						translator.translate(catalog);
						return translator.getResult();
					} else {
						return null;
					}
				} catch (TranslationException | GenException e) {
					e.printStackTrace();
					return null;
				}
            }
        };
    }

}