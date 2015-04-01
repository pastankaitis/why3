package uk.ac.ncl.eventb.why3.gen.translation;

import org.eventb.core.ast.Expression;
import org.eventb.core.ast.Predicate;

import uk.ac.ncl.eventb.why3.translator.TranslationException;

public interface IGenericTranslatorPort {
	public String mapOperator(int tag, String defaultValue) throws TranslationException;
	public boolean translate(Predicate predicate) throws TranslationException;
	public boolean translate(Expression predicate) throws TranslationException;
}
