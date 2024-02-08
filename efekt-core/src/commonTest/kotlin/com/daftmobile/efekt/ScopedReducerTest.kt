package com.daftmobile.efekt

import io.kotest.matchers.collections.shouldHaveSingleElement
import io.kotest.matchers.shouldBe
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
        state.value shouldBe State(counter = 2)
    }

    @Test
    fun testQueuesEffectProperly() {
        val scope = ReducerScope(state)
        reducer.invoke(scope, Action("B"))
        scope.effects shouldHaveSingleElement Action("EffectA").toEffect()
    }

    private data class State(val counter: Int = 0)
}
