package com.daftmobile.efekt.datetime

import com.daftmobile.efekt.StateContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.TimeSource

public val StateContext.clock: Clock get() = get(ContextClock)

public fun Clock.toContextClock(): StateContext = if (this is ContextClock) this else ContextClock(this)

public fun Instant.toContextClock(): StateContext = ConstClock(this).toContextClock()

public fun emulatedClockContext(startingAt: Instant): StateContext = EmulatedClock(startingAt).toContextClock()

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

private class EmulatedClock(
    private val startingAt: Instant,
    timeSource: TimeSource = TimeSource.Monotonic
) : Clock {

    private val startMark = timeSource.markNow()

    override fun now(): Instant = startingAt + startMark.elapsedNow()
}
