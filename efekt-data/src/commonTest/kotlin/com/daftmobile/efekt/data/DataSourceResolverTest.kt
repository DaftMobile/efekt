package com.daftmobile.efekt.data

import io.kotest.matchers.collections.shouldNotContainDuplicates
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class DataSourceResolverTest {

    private val getCountDataSource =  dataSourceOf<Unit, Int>(3)
    private val getElementsDataSource =  dataSourceOf<Unit, List<String>>(listOf("1", "2", "3"))

    private val resolver = DataSourceResolver {
        Keys.GetCount resolvesTo getCountDataSource
        Keys.GetElements resolvesTo getElementsDataSource
        Keys.PostText resolvedBy  { dataSourceOf(Unit) }
    }

    @Test
    fun testReturnsConfiguredConstReferencesWithResolvesTo() {
        resolver[Keys.GetCount] shouldBe getCountDataSource
        resolver[Keys.GetCount] shouldBe getCountDataSource
        resolver[Keys.GetElements] shouldBe getElementsDataSource
        resolver[Keys.GetElements] shouldBe getElementsDataSource
    }

    @Test
    fun testReturnsConfiguredWithResolvedBy() {
        val sources = listOf(resolver[Keys.PostText], resolver[Keys.PostText], resolver[Keys.PostText])
        sources.shouldNotContainDuplicates()
    }

    @Test
    fun testReturnsNullWhenNotConfiguredSources() {
        resolver[Keys.GetElement].shouldBeNull()
    }
}

private object Keys {
    object GetCount : SimpleDataSourceKey<Unit, Int>
    object GetElements : SimpleDataSourceKey<Unit, List<String>>
    object GetElement : SimpleDataSourceKey<Int, String>
    object PostText : SimpleDataSourceKey<String, Unit>
}
