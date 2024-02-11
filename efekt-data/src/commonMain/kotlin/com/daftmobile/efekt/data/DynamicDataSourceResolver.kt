package com.daftmobile.efekt.data

public fun DataSourceResolver.Key.chained(
    vararg resolvers: DataSourceResolver
): DataSourceResolver = dynamic("chainOf=[${resolvers.joinToString()}]") { key ->
    resolvers.firstNotNullOfOrNull { it[key] }
}

public fun DataSourceResolver.Key.dynamic(
    description: String? = null,
    block: (key: DataSourceKey<*>) -> Any?
): DataSourceResolver = DynamicDataSourceResolverImpl(description, block)

@Suppress("UNCHECKED_CAST")
private class DynamicDataSourceResolverImpl(
    private val description: String? = null,
    private val block: (key: DataSourceKey<*>) -> Any?
) : DataSourceResolver {

    override fun <T : DataSource<*, *>> get(key: DataSourceKey<T>): T? = block(key) as T?

    override fun toString(): String = "DynamicDataSourceResolver(${description ?: "..."})"
}
