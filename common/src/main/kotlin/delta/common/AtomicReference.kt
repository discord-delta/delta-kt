package delta.common

import kotlin.reflect.KProperty

class AtomicReference<T>(@Volatile var value: T) {

    operator fun getValue(thisRef: Any?, prop: KProperty<*>): T {
        return value
    }

    operator fun setValue(thisRef: Any?, prop: KProperty<*>, value: T) {
        this.value = value
    }
}