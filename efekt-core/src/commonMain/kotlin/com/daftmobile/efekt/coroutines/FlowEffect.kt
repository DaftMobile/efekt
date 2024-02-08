package com.daftmobile.efekt.coroutines

import com.daftmobile.efekt.Action
import com.daftmobile.efekt.Dispatch
import com.daftmobile.efekt.Effect
import com.daftmobile.efekt.StateContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

public open class FlowEffect(
    private val coroutineToken: Any?,
    public val flow: StateContext.() -> Flow<Action>,
) : Effect {

    public constructor(flow: StateContext.() -> Flow<Action>) : this(coroutineToken = null, flow = flow)

    override fun StateContext.receive(dispatch: Dispatch) {
        val job = flow().onEach(dispatch).launchIn(effectCoroutineScope)
        if (coroutineToken != null) jobRegistry.register(coroutineToken, job)
    }
}
