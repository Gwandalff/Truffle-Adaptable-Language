package com.oracle.truffle.adaptable.language;


import java.util.List;
import java.util.Map;

import com.oracle.truffle.adaptable.language.decision.model.Goal;
import com.oracle.truffle.adaptable.language.decision.model.Resource;
import com.oracle.truffle.adaptable.language.decision.model.Softgoal;
import com.oracle.truffle.adaptable.module.TruffleAdaptableModule;

public abstract class AdaptationContext<Lang extends TruffleAdaptableLanguage<?>> {
	
	private Map<String, Double> userConfig;
	private List<Softgoal> softgoals;
	private List<Resource> resources;
	private Goal tradeOff;

	public AdaptationContext() {
		tradeOff = new Goal("GlobalTradeOff");
	}
	
	public final void registerModule(TruffleAdaptableModule<?, AdaptationContext<?>> module) {
		Lang.registerModule(module);
	}
	
	Map<String, Double> getUserConfig() {
		return userConfig;
	}

	void setUserConfig(Map<String, Double> userConfig) {
		this.userConfig = userConfig;
	}

	List<Softgoal> getSoftgoals() {
		return softgoals;
	}
	
	List<Resource> getResources() {
		return resources;
	}
	
	Goal getGoal() {
		return tradeOff;
	}

	public abstract Map<String, Double> loadUserConfig();
	
	public abstract Softgoal[] softgoals();
	
	public abstract Resource[] resources();

}
