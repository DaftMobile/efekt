package com.daftmobile.efekt

public typealias Reducer<T> = ReducerScope<T>.(action: Action) -> Unit

public inline fun <T, reified A : Action> typedReducer(
    crossinline block: ReducerScope<T>.(action: A) -> Unit
): Reducer<T> = {
    if (it is A) block(it)
}

public inline fun <reified A : Action> reducer(
    crossinline block: ReducerScope<Nothing?>.(action: A) -> Unit
): Reducer<Nothing?> = {
    if (it is A) block(it)
}

public inline fun <T> combine(vararg reducers: Reducer<T>): Reducer<T> = { action ->
    reducers.forEach {
        it.invoke(this, action)
    }
}

public fun <T, R> Reducer<R>.scoped(get: T.() -> R, set: T.(R) -> T): Reducer<T> = { action ->
    val scope = ReducerScope(state.map(get, set), context)
    invoke(scope, action)
    queue(scope.effects)
}

public fun <T> Reducer<Nothing?>.stateless(): Reducer<T> = { action ->
    val scope = ReducerScope(EmptyStateProperty, context)
    invoke(scope, action)
    queue(scope.effects)
}
