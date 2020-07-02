package com.oracle.truffle.adaptable.language;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.oracle.truffle.adaptable.language.decision.model.Goal;
import com.oracle.truffle.adaptable.language.decision.model.Resource;
import com.oracle.truffle.adaptable.language.decision.model.Softgoal;
import com.oracle.truffle.adaptable.module.TruffleAdaptableModule;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.instrumentation.Tag;

public abstract class TruffleAdaptableLanguage<ExecutionContext> extends TruffleLanguage<ExecutionContext> {
	
	private static List<TruffleAdaptableModule> modules = new ArrayList<>();
	
	public static <AdaptationCtx extends AdaptationContext<?>> AdaptationCtx getAdaptationContext() {
		return null;
	}
	
	public static Class<? extends Tag> getFeedbackLoopTrigger() {
		return null;
	}
	
	public static <AdaptationCtx extends AdaptationContext<?>> void monitor(AdaptationCtx context) {
		context.setUserConfig(context.loadUserConfig());
		List<Resource> resources = context.getResources();
		for (Resource resource : resources) {
			resource.setValue(0);
		}
	}
	
	public static <AdaptationCtx extends AdaptationContext<?>> void analyze(AdaptationCtx context) {
		Goal tradeOff = context.getGoal();
		tradeOff.cleanModel();
		tradeOff.detach();
		Map<String, Double> userConfig = context.getUserConfig();
		List<Softgoal> softs = context.getSoftgoals();
		for (Softgoal softgoal : softs) {
			double impact = userConfig.getOrDefault(softgoal.ID, 0.0);
			tradeOff.addContribution(softgoal, impact);
		}
		for (TruffleAdaptableModule module : modules) {
			for (Softgoal softgoal : softs) {
				module.connectSoftGoal(softgoal);
			}
		}
		tradeOff.assessVariables();
		for (Softgoal softgoal : softs) {
			softgoal.detach();
		}
	}
	
	public static <AdaptationCtx extends AdaptationContext<?>> void plan   (AdaptationCtx context) {
		
	}
	
	public static <AdaptationCtx extends AdaptationContext<?>> void execute(AdaptationCtx context) {}
	
	final static void registerModule(TruffleAdaptableModule<?, AdaptationContext<?>> module) {
		modules.add(module);
	}
	
}
