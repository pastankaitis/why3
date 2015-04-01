package uk.ac.ncl.eventb.why3.gen;

import java.util.BitSet;
import java.util.List;
import java.util.Stack;

import uk.ac.ncl.eventb.why3.gen.opregistry.OperatorRegistry;

public class TriggerContext {
	private long fallBackPlainbitset = 0;
	private long plainbitset = 0;
	private BitSet set;
	private Stack<Integer> stack;
	
	public TriggerContext() {
		set = new BitSet(64 + 64*64);
		stack = new Stack<Integer>();
	}
	
	public void enter(int tag) {
		stack.push(tag);
		plainbitset |= 1 << OperatorRegistry.tagToCode(tag);
	}

	public void leave(int tag) {
		assert(!stack.isEmpty() && tag == stack.peek());
		
		int code = OperatorRegistry.tagToCode(stack.peek());
		if (stack.size() >= 2) {
			int code2 = OperatorRegistry.tagToCode(stack.get(stack.size() - 2));
			set.set((code << 6) | code2);
		}
		
		stack.pop();
	}

	public void useFallBackOperator(int code) {
		fallBackPlainbitset |= 1 << code;
	}
	
	public boolean dependsOn(int code) {
		return (plainbitset & code) == code;
	}

	public boolean dependsOnFallBackOperator(int code) {
		return (fallBackPlainbitset & code) == code;
	}
	
	public boolean match(List<Long> triggerSet) {
		assert(triggerSet.size() > 0);
		long plainset = triggerSet.get(0);
		if ((plainset & plainbitset) != plainset)
			return false;
		
		for(int i = 1; i < triggerSet.size(); i++) {
			long code = triggerSet.get(i);
			if (!set.get((int) code))
				return false;
		}
		
		return true;
	}
}
