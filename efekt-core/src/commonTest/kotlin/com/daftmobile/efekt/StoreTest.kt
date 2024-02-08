package com.daftmobile.efekt

import app.cash.turbine.test
import dev.mokkery.answering.calls
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.ArgMatchersScope
import dev.mokkery.matcher.any
import dev.mokkery.matcher.matching
import dev.mokkery.mock
import dev.mokkery.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals


class StoreTest {

    private val reducer = mock<Reducer<Int>> { }
    private val initialContext = Context("A")
    private val initialState = 1
    private val store = Store(initialState, reducer, initialContext)

    @Test
    fun testDispatchesActionsFromEffects() {
        every { reducer(any(), any()) } calls { (scope: ReducerScope<Int>, action: Action) ->
            if (action == Action("A")) scope.queue(Action("B"))
        }
        store.dispatch(Action("A"))
        verify {
            reducer(any(), Action("A"))
            reducer(any(), Action("B"))
        }
    }

    @Test
    fun testCallsReducerWithScopeThatProvidesInitialStateAndContext() {
        every { reducer(any(), any()) } returns Unit
        store.dispatch(Action("A"))
        verify { reducer(isValidReducerScope(), Action("A")) }
    }

    @Test
    fun testDelegatesReducerStateUpdatesToStateFlow() = runTest {
        every { reducer(any(), any()) } calls  { (scope: ReducerScope<Int>) -> scope.state.value = 2 }
        store.state.test {
            assertEquals(1, awaitItem())
            store.dispatch(Action("A"))
            assertEquals(2, awaitItem())
        }
    }

    private fun ArgMatchersScope.isValidReducerScope(): ReducerScope<Int> {
        return matching<ReducerScope<Int>>(toString = { "isValidReducerScope()" }) {
            it.state.value == 1 && it.context == initialContext
        }
    }
}
