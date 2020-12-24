package delta.request

import kotlin.reflect.KProperty

class BulkApiRequest<T> {

    internal val pipeline: Pipeline<T> = Pipeline()

    fun filter(filter: (T) -> Boolean): BulkApiRequest<T> {
        return this
    }

    fun forEach(callback: (T) -> Unit): BulkApiRequest<T> {
        return this
    }

    fun <U> map(callback: (T) -> U): BulkApiRequest<U> {
        val next = BulkApiRequest<U>()
        pipeline.map(next.pipeline)
        return next
    }

    fun <U> flatMap(callback: (T) -> ApiRequest<U>): BulkApiRequest<U> {
        return BulkApiRequest()
    }

    fun collect(collection: MutableCollection<T>): BulkApiRequest<T> {
        return this
    }

    suspend fun await() {

    }
}

internal class Pipeline<T> {

    fun <U> map(other: Pipeline<U>) {

    }

    fun pass(value: T) {

    }

    fun error(error: Throwable) {

    }
}

class ErroneousDelegate<T> {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        throw IllegalStateException("This property does not hold a value")
    }
}

suspend fun test() {
    val request: BulkApiRequest<String> by ErroneousDelegate()

    val lengths = mutableListOf<Int>()

    request.forEach { name -> println(name) }
        .map { name -> name.length }
        .filter { len -> len % 2 == 1 }
        .collect(lengths)
        .await()
}