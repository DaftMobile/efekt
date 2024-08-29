# Efekt

[![Kotlin](https://img.shields.io/badge/kotlin-2.0.20-blue.svg?logo=kotlin)](http://kotlinlang.org) 
[![GitHub](https://img.shields.io/github/license/DaftMobile/efekt)](https://github.com/DaftMobile/efekt/blob/main/LICENSE)

Efekt is a [Redux pattern](https://redux.js.org/understanding/thinking-in-redux/three-principles) adaptation in Kotlin,
integrated with [coroutines](https://github.com/Kotlin/kotlinx.coroutines), testable and multiplatform.

## Dependencies

```kotlin
dependencies {
    implementation("com.daftmobile:efekt-core:1.0.0")
    implementation("com.daftmobile:efekt-data:1.0.0")
    implementation("com.daftmobile:efekt-datetime:1.0.0")
    implementation("com.daftmobile:efekt-test:1.0.0")
}
```

## Store

The core of the library is a `Store`. 

```kotlin
public interface Store<AppState> {

    public val context: StateContext
    public val state: StateFlow<AppState>

    public fun dispatch(action: Action)
}
```

* It hosts an app state and propagates its changes with `state: StateFlow<T>`.
* Any change to the state must be triggered by an `Action` send to the `Store` with `dispatch`.
* The `Store` also provides a `StateContext`. It contains all dependencies required by the logic to change the state. 
Passing dependencies through context makes it easy to replace them in tests.

To create a `Store` use `Store` function. It requires:
* Initial app state defined by the user.
* Initial `StateContext`. It's empty for the simplicity of the example. It's covered [here](#state-context). 
* `Reducer<AppState>` that defines the store logic. It's invoked with every action dispatched. It's covered [here](#defining-logic).

```kotlin
val store = AppStore(
    iniitalState = AppState(...),
    initialContext = EmptyStateContext,
    reducer = appReducer
)

```

### Defining logic

Let's consider given app state:

```kotlin
data class AppState(
    val counter: Int
)
```

The single source of the logic is the `Reducer<AppState>`, that is passed to the `Store`.

Reducer is able to:
* Change the state
* Access `StateContext`
* Queue effects. Covered [here](#effects).

Before creating the reducer, it is necessary to define some actions:

```kotlin
sealed class CounterAction : Action {
    
    data object Increment : Action
    data object Decrement : Action
}
```

Now we can create the reducer:

```kotlin
val appReducer = typed<AppState, Action> { action ->
    when (action) {
        CounterAction.Increment -> state.update { it.copy(counter = it.counter + 1) }
        CounterAction.Decrement -> state.update { it.copy(counter = it.counter - 1) }
        else -> Unit
    }
}

val store = Store(initialState = AppState(counter = 0), reducer = appReducer)

println(store.value.counter) // prints("0")

store.dispatch(CounterAction.Increment)
println(store.value.counter) // prints("1")

store.dispatch(CounterAction.Increment)
println(store.value.counter) // prints("2")

store.dispatch(CounterAction.Decrement)
println(store.value.counter) // prints("1")
```

Having whole `appReducer` in a single place is not ideal. The `appReducer` can be combined with other reducers.
It allows splitting the logic:

```kotlin
val appReducer = combine(
    counterReducer,
    otherReducer,
)

val counterReducer = typed<AppState, CounterAction> { action ->
    // Actions are prefiltered by the type argument `CounterAction`, so only counter actions are accepted.
    when (action) {
        CounterAction.Increment -> state.update { it.copy(counter = it.counter + 1) }
        CounterAction.Decrement -> state.update { it.copy(counter = it.counter - 1) }
    }
}
```

The `combine` creates a reducer that delegates each received action to all combined reducers.

We can simplify the `counterReducer` by scoping the state:

```kotlin
val appReducer = combine(
    counterReducer.scoped(AppState::counter) { it.copy(counter = it) },
    otherReducer,
)

val counterReducer = typed<Int, CounterAction> { action ->
    when (action) {
        CounterAction.Increment -> state.update { it + 1 }
        CounterAction.Decrement -> state.update { it - 1 }
    }
}
```

Reducers should provide only synchronous logic that should be mostly [pure](https://en.wikipedia.org/wiki/Pure_function)
or at least predictable and not error-prone. For example:
* You should not access files in reducers - not pure and error-prone.
* You can access current time - not pure but predictable and not error-prone. Make sure that you access all external 
dependencies (like current time) through [context](#state-context). 

## Effects

Beside changing the state, reducer might also queue effects.

Effect is a small portion of a logic (often asynchronous) that might dispatch actions, have access to `StateContext`,
but it's not allowed to access the state. To change the state it must emit an action.

```kotlin
public fun interface Effect {

    public fun StateContext.receive(dispatch: Dispatch)
}
```

The `Store` evaluates all effects in the same order as they were queued, after the main reducer call.

When reducer needs to just emit a series of actions, it has to convert it to effect.
It can be achieved with predefined `ListEffect`

```kotlin
val counterReducer = typed<State, FooAction> { action ->
    when (action) {
        is FooAction.A -> queue(ListEffect(listOf(FooAction.B, FooAction.C))) 
        is FooAction.B ->  println("B")
        is FooAction.C ->  println("C")
    }
}
// ...
store.dispatch(FooAction.A) // prints "B" and "C"
```

It's a lot of boilerplate that can be simplified with `queue` extension:
```kotlin
// ...
import com.daftmobile.efekt.queue
// ...
when (action) {
    is FooAction.A -> queue(FooAction.B, FooAction.C)
// ...
```

For asynchronous logic use `FlowEffect`:

```kotlin
data class GetBookEffect(val id: String) : FlowEffect({
    // it's an extension on `StateContext` from `efekt-data`
    // it provides an abstraction over data sources
    // accepts a request and returns the flow
    callDataSource(DataSources.GetBook, id) 
        .map { BookAction.Loaded(it) }
        .catch { ErrorAction.Failed(it) }
})
```

## State context

`StateContext` is a map of singletons that are accessible by the `Key` object. It's a mechanism of extending
the store's functionality. Also, it's kind of dependency injection mechanism. It's not very convenient to use as a 
DI, but it's easy to integrate the DI tool easily:

```kotlin
class KoinApp(val application: KoinApplication) : StateContext.Element {

    override fun toString(): String = "KoinApp($application)"

    override val key = Key

    companion object Key : StateContext.Key<KoinApp>
}

val store = Store(..., initialContext = KoinApp(...))

// ...

store.context[KoinApp].application.koin.get<HttpClient>()
```

It's recommended to provide extension accessors for context elements:

```kotlin
val StateContext.koin: Koin get() = get(KoinApp).application.koin
// ...
store.context.koin.get<HttpClient>()
```

The other example is integrating the time source to the context:

```kotlin

val StateContext.clock: Clock get() = get(ContextClock)

fun Clock.toContextClock(): StateContext = if (this is ContextClock) this else ContextClock(this)

private class ContextClock(
    private val clock: Clock,
) : StateContext.Element, Clock by clock {

    override fun toString(): String = "ContextClock($clock)"

    override val key = Key

    companion object Key : StateContext.Key<ContextClock>
}
```

To inject it with `KoinApp` just use the plus operator:

```kotlin
val store = Store(..., initialContext = KoinApp(...) + Clock.System.toContextClock())
```