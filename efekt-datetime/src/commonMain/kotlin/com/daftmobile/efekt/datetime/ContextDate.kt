package com.daftmobile.efekt.datetime

import com.daftmobile.efekt.StateContext
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

public val StateContext.currentDateTime: LocalDateTime get() = clock.now().toLocalDateTime(timeZone)
public val StateContext.currentDate: LocalDate get() = currentDateTime.date
public val StateContext.currentTime: LocalTime get() = currentDateTime.time

public fun constDateTimeContext(
    date: LocalDate,
    time: LocalTime = LocalTime.fromSecondOfDay(0),
    zone: TimeZone = TimeZone.UTC
): StateContext = constDateTimeContext(LocalDateTime(date, time), zone)

public fun constDateTimeContext(
    dateTime: LocalDateTime,
    zone: TimeZone = TimeZone.UTC
): StateContext = dateTime.toInstant(zone).toContextClock() + zone.toContextTimeZone()
