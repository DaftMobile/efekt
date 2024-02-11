package com.daftmobile.efekt.data

import com.daftmobile.efekt.StateContext
import kotlinx.coroutines.flow.Flow

public fun <Request, Response> StateContext.callDataSource(
    key: DataSourceKey<DataSource<Request, Response>>, request: Request
): Flow<Response> = dataSourceResolver[key]?.call(request) ?: error("Failed to find data source with $key!")

public fun <Response> StateContext.callDataSource(
    key: DataSourceKey<DataSource<Unit, Response>>
): Flow<Response> = callDataSource(key, Unit)
