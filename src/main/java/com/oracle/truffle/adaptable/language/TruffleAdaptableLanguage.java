package com.oracle.truffle.adaptable.language;

import com.oracle.truffle.api.TruffleLanguage;

public abstract class TruffleAdaptableLanguage<ExecutionContext> extends TruffleLanguage<ExecutionContext> {
	
	public static <AdaptationCtx extends AdaptationContext<?>> AdaptationCtx getAdaptationContext() {
		return null;
	}
	
}
