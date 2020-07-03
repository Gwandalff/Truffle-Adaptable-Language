package com.oracle.truffle.adaptable.language.decision.model;

import java.util.HashMap;
import java.util.Set;

public class Goal extends ModelingElement {

	public Goal(String ID) {
		super(ID);
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
	
	public final void updateLink(String softgoalID, double newValue) {
		Set<ModelingElement> softs = inputLinks.keySet();
		for (ModelingElement soft : softs) {
			if(soft.ID.equals(softgoalID)) {
				inputLinks.put(soft, newValue);
				break;
			}
		}
	}

}
