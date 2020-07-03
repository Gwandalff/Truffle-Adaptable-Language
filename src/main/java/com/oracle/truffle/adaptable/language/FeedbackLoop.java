package com.oracle.truffle.adaptable.language;

import com.oracle.truffle.api.instrumentation.Instrumenter;
import com.oracle.truffle.api.instrumentation.SourceSectionFilter;
import com.oracle.truffle.api.instrumentation.Tag;
import com.oracle.truffle.api.instrumentation.TruffleInstrument;
import com.oracle.truffle.api.nodes.Node;

public abstract class FeedbackLoop
		<Lang extends TruffleAdaptableLanguage<?>, 
		AdaptationCtx extends AdaptationContext<Lang>> 
	extends TruffleInstrument {
	
	public static final String ID = "Feedback-Loop";
	
	//@Option(name = "enable", help = "Unused", category = OptionCategory.USER, stability = OptionStability.STABLE)
    //static final OptionKey<Boolean> ENABLED = new OptionKey<Boolean>(false);

	@Override
	protected final void onCreate(Env env) {
		System.out.println("Loop created");
		Instrumenter instrumenter = env.getInstrumenter();
		SourceSectionFilter filter = SourceSectionFilter.newBuilder().tagIs(getFeedbackLoopTrigger()).build();
		instrumenter.attachExecutionEventListener(filter, new FeedbackLoopExecutor<Lang,AdaptationCtx>(this));
		env.registerService(this);
	}
	
	protected abstract Class<? extends Tag> getFeedbackLoopTrigger();
	protected abstract boolean additionalFilter(Node trigger);

}
