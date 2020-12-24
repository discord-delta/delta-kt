package delta.request

interface ApiRequest<T> {

    fun then(callback: (T) -> Unit): ApiRequest<T>

    fun catch(callback: (Throwable) -> Unit): ApiRequest<T>

    fun <U> map(callback: (T) -> U): ApiRequest<U>

    fun <U> flatMap(callback: (T) -> ApiRequest<U>)

    fun schedule(delay: Long): ApiRequest<T>

    suspend fun await(): T
}