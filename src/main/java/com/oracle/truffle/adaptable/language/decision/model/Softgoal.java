package com.oracle.truffle.adaptable.language.decision.model;

import java.util.HashMap;

public class Softgoal extends ModelingElement {

	public Softgoal(String ID) {
		super(ID);
	}
	
	public final void detach() {
		inputLinks = new HashMap<ModelingElement, Double>();
	}

}
