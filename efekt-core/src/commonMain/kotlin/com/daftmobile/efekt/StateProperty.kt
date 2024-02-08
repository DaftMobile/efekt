package com.daftmobile.efekt

import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.reflect.KProperty

public interface StateProperty<T> {

    public var value: T
}

public inline fun <T> StateProperty<T>.update(transform: (T) -> T) {
    value = transform(value)
}

public inline fun <T> StateProperty<List<T>>.mutate(transform: (MutableList<T>) -> Unit) {
    update { it.toMutableList().apply(transform) }
}

public operator fun <T> StateProperty<T>.getValue(thisRef: Any?, property: KProperty<*>): T = value

public operator fun <T> StateProperty<T>.setValue(thisRef: Any?, property: KProperty<*>, value: T) {
    this.value = value
}

public fun <T> MutableStateFlow<T>.asStateProperty(): StateProperty<T> = MutableStateFlowProperty(this)

public fun <T, R> StateProperty<T>.map(
    get: T.() -> R,
    apply: T.(R) -> T
): StateProperty<R> = MapProperty(this, get, apply)

public data object EmptyStateProperty : StateProperty<Nothing?> {

    override var value: Nothing?
        get() = null
        set(_) {

        }
}

private class MutableStateFlowProperty<T>(
    private val stateFlow: MutableStateFlow<T>
) : StateProperty<T> {

    override var value: T
        get() = stateFlow.value
        set(value) {
            stateFlow.value = value
        }
}

private class MapProperty<T, R>(
    private val property: StateProperty<T>,
    private val get: T.() -> R,
    private val apply: T.(R) -> T,
) : StateProperty<R> {

    override var value: R
        get() = property.value.let(get)
        set(value) {
            property.value = apply(property.value, value)
        }
}
