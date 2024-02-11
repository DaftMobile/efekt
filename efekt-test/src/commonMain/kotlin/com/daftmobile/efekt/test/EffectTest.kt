package com.daftmobile.efekt.test

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import app.cash.turbine.testIn
import com.daftmobile.efekt.Action
import com.daftmobile.efekt.EmptyStateContext
import com.daftmobile.efekt.StateContext
import com.daftmobile.efekt.coroutines.FlowEffect
import com.daftmobile.efekt.coroutines.FlowInterceptor
import com.daftmobile.efekt.coroutines.FlowInterceptorApplier
import kotlinx.coroutines.CoroutineScope

internal suspend inline fun FlowEffect.test(
    context: StateContext = EmptyStateContext,
    crossinline block: suspend ReceiveTurbine<Action>.() -> Unit
) = flow(context).test { block() }

internal fun FlowEffect.testIn(context: StateContext, scope: CoroutineScope) = flow(context).testIn(scope)

public fun testFlowEffectContext(catchErrors: Boolean = false): StateContext = TestFlowInterceptorApplier(catchErrors)

public val StateContext.appliedInterceptors: List<FlowInterceptor<*, *>>
    get() = testFlowInterceptorApplier.applied

public val StateContext.errorsPropagatedToInterceptor: List<Throwable>
    get() = testFlowInterceptorApplier.errors

public val StateContext.testFlowInterceptorApplier: TestFlowInterceptorApplier
    get() = get(FlowInterceptorApplier) as TestFlowInterceptorApplier

