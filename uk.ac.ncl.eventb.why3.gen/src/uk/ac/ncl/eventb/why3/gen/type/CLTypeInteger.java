package uk.ac.ncl.eventb.why3.gen.type;

public class CLTypeInteger extends CLType {
	public static final CLType INSTANCE = new CLTypeInteger(); 
	public static final CLType POWER_INSTANCE = new CLPowerType(INSTANCE); 
	
	private CLTypeInteger() {
	}

	@Override
	public boolean isPolymorphic() {
		return false;
	}

	@Override
	public boolean equals(Object _o) {
		if (_o == this) {
			return true;
		} else if (_o instanceof CLTypeAny) {
			return _o.equals(this);
		}
		return false;
	}
	
	@Override
	public boolean isTemplate() {
		return false;
	}

	@Override
	public String toWhy3Type() {
		return "int";
	}

}
