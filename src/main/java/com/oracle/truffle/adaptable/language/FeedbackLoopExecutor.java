package com.oracle.truffle.adaptable.language;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.EventContext;
import com.oracle.truffle.api.instrumentation.ExecutionEventListener;

public class FeedbackLoopExecutor
		<Lang extends TruffleAdaptableLanguage<?>, 
		AdaptationCtx extends AdaptationContext<Lang>>
	implements ExecutionEventListener {
	
	private AdaptationCtx adaptationContext;
	
	public FeedbackLoopExecutor() {
		adaptationContext = Lang.<AdaptationCtx>getAdaptationContext();
	}

	@Override
	public void onEnter(EventContext context, VirtualFrame frame) {
		Lang.<AdaptationCtx>monitor(adaptationContext);
		Lang.<AdaptationCtx>analyze(adaptationContext);
		Lang.<AdaptationCtx>plan   (adaptationContext);
		Lang.<AdaptationCtx>execute(adaptationContext);
	}

	@Override
	public void onReturnValue(EventContext context, VirtualFrame frame, Object result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReturnExceptional(EventContext context, VirtualFrame frame, Throwable exception) {
		// TODO Auto-generated method stub

	}

}
