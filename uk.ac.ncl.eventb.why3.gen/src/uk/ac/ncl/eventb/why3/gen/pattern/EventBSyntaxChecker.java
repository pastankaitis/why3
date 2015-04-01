package uk.ac.ncl.eventb.why3.gen.pattern;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.sapphire.Element;
import org.eclipse.swt.widgets.Display;
import org.eventb.core.ast.Expression;
import org.eventb.core.ast.Predicate;

import uk.ac.ncl.eventb.why3.driver.Why3SyntaxCheckResult;

public class EventBSyntaxChecker implements Runnable {
	private IEventBFormulaSource source;
	private Why3SyntaxCheckResult result;
	private Timer timer;
	private TimerTask task;
	private Element sourceElement;
	private int textOffset;

	public EventBSyntaxChecker(IEventBFormulaSource source, boolean isDefinition) {
		this.source = source;
		timer = new Timer();
		task = new MyTimerTask();
	}

	public EventBSyntaxChecker(IEventBFormulaSource source, int offset) {
		this.source = source;
		timer = new Timer();
		task = new MyTimerTask();
	}


	public void check() {
		try {
			task.cancel();
			task = new MyTimerTask();
			timer.schedule(task, 2000);
		} catch (Throwable e) {
			e.printStackTrace();
			source.markError(0, -1, e.toString());
		}
	}

	@Override
	public void run() {
		try {
			if (PatternUtils.isExpression(source)) {
				Expression expression = PatternUtils.parseExpression(source);
			} else {
				Predicate predicate = PatternUtils.parsePredicate(source);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			result = new Why3SyntaxCheckResult("Tool failure:" + e.getMessage());
		}

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				source.reportStart();
				if (result.getErrorMessage() != null) {
					if (result.getErrorColStart() >= 0 && result.getErrorColEnd() >= 0) {
						source.markError(result.getErrorColStart() + textOffset, result.getErrorColEnd() - result.getErrorColStart(), result.getErrorMessage());
					} else {
						source.markError(0, -1, result.getErrorMessage());
					}
				} else {
					source.noErrors();
				}
				source.reportEnd();
			}
		});
	}

	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			new Thread(EventBSyntaxChecker.this).start();
		}

	}

}
