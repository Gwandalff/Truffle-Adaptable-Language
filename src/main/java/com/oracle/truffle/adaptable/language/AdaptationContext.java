package com.oracle.truffle.adaptable.language;


import java.util.List;
import java.util.Map;

import com.oracle.truffle.adaptable.language.decision.model.Goal;
import com.oracle.truffle.adaptable.language.decision.model.Resource;
import com.oracle.truffle.adaptable.language.decision.model.Softgoal;
import com.oracle.truffle.adaptable.module.TruffleAdaptableModule;

public abstract class AdaptationContext<Lang extends TruffleAdaptableLanguage<?>> {
	
	private Map<String, Double> userConfig;
	private List<Resource> resources;
	private List<Goal> tradeOffByModule;
	private Goal tradeOff;

	public AdaptationContext() {
		tradeOff = new Goal("GlobalTradeOff");
	}
	
	public final void registerModule(TruffleAdaptableModule<?, AdaptationContext<?>> module) {
		tradeOffByModule.add(module.getModuleTradeOff());
		tradeOff.addContribution(module.getModuleTradeOff(), 1.0);
		Lang.registerModule(module);
	}
	
	final Map<String, Double> getUserConfig() {
		return userConfig;
	}

	final void setUserConfig(Map<String, Double> userConfig) {
		this.userConfig = userConfig;
	}
	
	final List<Resource> getResources() {
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
	
	public abstract Resource[] resources();

}
