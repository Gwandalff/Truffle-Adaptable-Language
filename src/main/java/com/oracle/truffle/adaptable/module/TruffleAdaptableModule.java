package com.oracle.truffle.adaptable.module;

import com.oracle.truffle.adaptable.language.AdaptationContext;
import com.oracle.truffle.adaptable.language.TruffleAdaptableLanguage;
import com.oracle.truffle.api.instrumentation.TruffleInstrument;

public abstract class TruffleAdaptableModule
	<Lang extends TruffleAdaptableLanguage<?>, 
	AdaptationCtx extends AdaptationContext<Lang>> 
		extends TruffleInstrument {
	
	@Override
	final protected void onCreate(Env env) {
		AdaptationCtx adaptationContext = Lang.<AdaptationCtx>getAdaptationContext();
	}
	
}
