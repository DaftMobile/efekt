package com.daftmobile.efekt.coroutines

import com.daftmobile.efekt.Dispatch
import com.daftmobile.efekt.Effect
import com.daftmobile.efekt.StateContext
import com.daftmobile.efekt.receiveWith
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch

public data class CombinedEffect(
    val effects: List<Effect>,
) : Effect {

    override fun StateContext.receive(dispatch: Dispatch) {
        effectCoroutineScope.launch(start = CoroutineStart.UNDISPATCHED) {
            effects.forEach { effect ->
                if (effect is FlowEffect) effect.flow(this@receive).collect(dispatch)
                else effect.receiveWith(this@receive, dispatch)
            }
        }
    }
}

public operator fun Effect.plus(effect: Effect): Effect = CombinedEffect(this.unwrap() + effect.unwrap())

private inline fun Effect.unwrap(): List<Effect> = if (this is CombinedEffect) effects else listOf(this)
