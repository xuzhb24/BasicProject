package com.android.widget.AlignTextView

/**
 * Created by xuzhb on 2021/4/14
 */
object ArrayUtil {

    private const val CACHE_SIZE = 73
    private val mCache = arrayOfNulls<Any>(CACHE_SIZE)

    /**
     * Returns an empty array of the specified type.  The intent is that
     * it will return the same empty array every time to avoid reallocation,
     * although this is not guaranteed.
     */
    fun <T> emptyArray(kind: Class<T>): Array<T>? {
        if (kind == Any::class.java) {
            return arrayOfNulls<Any>(0) as? Array<T>
        }
        val bucket = (kind.hashCode() and 0x7FFFFFFF) % CACHE_SIZE
        var cache = mCache[bucket]
        if (cache == null || cache.javaClass.componentType != kind) {
            cache = java.lang.reflect.Array.newInstance(kind, 0)
            mCache[bucket] = cache
        }
        return cache as? Array<T>
    }

}