package com.oracle.truffle.adaptable.module;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.EventContext;
import com.oracle.truffle.api.instrumentation.ExecutionEventListener;

public class AdaptationListener implements ExecutionEventListener {

	@Override
	public void onEnter(EventContext context, VirtualFrame frame) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReturnValue(EventContext context, VirtualFrame frame, Object result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReturnExceptional(EventContext context, VirtualFrame frame, Throwable exception) {
		// TODO Auto-generated method stub

	}
	
	final void setEnabled(boolean b) {
		
	}

}
