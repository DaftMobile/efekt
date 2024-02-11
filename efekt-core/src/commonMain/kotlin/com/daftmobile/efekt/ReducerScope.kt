package com.daftmobile.efekt

public interface ReducerScope<T> {

    public val context: StateContext
    public val state: StateProperty<T>
    public val effects: List<Effect>

    public fun queue(effect: Effect)
}

public fun <T> ReducerScope(
    state: StateProperty<T>,
    context: StateContext = EmptyStateContext,
): ReducerScope<T> = ReducerScopeImpl(state, context)

private class ReducerScopeImpl<T>(
    override val state: StateProperty<T>,
    override val context: StateContext,
) : ReducerScope<T> {

    override val effects = mutableListOf<Effect>()

    override fun queue(effect: Effect) {
        effects.add(effect)
    }
}
