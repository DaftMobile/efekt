package com.daftmobile.efekt.test

import com.daftmobile.efekt.StateContext
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock

fun mockContext(): StateContext.Element = mock { every { key } returns mock<StateContext.Key<*>>() }
