package com.daftmobile.efekt

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class StateContextTest {

    @Test
    fun testSumOfContextElementsReturnsSingleContextElementThatContainsAllElements() {
        val contextA = Context("A")
        val contextB = Context("B")
        val result = contextA + contextB
        assertEquals(contextA, result[Context.Key("A")])
        assertEquals(contextB, result[Context.Key("B")])
    }

    @Test
    fun testSumOfContextElementsScattersIntoSeparateElements() {
        val contextA = Context("A")
        val contextB = Context("B")
        assertEquals<Map<StateContext.Key<*>, StateContext.Element>>(
            expected = mapOf(
                Context.Key("A") to contextA,
                Context.Key("B") to contextB,
            ),
            actual = (contextA + contextB).scatter()
        )
    }

    @Test
    fun testElementsFromTheRightSideOverrideElementsOnTheLeftSideInSum() {
        val contextA1 = Context("A")
        val contextB = Context("B")
        val contextA2 = Context("A")
        val result = contextA1 + contextB + contextA2
        assertEquals(contextA2, result[Context.Key("A")])
    }

    @Test
    fun testMinusRemovesElementFormTheContext() {
        val contextA = Context("A")
        val contextB = Context("B")
        val contextC = Context("C")
        val result = contextA + contextB + contextC - Context.Key("A")
        assertNull(result.find(Context.Key("A")))
    }

    @Test
    fun testMinusRemovesElementFrom2ElementsContextOptimizesToSingleElement() {
        val contextA = Context("A")
        val contextB = Context("B")
        val result = contextA + contextB - Context.Key("A")
        assertEquals(contextB, result)
    }

    @Test
    fun testMinusRemovesElementFromSingleElementContextOptimizesEmptyContext() {
        val result = Context("A") - Context.Key("A")
        assertEquals(EmptyStateContext, result)
    }

    @Test
    fun testMinusAllowsMissingKey() {
        val result = Context("A") - Context.Key("B")
        assertNotNull(result.find(Context.Key("A")))
    }
}
