package com.daftmobile.efekt.coroutines

import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FlowInterceptorApplierTest {

    private val interceptor = mock<FlowInterceptor<Int, String>> {
        every { intercept(any()) } returns flowOf("1", "2", "3")
    }
    private val interceptorApplier = enabledFlowInterceptorApplier()
    private val flow = flowOf(1, 2, 3)

    @Test
    fun testAppliesInterceptorToFlow() {
        interceptorApplier.applyInterceptor(flow, interceptor)
        verify { interceptor.intercept(flow) }
    }

    @Test
    fun testReturnsInterceptedFlow() = runTest {
        interceptorApplier.applyInterceptor(flow, interceptor).toList() shouldBe listOf("1", "2", "3")
    }
}
