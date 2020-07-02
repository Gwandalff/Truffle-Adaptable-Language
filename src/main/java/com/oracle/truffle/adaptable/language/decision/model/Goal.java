package com.oracle.truffle.adaptable.language.decision.model;

import java.util.HashMap;

public class Goal extends ModelingElement {

	public Goal(String ID) {
		super(ID);
	}
	
	public final void detach() {
		inputLinks = new HashMap<ModelingElement, Double>();
	}
	
	public void cleanModel() {
		this.clean();
	}
	
	public void assessVariables() {
		this.assessVariables(1.0);
	}
	
	public boolean execute() {
		return this.evaluate() > 0;
	}

}
