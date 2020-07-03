package com.oracle.truffle.adaptable.language;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.oracle.truffle.adaptable.language.decision.model.Goal;
import com.oracle.truffle.adaptable.language.decision.model.Resource;
import com.oracle.truffle.adaptable.module.TruffleAdaptableModule;

public abstract class AdaptationContext<Lang extends TruffleAdaptableLanguage<?>> {
	
	private Map<String, Double> userConfig;
	private List<Resource> resources;
	private List<Goal> tradeOffByModule;
	private Goal tradeOff;

	public AdaptationContext() {
		tradeOff = new Goal("GlobalTradeOff");
		resources = new ArrayList<Resource>();
		for (Resource resource : createResources()) {
			resources.add(resource);
		}
		tradeOffByModule = new ArrayList<Goal>();
	}
	
	final void registerModule(TruffleAdaptableModule<AdaptationContext<?>, ?> module) {
		module.afterRegister();
		System.err.println("REGISTER");
		tradeOffByModule.add(module.getModuleTradeOff());
		System.err.println("TRADE BY MODULE");
		tradeOff.addContribution(module.getModuleTradeOff(), 1.0);
		System.err.println("TRADE");
	}
	
	final Map<String, Double> getUserConfig() {
		return userConfig;
	}

	final void setUserConfig(Map<String, Double> userConfig) {
		this.userConfig = userConfig;
	}
	
	public final List<Resource> getResources() {
		return resources;
	}
	
	final List<Goal> getTradeOffByModule() {
		return tradeOffByModule;
	}
	
	final Goal getGoal() {
		return tradeOff;
	}

	public abstract Map<String, Double> loadUserConfig();
	
	public abstract String[] softgoalIDs();
	
	protected abstract Resource[] createResources();

}
