package com.daftmobile.efekt

import io.kotest.matchers.collections.shouldHaveSingleElement
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.test.Test
import kotlin.test.assertEquals


class ReducerScopeTest {

    private val stateProperty = MutableStateFlow(1).asStateProperty()
    private val context = Context("A")
    private val reducerScope = ReducerScope(stateProperty, context)
    private val effectA = Effect { }
    private val effectB = Effect { }

    @Test
    fun testProvidesGivenStateProperty() {
        assertEquals(stateProperty, reducerScope.state)
    }

    @Test
    fun testProvidesGivenContext() {
        assertEquals(context, reducerScope.context)
    }

    @Test
    fun testQueuesEffects() {
        reducerScope.queue(effectA)
        reducerScope.queue(effectB)
        reducerScope.effects shouldBe listOf(effectA, effectB)
    }

    @Test
    fun testQueuesVarargEffects() {
        reducerScope.queue(effectA, effectB)
        reducerScope.effects shouldBe listOf(effectA, effectB)
    }

    @Test
    fun testQueuesIterableEffects() {
        reducerScope.queue(listOf(effectA, effectB))
        reducerScope.effects shouldBe listOf(effectA, effectB)

    }

    @Test
    fun testQueueActionAsActionsEffect() {
        reducerScope.queue(Action("A"))
        reducerScope.effects shouldHaveSingleElement Action("A").toEffect()
    }

    @Test
    fun testQueuesVarargActionsAsActionsEffect() {
        reducerScope.queue(Action("A"), Action("B"))
        reducerScope.effects shouldHaveSingleElement  listOf(Action("A"), Action("B")).toEffect()
    }

    @Test
    fun testQueuesIterableActionsAsActionsEffect() {
        reducerScope.queue(listOf(Action("A"), Action("B")))
        reducerScope.effects shouldHaveSingleElement listOf(Action("A"), Action("B")).toEffect()
    }
}
