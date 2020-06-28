package com.oracle.truffle.adaptable.language.decision.model;

import java.util.HashSet;
import java.util.Set;

public final class Variable extends ModelingElement {
	
	private double globalImpact;
	private double lowerBound;
	private double higherBound;
	private double equilibrium;

	public Variable(String ID) {
		super(ID);
		this.lowerBound  = -100;
		this.higherBound =  100;
		this.equilibrium =  0;
	}
	
	public Variable(String ID, double equilibrium) {
		super(ID);
		this.lowerBound  = -100;
		this.higherBound =  100;
		this.equilibrium =  equilibrium;
	}
	
	public Variable(String ID, double equilibrium, double lowerBound) {
		super(ID);
		this.lowerBound  = lowerBound;
		this.higherBound = 100;
		this.equilibrium = equilibrium;
	}
	
	public Variable(String ID,double equilibrium,  double lowerBound, double higherBound) {
		super(ID);
		this.lowerBound  = lowerBound;
		this.higherBound = higherBound;
		this.equilibrium = equilibrium;
	}
	
	@Override
	protected Set<Variable> assessVariables(Double pathImpact) {
		this.globalImpact += globalImpact;
		Set<Variable> out = new HashSet<Variable>();
		out.add(this);
		return out;
	}
	
	@Override
	protected Double evaluate() {
		if (value != null) {
			return value;
		}
		
		if (globalImpact == 0) {
			value = equilibrium;
		} else {
			value = globalImpact > 0 ? higherBound : lowerBound;
		}
		
		return value;
	}
	
	@Override
	public void addContribution(ModelingElement elem, double impact){
		System.out.println("WARNING : Add contribution to a variable is unnecessary");
	}

}
