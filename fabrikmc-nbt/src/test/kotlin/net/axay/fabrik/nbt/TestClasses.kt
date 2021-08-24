package net.axay.fabrik.nbt

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TestClass(
    val x: Int,
    val y: Long,
    val name: String,
    val stringList: List<String>,
    val longSet: Set<Long>,
    val inner: InnerTestClass,
    val nullable: Short?,
)

@Serializable
data class InnerTestClass(val test: Boolean)

@Serializable
data class TestClassWithDefault(val one: Int = 1, val tru: Boolean = true)

enum class TestEnum {
    VARIANT_1, VARIANT_2, LastVariant,
}

@Serializable
sealed class SealedBase {
    abstract val baseVal: Float
}

@Serializable
@SerialName("child1")
data class SealedChild1(override val baseVal: Float, val childProp: Double) : SealedBase()

@Serializable
@SerialName("child2")
data class SealedChild2(override val baseVal: Float, val otherChildProp: String) : SealedBase()
