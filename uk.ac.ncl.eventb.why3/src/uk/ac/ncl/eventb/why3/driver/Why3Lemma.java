package uk.ac.ncl.eventb.why3.driver;

import uk.ac.ncl.eventb.why3.translator.TheoremTranslated;

public class Why3Lemma {
	private String lemma;

	public Why3Lemma(String lemma) {
		super();
		this.lemma = lemma;
	}

	public TheoremTranslated getTranslatedForm() {
		return new TheoremTranslated(lemma, false);
	}
	
	public String getLemma() {
		return lemma;
	}
	
}
