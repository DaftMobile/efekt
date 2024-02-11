package com.daftmobile.efekt.test

import com.daftmobile.efekt.Action
import com.daftmobile.efekt.Effect
import com.daftmobile.efekt.Reducer
import com.daftmobile.efekt.ReducerScope
import com.daftmobile.efekt.StateContext
import dev.mokkery.answering.calls
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.ArgMatchersScope
import dev.mokkery.matcher.any
import dev.mokkery.matcher.matching
import dev.mokkery.mock
import dev.mokkery.verify
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class ReducerTesterTest {

    private val initialContext = mockContext()

    private val reducer = mock<Reducer<Int>> {
        every { invoke(any(), any()) } returns Unit
    }
    private val tester = reducer.tester(3, initialContext)

    @Test
    fun testProvidesInitialStateInTestBlock() {
        tester.test { state shouldBe 3 }
    }

    @Test
    fun testProvidesInitialContextInTestBlock() {
        tester.test { context shouldBe initialContext }
    }

    @Test
    fun testMutatesStateInTestBlock() {
        tester.test {
            state = 2
            state shouldBe 2
        }
    }

    @Test
    fun testMutatesContextInTestBlock() {
        tester.test {
            val newContext = mockContext()
            context = newContext
            context shouldBe newContext
        }
    }

    @Test
    fun testPassesActionToReducerUnderTest() {
        val testAction = mock<Action>()
        tester.test { testExecute(testAction) }
        verify {
            reducer(any(), testAction)
        }
    }

    @Test
    fun testProvidesScopeWithUpdatedDataToReducerUnderTest() {
        val newContext = mockContext()
        tester.test {
            state = 2
            context = newContext
            testExecute(mock<Action>())
        }
        verify {
            reducer(isReducerScopeWith(2, newContext), any())
        }
    }

    @Test
    fun testCollectsEffectsFromReducerUnderTest() {
        val effect = mock<Effect>()
        every { reducer(any(), any()) } calls { (scope: ReducerScope<Int>) -> scope.queue(effect) }
        tester.test {
            testExecute(mock())
            effects shouldBe listOf(effect)
        }
    }

    @Test
    fun testClearsEffectsCollectedFromReducerUnderTest() {
        every { reducer(any(), any()) } calls { (scope: ReducerScope<Int>) -> scope.queue(mock()) }
        tester.test {
            testExecute(mock())
            clearEffects()
            effects.shouldBeEmpty()
        }
    }

    private fun <T> ArgMatchersScope.isReducerScopeWith(state: T, context: StateContext): ReducerScope<T> = matching {
        it.state.value == state && it.context == context
    }
}
