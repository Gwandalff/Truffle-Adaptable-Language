package com.oracle.truffle.adaptable.language.decision.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class ModelingElement {
	
	final public String ID;
	
	protected Map<ModelingElement, Double> inputLinks;
	protected Double value;

	public ModelingElement(String ID) {
		inputLinks = new HashMap<ModelingElement, Double>();
		value = null;
		this.ID = ID;
	}
	
	protected void clean() {
		value = null;
		inputLinks.keySet().forEach( (ModelingElement elem) -> {elem.clean();} );
	}
	
	protected Set<Variable> assessVariables(Double pathImpact) {
		Set<ModelingElement> children = inputLinks.keySet();
		Set<Variable> out = new HashSet<Variable>();
		for (ModelingElement element : children) {
			out.addAll(element.assessVariables(pathImpact * inputLinks.get(element)));
		}
		return out;
	}
	
	protected Double evaluate() {
		if (value != null) {
			return value;
		}
		
		boolean computable= true;
		value = 0.0;
		Set<ModelingElement> children = inputLinks.keySet();
		for (ModelingElement element : children) {
			Double val = element.evaluate();
			if (val == null) {
				computable = false;
			} else {
				value += val * inputLinks.get(element);
			}
		}
		System.err.println(ID + " : " + (computable?"computed -> ":"not computed -> ") + value);
		if(!computable) value = null;
		return value;
	}
	
	public void addContribution(ModelingElement elem, double impact) {
		inputLinks.put(elem, impact);
	}

}
