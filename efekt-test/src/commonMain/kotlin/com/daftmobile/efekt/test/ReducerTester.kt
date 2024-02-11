package com.daftmobile.efekt.test

import com.daftmobile.efekt.Action
import com.daftmobile.efekt.Effect
import com.daftmobile.efekt.EmptyStateContext
import com.daftmobile.efekt.Reducer
import com.daftmobile.efekt.ReducerScope
import com.daftmobile.efekt.StateContext
import com.daftmobile.efekt.StateProperty
import com.daftmobile.efekt.getValue
import com.daftmobile.efekt.setValue

public interface ReducerTester<T> {

    public fun test(): ReducerTesterScope<T>

    public fun test(block: ReducerTesterScope<T>.() -> Unit): ReducerTesterScope<T> = test().apply(block)
}

internal fun <T> Reducer<T>.tester(
    initialState: T,
    initialContext: StateContext = EmptyStateContext
): ReducerTester<T> = ReducerTesterImpl(this, initialState, initialContext)

internal fun Reducer<Nothing?>.tester(
    initialContext: StateContext = EmptyStateContext
): ReducerTester<Nothing?> = ReducerTesterImpl(this, null, initialContext)

public interface ReducerTesterScope<T> {

    public var context: StateContext
    public var state: T
    public val effects: List<Effect>

    public fun testExecute(action: Action)

    public fun clearEffects()
}

private class ReducerTesterImpl<T>(
    private val reducer: Reducer<T>,
    private val initialState: T,
    private val initialContext: StateContext
) : ReducerTester<T> {

    override fun test(): ReducerTesterScope<T> = ReducerTesterScopeImpl(reducer, initialState, initialContext)
}

private class ReducerTesterScopeImpl<T>(
    private val reducer: Reducer<T>,
    state: T,
    override var context: StateContext
) : ReducerTesterScope<T> {

    private val property = stateProperty(state)
    override var state: T by property
    override val effects = mutableListOf<Effect>()

    override fun testExecute(action: Action) {
        val scope = ReducerScope(property, context)
        scope.reducer(action)
        effects.addAll(scope.effects)
    }

    override fun clearEffects() {
        effects.clear()
    }
}

private fun <T> stateProperty(value: T): StateProperty<T> = object : StateProperty<T> {
    override var value: T = value
}
