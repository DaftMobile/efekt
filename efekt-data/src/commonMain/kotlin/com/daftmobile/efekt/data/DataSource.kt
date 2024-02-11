package com.daftmobile.efekt.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

public fun interface DataSource<in Request, out Response> {

    public fun call(request: Request): Flow<Response>
}

public fun <Req, Res> dataSourceFlow(block: suspend FlowCollector<Res>.(Req) -> Unit): DataSource<Req, Res> {
    return DataSource { request ->
        flow { block(request) }
    }
}

public fun <Req, Res> dataSourceOf(vararg values: Res): DataSource<Req, Res> = dataSourceFlow {
    values.forEach { emit(it) }
}
