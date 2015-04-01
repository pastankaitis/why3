package uk.ac.ncl.eventb.why3.gen.type;



public abstract class CLType  {
	
	/**
	 * Returns whether a given type is a template type
	 */
	public abstract boolean isTemplate();	
	
	public abstract boolean isPolymorphic();

	public abstract String toWhy3Type();
	
	/**
	 * Transforms a template type into a new instance of a polymorphic type
	 * with fresh type variables
	 * @return
	 */
	public CLType makePolymorphicType() {
		return this;
	}

	public CLType makePolymorphicType(CLType type) {
		if (type == null)
			return new CLTypeAny();
		else if (type.isTemplate())
			return type.makePolymorphicType();
		else
			return type;
	}
	
	public boolean isSet() {
		if (this instanceof CLPowerType) {
			return true;
		}
		
		return false;
	}	
	
	public boolean isRelation() {
		if (this instanceof CLPowerType) {
			CLPowerType st = (CLPowerType) this;
			if (st.getBase() instanceof CLProductType) {
				return true;
			}
		}
		
		return false;
	}

	public CLType domType() {
		if (this instanceof CLPowerType) {
			CLPowerType st = (CLPowerType) this;
			if (st.getBase() instanceof CLProductType) {
				CLProductType pr = (CLProductType) st.getBase();
				return pr.getLeft();
			}
		}
		
		return null;
	}

	public CLType ranType() {
		if (this instanceof CLPowerType) {
			CLPowerType st = (CLPowerType) this;
			if (st.getBase() instanceof CLProductType) {
				CLProductType pr = (CLProductType) st.getBase();
				return pr.getRight();
			}
		}
		
		return null;
	}

	
}
