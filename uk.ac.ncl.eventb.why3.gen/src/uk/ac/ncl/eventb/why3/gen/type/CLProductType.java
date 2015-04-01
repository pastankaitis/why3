package uk.ac.ncl.eventb.why3.gen.type;

public class CLProductType extends CLType {
	public static final CLProductType TEMPLATE = new CLProductType(null, null);	
	private CLType left;	
	private CLType right;	
	
	public CLProductType(CLType left, CLType right) {
		this.left = left;
		this.right = right;
	}

	public CLType getLeft() {
		return left;
	}

	public CLType getRight() {
		return right;
	}

	@Override
	public boolean equals(Object _o) {
		if (_o instanceof CLProductType) {
			CLProductType pt = (CLProductType) _o;
			return pt.left.equals(left) && pt.right.equals(right);
		} else if (_o instanceof CLTypeAny) {
			return _o.equals(this);
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return left.toString() + "**" + right.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
		return result;
	}
	
	@Override
	public boolean isPolymorphic() {
		return (left != null && left.isPolymorphic()) || (right != null && right.isPolymorphic());
	}	
	
	@Override
	public boolean isTemplate() {
		return (left == null || left.isTemplate()) || (right == null || right.isTemplate());
	}	
	
	
	@Override	
	public CLType makePolymorphicType() {
		assert (isTemplate());
		return new CLProductType(makePolymorphicType(left), makePolymorphicType(right));
	}

	@Override
	public String toWhy3Type() {
		return "(" + left.toWhy3Type() + ", " + right.toWhy3Type() + ")";
	}		
}
