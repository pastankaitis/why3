package uk.ac.ncl.eventb.why3.gen.pattern;

import org.eclipse.core.runtime.CoreException;
import org.eventb.core.ast.Expression;
import org.eventb.core.ast.FormulaFactory;
import org.eventb.core.ast.IParseResult;
import org.eventb.core.ast.Predicate;
import org.eventb.core.ast.Type;

public class PatternUtils {
	public static Type parseType(String text) throws CoreException {
		
		IParseResult parseResult = FormulaFactory.getDefault().parseType(text);

		if (parseResult.hasProblem()) {
			
			// take location from
			// parseResult.getProblems().get(0).getSourceLocation();
//			createProblemMarker(formulaElement, EventBAttributes.PREDICATE_ATTRIBUTE, 
//					new GameProblem(IMarker.SEVERITY_ERROR, 
//						"Syntax error in predicate expression"));
			return null;
		}

		Type type = parseResult.getParsedType();

		return type;
	}	
	
	public static Expression parseExpression(IEventBFormulaSource source) throws CoreException {
		String formula = source.getSource();
		IParseResult parseResult = FormulaFactory.getDefault().parseExpression(formula, null);

		if (parseResult.hasProblem()) {
			
			// take location from
			// parseResult.getProblems().get(0).getSourceLocation();
//			createProblemMarker(formulaElement, EventBAttributes.PREDICATE_ATTRIBUTE, 
//					new GameProblem(IMarker.SEVERITY_ERROR, 
//						"Syntax error in predicate expression"));
			return null;
		}

		Expression predicate = parseResult.getParsedExpression();

		return predicate;
	}
	
	public static Predicate parsePredicate(IEventBFormulaSource source) throws CoreException {
		String formula = source.getSource();
		IParseResult parseResult = FormulaFactory.getDefault().parsePredicate(formula, null);

		if (parseResult.hasProblem()) {
			
			// take location from
			// parseResult.getProblems().get(0).getSourceLocation();
//			createProblemMarker(formulaElement, EventBAttributes.PREDICATE_ATTRIBUTE, 
//					new GameProblem(IMarker.SEVERITY_ERROR, 
//						"Syntax error in predicate expression"));
			return null;
		}

		Predicate predicate = parseResult.getParsedPredicate();

		return predicate;
	}

	public static boolean isExpression(IEventBFormulaSource source) throws CoreException {
		String formula = source.getSource();
		IParseResult parseResult = FormulaFactory.getDefault().parseExpression(formula, null);

		if (parseResult.hasProblem())
			return false;
		else
			return true;
	}
	
}
