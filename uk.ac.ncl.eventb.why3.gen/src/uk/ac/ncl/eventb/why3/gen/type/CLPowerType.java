package uk.ac.ncl.eventb.why3.gen.type;


public class CLPowerType extends CLType {
	public static final CLPowerType TEMPLATE = new CLPowerType(null);
	public static final CLPowerType RELATION = new CLPowerType(new CLProductType(new CLTypeAny("a"), new CLTypeAny("b")));
	public static final CLPowerType POW = new CLPowerType(new CLPowerType(new CLTypeAny("a")));
	
	private CLType base;	
	
	public CLPowerType(CLType base) {
		this.base = base;
	}
	
	public CLType getBase() {
		return base;
	}

	@Override
	public boolean equals(Object _o) {
		if (_o instanceof CLPowerType) {
			CLPowerType pt = (CLPowerType) _o;
			if (pt.base == null || base == null)
				return true;
			else
				return pt.base.equals(base);
		} else if (_o instanceof CLTypeAny) {
			return _o.equals(this);
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		if (base != null)
			return "set(" + base.toString() + ")";
		else
			return "set(?)";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((base == null) ? 0 : base.hashCode());
		return result;
	}
	
	@Override
	public boolean isPolymorphic() {
		return base != null && base.isPolymorphic();
	}	
	
	@Override
	public boolean isTemplate() {
		return base == null || base.isTemplate();
	}	
	
	@Override	
	public CLType makePolymorphicType() {
		assert (isTemplate());
		return new CLPowerType(makePolymorphicType(base));
	}

	@Override
	public String toWhy3Type() {
		if (isRelation()) {
			CLProductType _base = (CLProductType) base;
			return "(rel " + _base.getLeft().toWhy3Type() + " " + _base.getRight().toWhy3Type() + ")";
		} else {
			return "(set " + base.toWhy3Type() + ")";  
		}
	}	

}
