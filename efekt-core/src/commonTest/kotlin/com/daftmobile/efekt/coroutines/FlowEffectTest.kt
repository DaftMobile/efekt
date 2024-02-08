package com.daftmobile.efekt.coroutines

import com.daftmobile.efekt.Action
import com.daftmobile.efekt.receiveWith
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class FlowEffectTest {

    private val testFlow = flow {
        emit(Action("A"))
        delay(500)
        emit(Action("B"))
        delay(500)
        emit(Action("C"))
    }
    private val flowEffect = FlowEffect { testFlow }
    private val flowEffectWithToken = FlowEffect(coroutineToken = "Test token") { testFlow }
    private val jobRegistry = defaultJobRegistry()

    @Test
    fun testDispatchesEmittedActions() {
        val actions = buildList {
            runTest {
                flowEffect.receiveWith(this.toEffectCoroutineScope(), ::add)
            }
        }
        assertEquals(listOf(Action("A"), Action("B"), Action("C")), actions)
    }

    @Test
    fun testJobRegistryContainsTokenWhenEffectHasToken() = runTest {
        val context = this.toEffectCoroutineScope() + jobRegistry
        flowEffectWithToken.receiveWith(context) {}
        assertNotNull(context.jobRegistry.get("Test token"))
    }
}
