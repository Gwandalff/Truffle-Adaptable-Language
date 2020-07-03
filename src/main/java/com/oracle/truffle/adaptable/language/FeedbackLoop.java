package com.oracle.truffle.adaptable.language;

import com.oracle.truffle.api.instrumentation.Instrumenter;
import com.oracle.truffle.api.instrumentation.SourceSectionFilter;
import com.oracle.truffle.api.instrumentation.Tag;
import com.oracle.truffle.api.instrumentation.TruffleInstrument;
import com.oracle.truffle.api.nodes.Node;


public abstract class FeedbackLoop<Lang extends TruffleAdaptableLanguage<?>> extends TruffleInstrument {
	
	public static final String ID = "Feedback-Loop";
	
	private AdaptationContext adaptationContext;

	@Override
	protected final void onCreate(Env env) {
		adaptationContext = Lang.getAdaptationContext();
		Instrumenter instrumenter = env.getInstrumenter();
		SourceSectionFilter filter = SourceSectionFilter.newBuilder().tagIs(getFeedbackLoopTrigger()).build();
		instrumenter.attachExecutionEventListener(filter, new FeedbackLoopExecutor(this));
		env.registerService(this);
	}
	
	public final void start(Env env) {
		this.onCreate(env);
	}
	
	final AdaptationContext getAdaptationContext() {
		return this.adaptationContext;
	}
	
	protected abstract Class<? extends Tag> getFeedbackLoopTrigger();
	protected abstract boolean additionalFilter(Node trigger);

}
