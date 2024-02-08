package com.daftmobile.efekt.datetime

import com.daftmobile.efekt.StateContext
import kotlinx.datetime.TimeZone

public val StateContext.timeZone: TimeZone get() = get(ContextTimeZone).get()

public fun currentSystemDefaultTimeZoneContext(): StateContext = CurrentSystemDefaultTimeZone

public fun TimeZone.toContextTimeZone(): StateContext = ConstantTimeZone(this)

internal interface ContextTimeZone : StateContext.Element {

    override val key get() = Key

    fun get(): TimeZone

    companion object Key : StateContext.Key<ContextTimeZone>
}

internal data object CurrentSystemDefaultTimeZone : ContextTimeZone {

    override fun get(): TimeZone = TimeZone.currentSystemDefault()
}

internal data class ConstantTimeZone(val zone: TimeZone) : ContextTimeZone {

    override fun get() = zone
}
