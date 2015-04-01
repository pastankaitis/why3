package uk.ac.ncl.eventb.why3.gen.type;


public class CLTypeAny extends CLType {
	public static final CLType INSTANCE = new CLTypeAny(); 
	public static final CLType POWER_INSTANCE = new CLPowerType(INSTANCE);		
	public static final CLType DOUBLE_POWER_INSTANCE = new CLPowerType(new CLPowerType(INSTANCE));		
	private CLType bakedType;
	private String id;
	
	public CLTypeAny() {
		this.id = "x";
	}

	public CLTypeAny(String id) {
		this.id = id;
	}
	
	
	/**
	 * Type baking fixates a type variable to a monomorphic type
	 * @param type a monomorphic type
	 */
	public void bake(CLType type) {
		assert(type != null && !type.isPolymorphic() && !type.isTemplate());
		bakedType = type;
	}
	
	public CLType getBakedType() {
		return bakedType;
	}

	@Override
	public boolean isPolymorphic() {
		return true;
	}

	@Override
	public boolean isTemplate() {
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((bakedType == null) ? 0 : bakedType.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override 
	public boolean equals(Object other) {
		if (bakedType != null)
			return bakedType.equals(other);
		else if (other instanceof CLTypeAny) {
			CLTypeAny any = (CLTypeAny) other;
			return any.id.equals(id);
		} else {
			return false;
		}
	}

	@Override 
	public boolean isSet() {
		if (bakedType != null)
			return bakedType.isSet();
		else
			return false;
	}	
	
	@Override
	public boolean isRelation() {
		if (bakedType != null)
			return bakedType.isRelation();
		else
			return false;
	}

	@Override
	public CLType domType() {
		if (bakedType != null)
			return bakedType.domType();
		else
			return null;
	}

	@Override
	public CLType ranType() {
		if (bakedType != null)
			return bakedType.ranType();
		else
			return null;
	}
	
	@Override
	public String toString() {
		if (bakedType != null)
			return bakedType.toString();
		else
			return "?" + super.hashCode();
	}

	@Override
	public String toWhy3Type() {
		return "'" + id;
	}
	
	
}
