package com.daftmobile.efekt

import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
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
        result[Context.Key("A")] shouldBe contextA
        result[Context.Key("B")] shouldBe contextB
    }

    @Test
    fun testSumOfContextElementsScattersIntoSeparateElements() {
        val contextA = Context("A")
        val contextB = Context("B")
        (contextA + contextB).scatter() shouldBe  mapOf(
            Context.Key("A") to contextA,
            Context.Key("B") to contextB,
        )
    }

    @Test
    fun testElementsFromTheRightSideOverrideElementsOnTheLeftSideInSum() {
        val contextA1 = Context("A")
        val contextB = Context("B")
        val contextA2 = Context("A")
        val result = contextA1 + contextB + contextA2
        result[Context.Key("A")] shouldBe contextA2
    }

    @Test
    fun testMinusRemovesElementFormTheContext() {
        val contextA = Context("A")
        val contextB = Context("B")
        val contextC = Context("C")
        val result = contextA + contextB + contextC - Context.Key("A")
        result
            .find(Context.Key("A"))
            .shouldBeNull()
    }

    @Test
    fun testMinusRemovesElementFrom2ElementsContextOptimizesToSingleElement() {
        val contextA = Context("A")
        val contextB = Context("B")
        val result = contextA + contextB - Context.Key("A")
        result shouldBe contextB
    }

    @Test
    fun testMinusRemovesElementFromSingleElementContextOptimizesEmptyContext() {
        val result = Context("A") - Context.Key("A")
        result shouldBe EmptyStateContext
    }

    @Test
    fun testMinusAllowsMissingKey() {
        val result = Context("A") - Context.Key("B")
        result.find(Context.Key("A")).shouldNotBeNull()
    }
}
