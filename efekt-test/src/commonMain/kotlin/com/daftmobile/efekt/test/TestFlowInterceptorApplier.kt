package com.daftmobile.efekt.test

import com.daftmobile.efekt.coroutines.FlowInterceptor
import com.daftmobile.efekt.coroutines.FlowInterceptorApplier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

@Suppress("UNCHECKED_CAST")
public class TestFlowInterceptorApplier(
    private val catchErrors: Boolean
) : FlowInterceptorApplier {

    private val _applied = mutableListOf<FlowInterceptor<*, *>>()
    private val _errors = mutableListOf<Throwable>()

    public val applied: List<FlowInterceptor<*, *>> get() = _applied
    public val errors: List<Throwable> get() = _errors

    override fun <T, R> applyInterceptor(flow: Flow<T>, interceptor: FlowInterceptor<T, R>): Flow<R> {
        _applied.add(interceptor)
        if (catchErrors) return flow.catch { _errors.add(it) } as Flow<R>
        return flow as Flow<R>
    }
}
