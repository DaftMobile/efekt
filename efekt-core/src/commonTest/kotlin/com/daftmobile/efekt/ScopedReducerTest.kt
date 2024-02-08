package com.daftmobile.efekt

import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.test.Test
import kotlin.test.assertEquals

class ScopedReducerTest {

    private val intReducer: Reducer<Int> = {
        state.value++
        if (it == Action("B")) queue(Action("EffectA"))
    }
    private val reducer: Reducer<State> = intReducer.scoped(State::counter) { copy(counter = it) }
    private val state = MutableStateFlow(State()).asStateProperty()

    @Test
    fun testScopedReducerUpdatesPartOfTheState() {
        val scope = ReducerScope(state)
        state.value = State(counter = 1)
        reducer.invoke(scope, Action("B"))
        assertEquals(State(counter = 2), state.value)
    }

    @Test
    fun testQueuesEffectProperly() {
        val scope = ReducerScope(state)
        reducer.invoke(scope, Action("B"))
        assertEquals(
            expected = listOf(Action("EffectA").toEffect()),
            actual = scope.effects
        )
    }

    private data class State(val counter: Int = 0)
}
