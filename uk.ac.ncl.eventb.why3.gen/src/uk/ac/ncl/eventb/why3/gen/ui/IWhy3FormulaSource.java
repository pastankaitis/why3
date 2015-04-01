package uk.ac.ncl.eventb.why3.gen.ui;


public interface IWhy3FormulaSource {
	public enum SYMBOL_CLASS {
		BUILTIN_TYPE,
		CONTRIBUTED_TYPE,
		BUILTIN_CONSTANT,
		CONTRIBUTED_CONSTANT,
		RULE_CONSTANT,
		LITERAL,
		COMMENT,
		KEYWORD,
		OTHER
	}	
		
	
	public ITranslationElement getSourceElement();
	public void markError(int from, int length, String message);
	public void markWarning(int from, int length, String message);
	public void markInfo(int from, int length, String message);
	public void colour(int from, int length, SYMBOL_CLASS symbol_class);
	public void reportStart();
	public void reportEnd();
	public void noErrors();
}
