package com.oracle.truffle.adaptable.language;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.oracle.truffle.adaptable.language.decision.model.Goal;
import com.oracle.truffle.adaptable.language.decision.model.Resource;
import com.oracle.truffle.adaptable.module.TruffleAdaptableModule;
import com.oracle.truffle.api.TruffleLanguage;

public abstract class TruffleAdaptableLanguage<AdaptationCtx extends AdaptationContext<?>> extends TruffleLanguage<AdaptationCtx> {
	
	private static List<TruffleAdaptableModule<?, ?>> modules = new ArrayList<>();
	private static List<Boolean> activation = new ArrayList<>();
	private static AdaptationContext<?> adaptationContext;
	
	protected abstract AdaptationCtx createAdaptationContext();
	protected abstract FeedbackLoop createFeedbackLoop(AdaptationCtx ctx);
	protected abstract Class<? extends FeedbackLoop> getFeedbackLoopType();
	
	@Override
	protected final AdaptationCtx createContext(Env env) {
		AdaptationCtx adaptationCtx = createAdaptationContext();
		System.err.println("CONTEXT CREATED");
		adaptationContext = adaptationCtx;
		env.lookup(env.getInstruments().get("Feedback-Loop"), getFeedbackLoopType());
		for (TruffleAdaptableModule module : modules) {
			adaptationCtx.registerModule(module);
		}
		return adaptationCtx;
	}
	
	public final static AdaptationContext<?> getAdaptationContext() {
		return adaptationContext;
	}
	
	public final static void monitor(AdaptationContext<?> context) {
		System.err.println("MONITOR");
		context.setUserConfig(context.loadUserConfig());
		List<Resource> resources = context.getResources();
		for (Resource resource : resources) {
			resource.setValue(0);
		}
	}
	
	public final static void analyze(AdaptationContext<?> context) {
		System.err.println("ANALYZE");
		Goal tradeOff = context.getGoal();
		tradeOff.cleanModel();
		Map<String, Double> userConfig = context.getUserConfig();
		Set<String> IDS = userConfig.keySet();
		
		for (TruffleAdaptableModule<?, ?> module : modules) {
			for (String id : IDS) {
				module.getModuleTradeOff().updateLink(id, userConfig.get(id));
			}
		}
		
		context.getGoal().assessVariables();
	}
	
	public final static void plan   (AdaptationContext<?> context) {
		System.err.println("PLAN");
		activation = new ArrayList<>();
		for (TruffleAdaptableModule<?, ?> module : modules) {
				activation.add(module.getModuleTradeOff().execute());
		}
	}
	
	public final static void execute(AdaptationContext<?> context) {
		System.err.println("EXECUTE");
		for (int i = 0; i < modules.size(); i++) {
			modules.get(i).setEnabled(activation.get(i));
		}
	}
	
	public final static void registerModule(TruffleAdaptableModule<AdaptationContext<?>, ?> module) {
		System.err.println("REGISTER LANGUAGE");
		modules.add(module);
	}
	
}
