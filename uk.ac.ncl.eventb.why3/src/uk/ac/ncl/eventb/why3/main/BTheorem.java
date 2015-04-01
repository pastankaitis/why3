package uk.ac.ncl.eventb.why3.main;

import java.util.ArrayList;
import java.util.List;

import org.eventb.core.IContextRoot;
import org.eventb.core.IMachineRoot;
import org.eventb.core.IPOSequent;
import org.eventb.core.IPOSource;
import org.eventb.core.ast.Formula;
import org.eventb.core.ast.FormulaFactory;
import org.eventb.core.ast.Predicate;
import org.eventb.core.seqprover.transformer.ISimpleSequent;
import org.eventb.core.seqprover.transformer.ITrackedPredicate;
import org.rodinp.core.IInternalElement;
import org.rodinp.core.IRodinElement;
import org.rodinp.core.RodinDBException;

public class BTheorem {
	private List<Predicate> EMPTY_HYP = new ArrayList<Predicate>();
	private List<Predicate> hypothesis; 
	private Predicate goal;
	private IInternalElement origin;
	
	public BTheorem(List<Predicate> hypothesis, Predicate goal) {
		this.hypothesis = hypothesis;
		this.goal = goal;
	}

	public BTheorem(Predicate goal) {
		this.hypothesis = EMPTY_HYP;
		this.goal = goal;
	}

	public BTheorem(ISimpleSequent sequent) throws RodinDBException {
		this.origin = getOrigin(sequent.getOrigin());
		
		if (sequent.getPredicates().length > 0) {
			hypothesis = new ArrayList<Predicate>(sequent.getPredicates().length - 1);
			
			for (ITrackedPredicate x : sequent.getPredicates()) {
				if (x.isHypothesis()) {
					hypothesis.add(x.getPredicate());
				} else {
					goal = x.getPredicate();
				}
			}
		} else {
			hypothesis = new ArrayList<Predicate>(0);
			goal = FormulaFactory.getDefault().makeLiteralPredicate(
					Formula.BFALSE, null);
		}
	}
	
	private IInternalElement getOrigin(Object origin) throws RodinDBException {
		if (origin instanceof IPOSequent) {
			IPOSequent po_seq = (IPOSequent) origin;
			for(IPOSource s: po_seq.getSources()) {
				if (!s.getRole().equals(IPOSource.ABSTRACT_ROLE)) {
					IRodinElement element = s.getSource();
					if (element instanceof IInternalElement) {
						IInternalElement internal_el = (IInternalElement) element;
						internal_el = internal_el.getRoot();
						if (internal_el instanceof IMachineRoot || internal_el instanceof IContextRoot) {
							return internal_el;
						}
					}
				}
			}
		}		
		
		return null;
	}
	
	public IInternalElement getOrigin() {
		return origin;
	}

	public List<Predicate> getHypothesis() {
		return hypothesis;
	}
	
	public Predicate getGoal() {
		return goal;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((goal == null) ? 0 : goal.hashCode());
		result = prime * result
				+ ((hypothesis == null) ? 0 : hypothesis.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BTheorem other = (BTheorem) obj;
		if (goal == null) {
			if (other.goal != null)
				return false;
		} else if (!goal.equals(other.goal))
			return false;
		if (hypothesis == null) {
			if (other.hypothesis != null)
				return false;
		} else if (!hypothesis.equals(other.hypothesis))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for(Predicate hyp: hypothesis) {
			sb.append("\t");
			sb.append(hyp.toString());
			sb.append("\n");
		}
		sb.append("|-\n");	
		sb.append("\t");	
		sb.append(goal);	
		sb.append("\n");	
		
		return sb.toString();
	}
	
}
