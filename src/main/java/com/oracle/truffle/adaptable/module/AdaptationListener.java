package com.oracle.truffle.adaptable.module;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.EventContext;
import com.oracle.truffle.api.instrumentation.ExecutionEventListener;
import com.oracle.truffle.api.nodes.Node;

public abstract class AdaptationListener implements ExecutionEventListener {
	
	private boolean enabled = false;
	private EventContext unwindContext;
	private VirtualFrame frame;

	@Override
	public final void onEnter(EventContext context, VirtualFrame frame) {
		if (enabled) {
			unwindContext = context;
			this.frame = frame;
			before(context.getInstrumentedNode());
		}
	}

	@Override
	public final void onReturnValue(EventContext context, VirtualFrame frame, Object result) {
		if (enabled) {
			unwindContext = context;
			this.frame = frame;
			afterResult(context.getInstrumentedNode(), result);
		}
	}

	@Override
	public final void onReturnExceptional(EventContext context, VirtualFrame frame, Throwable exception) {
		if (enabled) {
			unwindContext = context;
			this.frame = frame;
			afterException(context.getInstrumentedNode(), exception);
		}
	}
	
	public final Object onUnwind(EventContext context, VirtualFrame frame, Object info) {
		this.frame = frame;
		return afterBypass(context.getInstrumentedNode(), info);
	}
	
	protected final void bypass(Object info) {
		throw unwindContext.createUnwind(info);
	}
	
	protected final VirtualFrame frame() {
		return frame;
	}
	
	final void setEnabled(boolean b) {
		enabled = b;
	}
	
	public abstract void   before        (Node instrumentedNode);
	public abstract void   afterResult   (Node instrumentedNode, Object result);
	public abstract void   afterException(Node instrumentedNode, Throwable exception);
	public abstract Object afterBypass   (Node instrumentedNode, Object info);

}
