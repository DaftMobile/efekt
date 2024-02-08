package com.daftmobile.efekt

public fun interface Effect {

    public fun StateContext.receive(dispatch: Dispatch)
}

public data class ListEffect(
    val actions: List<Action>
) : Effect {
    override fun StateContext.receive(dispatch: Dispatch) {
        actions.forEach(dispatch)
    }
}

public open class SequenceEffect(
    public val sequence: StateContext.() -> Sequence<Action>
) : Effect {

    override fun StateContext.receive(dispatch: Dispatch) {
        sequence().forEach(dispatch)
    }
}

internal fun Effect.receiveWith(context: StateContext = EmptyStateContext, dispatch: Dispatch) {
    context.receive(dispatch)
}
