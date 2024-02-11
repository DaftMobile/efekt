package com.daftmobile.efekt.data

import com.daftmobile.efekt.StateContext

public interface DataSourceKey<T : DataSource<*, *>>
public typealias SimpleDataSourceKey<Request, Response> = DataSourceKey<DataSource<Request, Response>>

public val StateContext.dataSourceResolver: DataSourceResolver get() = get(DataSourceResolver)

public fun DataSourceResolver(
    block: DataSourceResolverConfig.() -> Unit
): DataSourceResolver = DslDataSourceResolver().apply(block)

public interface DataSourceResolver : StateContext.Element {

    override val key: Key get() = Key

    public operator fun <T : DataSource<*, *>> get(key: DataSourceKey<T>): T?

    public companion object Key : StateContext.Key<DataSourceResolver>
}

public interface DataSourceResolverConfig {

    public infix fun <T : DataSource<*, *>> DataSourceKey<T>.resolvesTo(dataSource: T)

    public infix fun <T : DataSource<*, *>> DataSourceKey<T>.resolvedBy(dataSourceProvider: () -> T)
}

@Suppress("UNCHECKED_CAST")
private class DslDataSourceResolver : DataSourceResolverConfig, DataSourceResolver {

    private val constMappings = mutableMapOf<DataSourceKey<*>, DataSource<*, *>>()
    private val providerMappings = mutableMapOf<DataSourceKey<*>, () -> DataSource<*, *>>()

    override fun <T : DataSource<*, *>> get(key: DataSourceKey<T>): T? {
        return constMappings[key] as? T ?: providerMappings[key]?.invoke() as? T
    }

    override fun <T : DataSource<*, *>> DataSourceKey<T>.resolvesTo(dataSource: T) {
        constMappings[this] = dataSource
    }

    override fun <T : DataSource<*, *>> DataSourceKey<T>.resolvedBy(dataSourceProvider: () -> T) {
        providerMappings[this] = dataSourceProvider
    }

    override fun toString(): String = "DataSourceResolver($constMappings)"
}
