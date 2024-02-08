package com.daftmobile.efekt.coroutines

import com.daftmobile.efekt.StateContext
import kotlinx.coroutines.CoroutineScope

public val StateContext.effectCoroutineScope: CoroutineScope get() = get(EffectCoroutineScope)

public fun CoroutineScope.toEffectCoroutineScope(): StateContext = when (this) {
    is EffectCoroutineScope -> this
    else -> EffectCoroutineScope(this)
}

private class EffectCoroutineScope(
    private val scope: CoroutineScope
) : StateContext.Element, CoroutineScope by scope {

    override fun toString(): String = "EffectCoroutineScope($scope)"

    override val key = Key

    companion object Key : StateContext.Key<EffectCoroutineScope>
}
