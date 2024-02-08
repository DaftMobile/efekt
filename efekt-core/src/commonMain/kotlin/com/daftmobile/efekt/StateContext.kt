package com.daftmobile.efekt

public interface StateContext {

    public fun <T : Element> find(key: Key<T>): T?

    public operator fun <T : Element> get(key: Key<T>): T = find(key) ?: throw MissingClosureElementException(key)

    public operator fun plus(
        closure: StateContext
    ): StateContext = combinedStateContextOf(scatter() + closure.scatter())

    public operator fun minus(key: Key<*>): StateContext

    public fun scatter(): Map<Key<*>, Element>

    public interface Element : StateContext {

        public val key: Key<*>

        @Suppress("UNCHECKED_CAST")
        override fun <T : Element> find(key: Key<T>): T? = if (this.key == key) this as T else null

        override fun <T : Element> get(key: Key<T>): T = find(key) ?: throw MissingClosureElementException(key)

        override fun minus(key: Key<*>): StateContext = if (this.key == key) EmptyStateContext else this

        override fun scatter(): Map<Key<*>, Element> = mapOf(key to this)
    }

    public interface Key<T : Element>
}

internal class MissingClosureElementException(
    key: StateContext.Key<*>
) : IllegalStateException("Element with a key=${key} is not part of a StateContext!")

internal fun combinedStateContextOf(
    elements: Map<StateContext.Key<*>, StateContext.Element>
): StateContext = when (elements.values.size) {
    0 -> EmptyStateContext
    1 -> elements.values.single()
    else -> CombinedStateContext(elements)
}

private class CombinedStateContext(
    private val elements: Map<StateContext.Key<*>, StateContext.Element>
) : StateContext {

    init {
        require(elements.isNotEmpty()) { "Combined StateContext cannot be empty!" }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : StateContext.Element> find(key: StateContext.Key<T>): T? = elements[key] as? T

    override fun minus(key: StateContext.Key<*>): StateContext = combinedStateContextOf(elements - key)

    override fun scatter(): Map<StateContext.Key<*>, StateContext.Element> = elements

    override fun toString(): String = elements.values.joinToString(separator = "\n + ")
}
