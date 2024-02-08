package com.daftmobile.efekt


public data object EmptyStateContext : StateContext {

    override fun <T : StateContext.Element> find(key: StateContext.Key<T>): Nothing? = null

    override fun <T : StateContext.Element> get(key: StateContext.Key<T>): Nothing =
        throw MissingClosureElementException(key)

    override fun plus(closure: StateContext): StateContext = closure

    override fun minus(key: StateContext.Key<*>): StateContext = this

    override fun scatter(): Map<StateContext.Key<*>, StateContext.Element> = emptyMap()

    override fun toString(): String = "EmptyStateContext"
}
