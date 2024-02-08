package com.daftmobile.efekt

fun Action(name: String): Action = NamedAction(name)

class Context(name: String) : StateContext.Element {

    override val key = Key(name)

    data class Key(val name: String) : StateContext.Key<Context>
}

private data class NamedAction(val name: String) : Action {

    override fun toString(): String = "Action($name)"
}
