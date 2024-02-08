package com.daftmobile.efekt.coroutines

import com.daftmobile.efekt.Action
import com.daftmobile.efekt.StateContext
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

public open class FlowEffectBuilder(block: suspend FlowEffectBuilderScope.() -> Unit) : FlowEffect(scope@{
    flow {
        block(FlowEffectBuilderScopeImpl(collector = this, context = this@scope))
    }
})

public interface FlowEffectBuilderScope : FlowCollector<Action>, StateContext

private class FlowEffectBuilderScopeImpl(
    collector: FlowCollector<Action>,
    context: StateContext,
) : FlowEffectBuilderScope, FlowCollector<Action> by collector, StateContext by context
