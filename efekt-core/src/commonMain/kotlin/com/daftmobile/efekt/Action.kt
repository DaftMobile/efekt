package com.daftmobile.efekt

public interface Action

public fun Action.toEffect(): Effect = ListEffect(listOf(this))

public fun List<Action>.toEffect(): Effect = ListEffect(this)
