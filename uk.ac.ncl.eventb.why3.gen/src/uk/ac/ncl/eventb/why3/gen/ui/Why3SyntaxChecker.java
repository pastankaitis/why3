package uk.ac.ncl.eventb.why3.gen.ui;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.sapphire.Element;
import org.eclipse.swt.widgets.Display;

import uk.ac.ncl.eventb.why3.driver.Why3SyntaxCheck;
import uk.ac.ncl.eventb.why3.driver.Why3SyntaxCheckResult;
import uk.ac.ncl.eventb.why3.gen.translation.ElementTranslator;
import uk.ac.ncl.eventb.why3.translator.TheoremTranslated;

public class Why3SyntaxChecker implements Runnable {
	private static int LemmaOffsetAdjust = -TheoremTranslated.GOAL_PREFIX.length();
	private IWhy3FormulaSource source;
	private Why3SyntaxCheckResult result;
	private Timer timer;
	private TimerTask task;
	private Element sourceElement;
	private int textOffset;

	public Why3SyntaxChecker(IWhy3FormulaSource source, boolean isDefinition) {
		this.source = source;
		timer = new Timer();
		task = new MyTimerTask();
	}

	public Why3SyntaxChecker(IWhy3FormulaSource source, int offset) {
		this.source = source;
		timer = new Timer();
		task = new MyTimerTask();
	}

	public void check() {
		check(LemmaOffsetAdjust);
	}

	public void check(int offset) {
		try {
			textOffset = offset;
			task.cancel();
			sourceElement = source.getSourceElement();
			task = new MyTimerTask();
			timer.schedule(task, 1000);
		} catch (Throwable e) {
			e.printStackTrace();
			source.markError(0, -1, e.toString());
		}
	}

	@Override
	public void run() {
		try {
			if (sourceElement.validation().ok()) {
				TheoremTranslated model = ElementTranslator.fromElement(sourceElement);
				Why3SyntaxCheck tool = new Why3SyntaxCheck(model);
				result = tool.check();
			} else {
				result = Why3SyntaxCheckResult.FAILED;
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
			new Thread(Why3SyntaxChecker.this).start();
		}

	}

}
