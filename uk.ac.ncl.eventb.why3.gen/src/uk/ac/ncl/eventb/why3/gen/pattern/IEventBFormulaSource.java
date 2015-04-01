package uk.ac.ncl.eventb.why3.gen.pattern;
import org.eventb.core.ast.ITypeEnvironment;
public interface IEventBFormulaSource {
	public String getSource();
	public void markError(int from, int length, String message);
	public void markWarning(int from, int length, String message);
	public void markInfo(int from, int length, String message);
	public void reportStart();
	public void reportEnd();
	public void noErrors();
	public ITypeEnvironment getITypeEnvironment();
}
