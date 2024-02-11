package com.daftmobile.efekt.test

import com.daftmobile.efekt.Action
import com.daftmobile.efekt.Effect
import com.daftmobile.efekt.ListEffect
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.test.assertIs
import kotlin.test.assertTrue

public inline fun <reified T : Effect> ReducerTesterScope<*>.shouldHaveSingleEffectOf(): T {
    assertHaveSingleEffect()
    return assertIs(effects.single())
}

public inline fun <reified T : Action> ListEffect?.shouldHaveSingleActionOf(): T {
    assertHaveSingleAction()
    return assertIs(actions.single())
}

public inline fun <reified T : Action> Effect?.shouldBeSingleActionOf(): T = assertIs<ListEffect>(this)
    .run { shouldHaveSingleActionOf() }

public inline fun <reified T : Action> ReducerTesterScope<*>.shouldDispatchSingleAction(block: (T) -> Unit = { }): T {
    return shouldHaveSingleEffectOf<ListEffect>()
        .shouldHaveSingleActionOf<T>()
        .apply(block)
}

@PublishedApi
internal fun ReducerTesterScope<*>.assertHaveSingleEffect() {
    assertTrue(actual = effects.size == 1, message = "Expected single effect! Effects: $effects")
}

@OptIn(ExperimentalContracts::class)
@PublishedApi
internal fun ListEffect?.assertHaveSingleAction() {
    contract { returns() implies (this@assertHaveSingleAction != null) }
    assertTrue(actual = this?.actions?.size == 1, message = "Expected single action! Effects: ${this?.actions}")
}

