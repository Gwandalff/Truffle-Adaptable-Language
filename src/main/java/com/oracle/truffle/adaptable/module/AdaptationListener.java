package com.oracle.truffle.adaptable.module;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.EventContext;
import com.oracle.truffle.api.instrumentation.ExecutionEventListener;
import com.oracle.truffle.api.nodes.Node;

public abstract class AdaptationListener implements ExecutionEventListener {
	
	private boolean enabled = false;
	private EventContext unwindContext;

	@Override
	public final void onEnter(EventContext context, VirtualFrame frame) {
		if (enabled) {
			unwindContext = context;
			before(context.getInstrumentedNode());
		}
	}

	@Override
	public final void onReturnValue(EventContext context, VirtualFrame frame, Object result) {
		if (enabled) {
			unwindContext = context;
			afterResult(context.getInstrumentedNode(), result);
		}
	}

	@Override
	public final void onReturnExceptional(EventContext context, VirtualFrame frame, Throwable exception) {
		if (enabled) {
			unwindContext = context;
			afterException(context.getInstrumentedNode(), exception);
		}
	}
	
	public final Object onUnwind(EventContext context, VirtualFrame frame, Object info) {
		return afterBypass(context.getInstrumentedNode());
	}
	
	public final void bypass(Object info) {
		throw unwindContext.createUnwind(info);
	}
	
	final void setEnabled(boolean b) {
		enabled = b;
	}
	
	public abstract void   before        (Node instrumentedNode);
	public abstract void   afterResult   (Node instrumentedNode, Object result);
	public abstract void   afterException(Node instrumentedNode, Throwable exception);
	public abstract Object afterBypass   (Node instrumentedNode);

}
