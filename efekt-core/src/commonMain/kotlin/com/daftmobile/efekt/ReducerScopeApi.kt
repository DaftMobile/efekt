package com.daftmobile.efekt

public fun ReducerScope<*>.queue(action: Action): Unit = queue(action.toEffect())

public fun ReducerScope<*>.queue(vararg actions: Action): Unit = queue(actions.toList().toEffect())

public fun ReducerScope<*>.queue(actions: List<Action>): Unit = queue(actions.toEffect())

public fun ReducerScope<*>.queue(effects: Iterable<Effect>): Unit = effects.forEach(::queue)

public fun ReducerScope<*>.queue(vararg effects: Effect): Unit = effects.forEach(::queue)
