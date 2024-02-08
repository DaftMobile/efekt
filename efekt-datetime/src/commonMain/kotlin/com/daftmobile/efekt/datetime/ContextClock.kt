package com.daftmobile.efekt.datetime

import com.daftmobile.efekt.StateContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

public val StateContext.clock: Clock get() = get(ContextClock)

public fun Clock.toContextClock(): StateContext = if (this is ContextClock) this else ContextClock(this)

public fun Instant.toContextClock(): StateContext = ConstClock(this).toContextClock()

private class ContextClock(
    private val clock: Clock,
) : StateContext.Element, Clock by clock {

    override fun toString(): String = "ContextClock($clock)"

    override val key = Key

    companion object Key : StateContext.Key<ContextClock>
}

private class ConstClock(private val date: Instant) : Clock {

    override fun now(): Instant = date
}
