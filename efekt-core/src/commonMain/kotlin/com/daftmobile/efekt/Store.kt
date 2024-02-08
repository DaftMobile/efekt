package com.daftmobile.efekt

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

public typealias Dispatch = (Action) -> Unit


public interface Store<T> {

    public val context: StateContext
    public val state: StateFlow<T>

    public fun dispatch(action: Action)
}

public fun <T> Store(
    initialState: T,
    reducer: Reducer<T>,
    initialContext: StateContext = EmptyStateContext
): Store<T> = StoreImpl(initialState, initialContext, reducer)

private class StoreImpl<T>(
    initialState: T,
    initialContext: StateContext,
    private val reducer: Reducer<T>,
) : Store<T> {

    override val state = MutableStateFlow(initialState)
    override val context = initialContext
    private val stateProperty = state.asStateProperty()

    override fun dispatch(action: Action) {
        ReducerScope(stateProperty, context).applyWithEffects(reducer, action)
    }

    private fun <T> ReducerScope<T>.applyWithEffects(reducer: Reducer<T>, action: Action) {
        reducer(action)
        effects.forEach { it.receiveWith(context, ::dispatch) }
    }
}
