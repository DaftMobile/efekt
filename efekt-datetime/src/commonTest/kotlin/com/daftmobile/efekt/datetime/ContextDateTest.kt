package com.daftmobile.efekt.datetime

import io.kotest.matchers.shouldBe
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class ContextDateTest {

    @Test
    fun testCurrentDateReturnsLocalDateBasedOnClockAndTimeZone() {
        val instant = Instant.fromEpochMilliseconds((3.days - 30.minutes).inWholeMilliseconds)
        val timeZone = TimeZone.of("GMT+1")
        val context = instant.toContextClock() + timeZone.toContextTimeZone()
        context.currentDate shouldBe LocalDate.fromEpochDays(3)
    }

    @Test
    fun testCurrentDateReturnsLocalDateTimeBasedOnClockAndTimeZone() {
        val instant = Instant.fromEpochMilliseconds((3.days - 30.minutes).inWholeMilliseconds)
        val timeZone = TimeZone.of("GMT+2")
        val context = instant.toContextClock() + timeZone.toContextTimeZone()
        context.currentDateTime shouldBe LocalDateTime(
            LocalDate.fromEpochDays(3),
            LocalTime.fromSecondOfDay((1.hours + 30.minutes).inWholeSeconds.toInt())
        )
    }

    @Test
    fun testCurrentTimeReturnsLocalTimeBasedOnClockAndTimeZone() {
        val instant = Instant.fromEpochMilliseconds((3.days - 30.minutes).inWholeMilliseconds)
        val timeZone = TimeZone.of("GMT+3")
        val context = instant.toContextClock() + timeZone.toContextTimeZone()
        context.currentTime shouldBe LocalTime.fromSecondOfDay((2.hours + 30.minutes).inWholeSeconds.toInt())
    }
}
