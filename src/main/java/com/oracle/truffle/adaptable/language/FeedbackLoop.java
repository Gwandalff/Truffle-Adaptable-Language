package com.oracle.truffle.adaptable.language;

import org.graalvm.options.OptionCategory;
import org.graalvm.options.OptionDescriptors;
import org.graalvm.options.OptionKey;
import org.graalvm.options.OptionStability;

import com.oracle.truffle.api.Option;
import com.oracle.truffle.api.instrumentation.Instrumenter;
import com.oracle.truffle.api.instrumentation.SourceSectionFilter;
import com.oracle.truffle.api.instrumentation.Tag;
import com.oracle.truffle.api.instrumentation.TruffleInstrument;
import com.oracle.truffle.api.instrumentation.TruffleInstrument.Registration;

@Registration(id = FeedbackLoop.ID, name = "Feedback Loop", version = "0.1", services = FeedbackLoop.class)
public class FeedbackLoop
		<Lang extends TruffleAdaptableLanguage<?>, 
		AdaptationCtx extends AdaptationContext<Lang>> 
	extends TruffleInstrument {
	
	public static final String ID = "Feedback-Loop";
	
	@Option(name = "", help = "Unused", category = OptionCategory.USER, stability = OptionStability.STABLE)
    static final OptionKey<Boolean> ENABLED = new OptionKey<Boolean>(false);

	@Override
	protected void onCreate(Env env) {
		Class<? extends Tag> tag = Lang.getFeedbackLoopTrigger();
		Instrumenter instrumenter = env.getInstrumenter();
		SourceSectionFilter filter = SourceSectionFilter.newBuilder().tagIs(tag).build();
		instrumenter.attachExecutionEventListener(filter, new FeedbackLoopExecutor<Lang,AdaptationCtx>());
		env.registerService(this);
	}
	
	@Override
    protected OptionDescriptors getOptionDescriptors() {
        return new FeedbackLoopOptionDescriptors();
    }

}
