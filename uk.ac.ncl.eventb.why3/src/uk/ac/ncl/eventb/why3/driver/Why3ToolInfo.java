package uk.ac.ncl.eventb.why3.driver;

public class Why3ToolInfo {
	private String name;
	private String version;

	public Why3ToolInfo(String name, String version) {
		super();
		this.name = name;
		this.version = version;
	}

	/**
	 * @return prover name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return prover version
	 */
	public String getVersion() {
		return version;
	}

	@Override
	public String toString() {
		return name + " (" + version + ")";
	}
}