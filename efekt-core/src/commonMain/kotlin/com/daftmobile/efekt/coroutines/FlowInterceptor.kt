package com.daftmobile.efekt.coroutines

import com.daftmobile.efekt.StateContext
import kotlinx.coroutines.flow.Flow

public interface FlowInterceptorApplier : StateContext.Element {

    public override val key: Key get() = Key

    public fun <T, R> applyInterceptor(flow: Flow<T>, interceptor: FlowInterceptor<T, R>): Flow<R>

    public companion object Key : StateContext.Key<FlowInterceptorApplier>
}

public fun enabledFlowInterceptorApplier(): FlowInterceptorApplier = FlowInterceptorApplierImpl()

public fun <T, R> Flow<T>.intercept(context: StateContext, interceptor: FlowInterceptor<T, R>): Flow<R> {
    return context[FlowInterceptorApplier].applyInterceptor(this, interceptor)
}

public interface FlowInterceptor<T, R> {

    public fun intercept(flow: Flow<T>): Flow<R>
}

private class FlowInterceptorApplierImpl : FlowInterceptorApplier {

    override fun <T, R> applyInterceptor(
        flow: Flow<T>,
        interceptor: FlowInterceptor<T, R>
    ) = interceptor.intercept(flow)
    
}
