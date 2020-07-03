package com.oracle.truffle.adaptable.language;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.oracle.truffle.adaptable.language.decision.model.Goal;
import com.oracle.truffle.adaptable.language.decision.model.Resource;
import com.oracle.truffle.adaptable.language.decision.model.Softgoal;
import com.oracle.truffle.adaptable.module.TruffleAdaptableModule;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.instrumentation.Tag;

public abstract class TruffleAdaptableLanguage<ExecutionContext> extends TruffleLanguage<ExecutionContext> {
	
	private static List<TruffleAdaptableModule> modules = new ArrayList<>();
	private static List<Boolean> activation = new ArrayList<>();
	
	public static AdaptationContext getAdaptationContext() {
		System.err.println("This should not be called");
		return null;
	}
	
	public final static void monitor(AdaptationContext context) {
		context.setUserConfig(context.loadUserConfig());
		List<Resource> resources = context.getResources();
		for (Resource resource : resources) {
			resource.setValue(0);
		}
	}
	
	public final static void analyze(AdaptationContext context) {
		Goal tradeOff = context.getGoal();
		tradeOff.cleanModel();
		Map<String, Double> userConfig = context.getUserConfig();
		Set<String> IDS = userConfig.keySet();
		
		for (TruffleAdaptableModule module : modules) {
			for (String id : IDS) {
				module.getModuleTradeOff().updateLink(id, userConfig.get(id));
			}
		}
		
		context.getGoal().assessVariables();
	}
	
	public final static void plan   (AdaptationContext context) {
		activation = new ArrayList<>();
		for (TruffleAdaptableModule module : modules) {
				activation.add(module.getModuleTradeOff().execute());
		}
	}
	
	public final static void execute(AdaptationContext context) {
		for (int i = 0; i < modules.size(); i++) {
			modules.get(i).setEnabled(activation.get(i));
		}
	}
	
	final static void registerModule(TruffleAdaptableModule<?, AdaptationContext<?>> module) {
		modules.add(module);
	}
	
}
