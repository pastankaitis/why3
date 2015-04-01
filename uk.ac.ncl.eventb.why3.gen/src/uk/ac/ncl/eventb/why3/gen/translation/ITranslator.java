package uk.ac.ncl.eventb.why3.gen.translation;

import org.eventb.core.ast.Expression;
import org.eventb.core.ast.Predicate;
import org.eventb.core.ast.Type;

import uk.ac.ncl.eventb.why3.translator.TranslationException;

public interface ITranslator {
	 public void translate(Predicate predicate) throws TranslationException;
	 public void translate(Expression expression) throws TranslationException;
	 public void translate(Type type) throws TranslationException;
}
