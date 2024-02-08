package com.daftmobile.efekt

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
        assertEquals(listOf(effectA, effectB), reducerScope.effects)
    }

    @Test
    fun testQueuesVarargEffects() {
        reducerScope.queue(effectA, effectB)
        assertEquals(listOf(effectA, effectB), reducerScope.effects)
    }

    @Test
    fun testQueuesIterableEffects() {
        reducerScope.queue(listOf(effectA, effectB))
        assertEquals(listOf(effectA, effectB), reducerScope.effects)

    }

    @Test
    fun testQueueActionAsActionsEffect() {
        reducerScope.queue(Action("A"))
        assertEquals(listOf(Action("A").toEffect()), reducerScope.effects)
    }

    @Test
    fun testQueuesVarargActionsAsActionsEffect() {
        reducerScope.queue(Action("A"), Action("B"))
        assertEquals(
            expected = listOf(listOf(Action("A"), Action("B")).toEffect()),
            actual = reducerScope.effects
        )
    }

    @Test
    fun testQueuesIterableActionsAsActionsEffect() {
        reducerScope.queue(listOf(Action("A"), Action("B")))
        assertEquals(listOf(listOf(Action("A"), Action("B")).toEffect()), reducerScope.effects)
    }
}
