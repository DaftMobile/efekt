package com.daftmobile.efekt

import dev.mokkery.answering.calls
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.test.Test
import kotlin.test.assertEquals


class CombineReducerTest {

    private val context = Context("A")
    private val stateProperty = MutableStateFlow(1).asStateProperty()
    private val scope = ReducerScope(stateProperty, context)
    private val reducer1 = mock<Reducer<Int>> {
        every { invoke(any(), any()) } calls { (scope: ReducerScope<Int>, action: Action) ->
            if (action == Action("A")) scope.queue(Action("Effect1"))
        }
    }
    private val reducer2 = mock<Reducer<Int>> {
        every { invoke(any(), any()) } calls { (scope: ReducerScope<Int>, action: Action) ->
            if (action == Action("A")) scope.queue(Action("Effect2"))
        }
    }
    private val reducer3 = mock<Reducer<Int>> {
        every { invoke(any(), any()) } calls { (scope: ReducerScope<Int>, action: Action) ->
            if (action == Action("A")) scope.queue(Action("Effect3"))
        }
    }
    private val combinedReducer = combine(reducer1, reducer2, reducer3)

    @Test
    fun testCombinedReducerShouldCombineEffects() {
        combinedReducer.invoke(scope, Action("A"))
        assertEquals(
            expected = listOf(Action("Effect1"), Action("Effect2"), Action("Effect3"))
                .map { it.toEffect() },
            actual = scope.effects
        )
    }

    @Test
    fun testCallsAllCombinedReducersWithProperActionAndScope() {
        combinedReducer(scope, Action("A"))
        verify {
            reducer1(scope, Action("A"))
            reducer2(scope, Action("A"))
            reducer3(scope, Action("A"))
        }
    }
}
