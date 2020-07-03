package com.oracle.truffle.adaptable.module;

import java.util.ArrayList;
import java.util.List;

import org.graalvm.options.OptionValues;

import com.oracle.truffle.adaptable.language.AdaptationContext;
import com.oracle.truffle.adaptable.language.TruffleAdaptableLanguage;
import com.oracle.truffle.adaptable.language.decision.model.Goal;
import com.oracle.truffle.adaptable.language.decision.model.Resource;
import com.oracle.truffle.adaptable.language.decision.model.Softgoal;
import com.oracle.truffle.api.instrumentation.SourceSectionFilter;
import com.oracle.truffle.api.instrumentation.TruffleInstrument;

public abstract class TruffleAdaptableModule
	<AdaptationCtx extends AdaptationContext<?>,
	Lang extends TruffleAdaptableLanguage<AdaptationCtx>> 
		extends TruffleInstrument {
	
	private Env env;
	private List<AdaptationListener> listeners;
	private Goal moduleTradeOff;
	
	@Override
	final protected void onCreate(Env env) {
		this.env = env;
		this.listeners = new ArrayList<AdaptationListener>();
		this.moduleTradeOff = new Goal("Module Trade-Off");
		Lang.registerModule((TruffleAdaptableModule<AdaptationContext<?>, ?>) this);
		
		env.registerService(this);
	}
	
	public void afterRegister() {
		AdaptationCtx adaptationContext = (AdaptationCtx) Lang.getAdaptationContext();
		
		init(adaptationContext, env.getOptions());
		
		List<Resource> resources = adaptationContext.getResources();
		for (Resource resource : resources) {
			connectResource(resource);
		}
		
		String[] softgoalIDs = adaptationContext.softgoalIDs();
		for (int i = 0; i < softgoalIDs.length; i++) {
			Softgoal soft = new Softgoal(softgoalIDs[i]);
			moduleTradeOff.addContribution(soft, 1.0 / softgoalIDs.length);
			connectSoftGoal(soft);
		}
	}
	
	protected final void attachExecutionListener(SourceSectionFilter filter, AdaptationListener listener) {
		env.getInstrumenter().attachExecutionEventListener(filter, listener);
		listeners.add(listener);
	}
	
	public final Goal getModuleTradeOff() {
		return moduleTradeOff;
	}
	
	public abstract void init(AdaptationCtx adaptationContext, OptionValues options);
	
	public abstract void connectSoftGoal(Softgoal softgoal);
	
	public abstract void connectResource(Resource resource);
	
	public void setEnabled(boolean b) {
		System.err.println("SET ENABLED : " + b);
		for (AdaptationListener adaptationListener : listeners) {
			adaptationListener.setEnabled(b);
		}
	}
	
}
