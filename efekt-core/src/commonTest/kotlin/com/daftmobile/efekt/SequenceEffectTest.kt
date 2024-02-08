package com.daftmobile.efekt

import kotlin.test.Test
import kotlin.test.assertEquals

class SequenceEffectTest {

    private val effect = SequenceEffect {
        sequence {
            yield(Action("A"))
            yield(Action("B"))
            yield(Action("C"))
        }
    }

    @Test
    fun testDispatchesYieldedActions() {
        val actions = buildList { effect.receiveWith(dispatch = ::add) }
        assertEquals(listOf(Action("A"), Action("B"), Action("C")), actions)
    }
}
