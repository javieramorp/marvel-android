package com.android.marvel.common.extensions

import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KType

/**
 * You can find the method explanation here on this post:
 * https://zaqueusantos.medium.com/an-easy-way-to-create-fixture-or-dummy-classes-for-your-android-tests-with-kotlin-9d6c619237d4
 */

inline fun <reified T : Any> fixture(noinline overrides: () -> Map<String, Any?> = { emptyMap() }): T =
    mountClass(T::class.constructors, overrides)

fun <T : Any> fixture(kClass: KClass<T>, overrides: () -> Map<String, Any?> = { emptyMap() }): T =
    mountClass(kClass.constructors, overrides)

fun <T : Any> mountClass(
    constructors: Collection<KFunction<T>>,
    overrides: () -> Map<String, Any?> = { emptyMap() },
): T {
    val ktSerializationArgName = "serializationConstructorMarker"

    val ktSerializationArg = constructors.first().parameters.firstOrNull { it.name == ktSerializationArgName }
    val constructor = if (ktSerializationArg == null) constructors.first() else constructors.last()

    val parameters = constructor.parameters
        .map { parameter ->
            overrides().getOrDefault(parameter.name, getRandomParameterValue(parameter.type))
        }

    return constructor.call(*parameters.toTypedArray())
}

private fun getRandomParameterValue(type: KType): Any {
    val classifier = type.classifier as? KClass<*>
    return when {
        classifier == String::class -> ('a'..'z').map { it }.shuffled().subList(0, 6).joinToString("")
        classifier == Char::class -> ('a'..'z').random()
        classifier == Boolean::class -> Random.nextBoolean()
        classifier == Int::class -> Random.nextInt()
        classifier == Long::class -> Random.nextLong()
        classifier == Short::class -> Random.nextInt().toShort()
        classifier == Byte::class -> Random.nextInt().toByte()
        classifier == Float::class -> Random.nextFloat()
        classifier == Double::class -> Random.nextDouble()
        classifier == List::class -> {
            val elementType = type.arguments.firstOrNull()?.type
                ?: throw IllegalArgumentException("Cannot process types without type arguments: $type")
            List(1) { getRandomParameterValue(elementType) }
        }

        classifier?.isData == true -> fixture(classifier)
        classifier?.java?.isEnum == true -> {
            val enumConstants = classifier.java.enumConstants
            enumConstants[Random.nextInt(enumConstants.size)]
        }

        else -> throw IllegalArgumentException("Unsupported type: $classifier")
    }
}